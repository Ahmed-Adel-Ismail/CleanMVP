package com.base.presentation.base.abstracts.features;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.base.abstraction.R;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.abstraction.annotations.readers.ClassAnnotationReader;
import com.base.abstraction.annotations.scanners.ClassAnnotationScanner;
import com.base.abstraction.commands.params.ArgsGen;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.events.Message;
import com.base.abstraction.exceptions.GenericTypeInitializerException;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.logs.SystemLogger;
import com.base.abstraction.messaging.AbstractMailbox;
import com.base.abstraction.messaging.Mailbox;
import com.base.abstraction.observer.ObservableAdd;
import com.base.abstraction.observer.ObservableRemove;
import com.base.abstraction.observer.Observer;
import com.base.abstraction.reflections.GenericTypeInitializer;
import com.base.abstraction.reflections.Initializer;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.presentation.annotations.interfaces.ActivityViewBinders;
import com.base.presentation.annotations.interfaces.BlockClicks;
import com.base.presentation.annotations.interfaces.Drawer;
import com.base.presentation.annotations.interfaces.Menu;
import com.base.presentation.annotations.interfaces.Splash;
import com.base.presentation.annotations.interfaces.Toolbar;
import com.base.presentation.base.loaders.AbstractLoaderViewBinder;
import com.base.presentation.exceptions.OnBackPressStoppedException;
import com.base.presentation.exceptions.OnKeyDownConsumedException;
import com.base.presentation.exceptions.OnTouchEventConsumedException;
import com.base.presentation.interfaces.Clickable;
import com.base.presentation.interfaces.IntentHolder;
import com.base.presentation.models.Model;
import com.base.presentation.models.ModelExchanger;
import com.base.presentation.models.NullModel;
import com.base.presentation.references.FieldsCleaner;
import com.base.presentation.requests.ActivityActionRequest;
import com.base.presentation.requests.PermissionResult;
import com.base.presentation.system.ManifestPermissions;
import com.base.presentation.system.SystemServices;

/**
 * The Base abstract Activity for All Activities for the Application, you must supply the
 * proper annotations for this class to build properly
 * <p>
 * <u>mandatory annotations :</u><br>
 * {@link ActivityViewBinders} : the {@link ViewBinder} classes for this Activity
 * <p>
 * <u>optional annotations :</u><br>
 * {@link Toolbar} : the toolbar for this activity ... it should be present in the xml layout<br>
 * {@link Drawer} : the {@link DrawerLayout} details for this activity, it should be present in xml layout<br>
 * {@link Menu} : the menu details for this activity<br><br>
 * {@link Splash} : mark this activity as the <b>Splash</b> activity<br>
 * {@link Address} : the Actor address of this Activity<br>
 * {@link BlockClicks} : the views ids that should not be clicked with each other at the same time
 * <p>
 * Created by Ahmed Adel on 8/31/2016.
 *
 * @see Splash
 * @see ActivityViewBinders
 * @see Address
 * @see BlockClicks
 * @see Toolbar
 * @see Drawer
 * @see Menu
 */
public abstract class AbstractActivity<T extends Model>
        extends AppCompatActivity implements
        Feature<T>,
        Clickable,
        CreatableView,
        Observer,
        ObservableAdd,
        ObservableRemove,
        IntentHolder {

    private static final int MODEL_GENERIC_ARG_INDEX = 0;
    private T model;
    private ViewBinder viewBinder;
    private ScreenLifeCycle screenLifeCycle;
    private SystemServices systemServices;
    private ManifestPermissions manifestPermission;
    private MultipleClicksHandler multipleClicksHandler;
    private boolean lockedInteractions;
    private Mailbox<Event> mailbox;
    private long address;
    private long menuId;
    private DrawerLayout drawerLayout;
    private Intent intent;
    private Bundle savedInstanceState;

    public AbstractActivity() {
        this.address = createAddress();
        this.systemServices = new SystemServices(this);
        this.manifestPermission = new ManifestPermissions(this);
        this.model = createModel();
        this.menuId = createMenuId();
        createBlockClicksScanner().execute(this);
    }


    private long createAddress() {
        long address = R.id.addressActivity;
        ClassAnnotationReader<Address> reader;
        reader = new ClassAnnotationReader<>(Address.class);
        try {
            address = reader.execute(this).value();
        } catch (AnnotationNotDeclaredException e) {
            Logger.getInstance().info(getClass(), "no @" +
                    Address.class.getSimpleName() + " declared");
        }
        return address;
    }

    private long createMenuId() {
        new ClassAnnotationScanner<Menu>(Menu.class) {
            @Override
            protected void onAnnotationFound(Menu annotation) {
                menuId = annotation.value();
            }
        }.execute(this);
        return menuId;
    }

    /**
     * get the {@link com.base.presentation.models.Model} that represents the Model for this Activity / Feature
     *
     * @return the {@link com.base.presentation.models.Model}, or
     * {@link NullModel} if non is available
     * @deprecated this method creates a {@link Model} from the generic type argument and
     * calls it's {@link Model#initialize(Observer)}
     */
    @SuppressWarnings("unchecked")
    protected T createModel() {
        T model = null;
        try {
            model = new GenericTypeInitializer<T>(MODEL_GENERIC_ARG_INDEX).execute(getClass());
            model.initialize(this);
        } catch (GenericTypeInitializerException e) {
            Logger.getInstance().exception(e);
        }
        return model;
    }

    @NonNull
    private ClassAnnotationScanner<BlockClicks> createBlockClicksScanner() {
        return new ClassAnnotationScanner<BlockClicks>(BlockClicks.class) {
            @Override
            protected void onAnnotationFound(BlockClicks annotation) {
                initializeMultipleClicksHandler(annotation);
            }
        };
    }

    private void initializeMultipleClicksHandler(BlockClicks annotation) {
        int[] viewsIds = annotation.value();
        int length = viewsIds.length;
        Integer[] intViewsIds = new Integer[length];
        for (int i = 0; i < length; i++) {
            intViewsIds[i] = viewsIds[i];
        }
        setMultipleClicksHandler(new MultipleClicksHandler(intViewsIds));
    }


    /**
     * @deprecated use {@link BlockClicks} annotations instead
     */
    @Deprecated
    public void setMultipleClicksHandler(MultipleClicksHandler handler) {
        multipleClicksHandler = handler;
        multipleClicksHandler.setAbstractActivity(this);
    }


    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.intent = getIntent();
        this.savedInstanceState = savedInstanceState;
        new IntentLoader(model).execute(intent);
        onCreateView(null, null, savedInstanceState);
        onActivityCreated(savedInstanceState);
        overridePendingTransition(getEnterAnimationResourceId(), getExitAnimationResourceId());
    }

    /**
     * get the latest {@link Bundle} received in {@link #onCreate(Bundle)}
     *
     * @return the latest {@link Bundle} received
     */
    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater,
                                   @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {

        mailbox = new Mailbox<>(new ActivityMailboxExecutor(this), Looper.getMainLooper());
        if (App.getInstance().getAppLoader().isReady()) {
            viewBinder = createViewBinder(savedInstanceState);
        } else {
            viewBinder = createLoaderViewBinder(savedInstanceState);
        }
        screenLifeCycle = new ScreenLifeCycleImplementer(this, viewBinder);
        return screenLifeCycle.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public ViewBinder createViewBinder(Bundle savedInstanceState) {
        ViewBinder viewBinder = null;
        try {
            ClassAnnotationReader<ActivityViewBinders> reader;
            reader = new ClassAnnotationReader<>(ActivityViewBinders.class);
            viewBinder = new Initializer<ViewBinder>().execute(reader.execute(this).value());
            viewBinder.initialize(this);
        } catch (Throwable e) {
            new TestException().execute(e);
        }
        return viewBinder;
    }

    /**
     * create the {@link ViewBinder} that will be responsible for the loading screen
     *
     * @param savedInstanceState the parameter passed to
     *                           {@link android.app.Activity#onCreate(Bundle)}
     * @return the {@link ViewBinder} that will hold the true implementation for
     * this Activity in case that this will be the splash activity or an activity
     * that will display Loading UI
     */
    protected final ViewBinder createLoaderViewBinder(Bundle savedInstanceState) {
        AbstractLoaderViewBinder viewBinder;
        try {
            ClassAnnotationReader<ActivityViewBinders> reader;
            reader = new ClassAnnotationReader<>(ActivityViewBinders.class);
            viewBinder = new Initializer<AbstractLoaderViewBinder>().execute(reader.execute(this).error());
            viewBinder.initialize(this);
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
            return createViewBinder(savedInstanceState);
        }
        return viewBinder;
    }


    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        screenLifeCycle.onActivityCreated(savedInstanceState);
    }

    @Override
    public final void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        screenLifeCycle.onActivityResult(requestCode, resultCode, data);
    }

    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        screenLifeCycle.onSaveInstanceState(bundle);
    }

    @Override
    public final void onStart() {
        super.onStart();
        screenLifeCycle.onStart();
    }

    @Override
    public final void onResume() {
        super.onResume();
        screenLifeCycle.onResume();
        App.getInstance().setForegroundActivity(this);
    }

    @Override
    public final void onPause() {
        screenLifeCycle.onPause();
        overridePendingTransition(getEnterAnimationResourceId(), getExitAnimationResourceId());
        App.getInstance().removeForegroundActivity(this);
        getSystemServices().hideKeyboard();
        super.onPause();
    }

    @Override
    public final void onStop() {
        screenLifeCycle.onStop();
        super.onStop();
    }

    @Override
    public final void onDestroy() {
        screenLifeCycle.onDestroy();
        super.onDestroy();
        model.clear();

        // do not null-reference the viewBinder before super.onDestroy()
        viewBinder = null;

        model.clear();
        model = null;

        mailbox.clear();
        mailbox = null;

        drawerLayout = null;

        if (systemServices != null) {
            systemServices.clear();
            systemServices = null;
        }

        if (manifestPermission != null) {
            manifestPermission.clear();
            manifestPermission = null;
        }

        if (multipleClicksHandler != null) {
            multipleClicksHandler.clear();
            multipleClicksHandler = null;
        }

        intent = null;
        savedInstanceState = null;
        new FieldsCleaner(true).execute(this);

    }

    @Override
    public final AbstractMailbox<Event> getMailbox() {
        return mailbox;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final AbstractActivity<T> getHostActivity() {
        return this;
    }

    @Override
    public SystemServices getSystemServices() {
        return systemServices;
    }

    @Override
    public ManifestPermissions getManifestPermissions() {
        return manifestPermission;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionResult result = new PermissionResult(requestCode, permissions, grantResults);
        Message message = new Message.Builder().id(requestCode).content(result).build();
        notifyOnUpdateModel();
        viewBinder.onUpdate(new EventBuilder(R.id.onRequestPermissionsResult, message).execute(this));
    }

    @Override
    public void onUpdate(Event event) throws RuntimeException {
        mailbox.execute(event);
    }


    /**
     * a method that invokes {@link ViewBinder#onUpdate(Event)} without any checks,
     * to be used from the {@link com.base.presentation.listeners.OnEventListener} or
     * similar classes that will setVariable
     * {@link com.base.abstraction.events.Event.Builder#senderActorAddress} with the same id of
     * this Class
     *
     * @param event the {@link Event} to be propagated to
     *              {@link ViewBinder#onUpdate(Event)} without any checks
     */
    public void onUpdateWithSameAddress(Event event) {
        viewBinder.onUpdate(event);
    }


    @Override
    public void addObserver(Observer observer) {
        viewBinder.addObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        viewBinder.removeObserver(observer);
    }

    @Override
    public final void onClick(View view) {
        if (multipleClicksHandler != null) {
            multipleClicksHandler.handleOnClick(view);
        } else {
            invokeOnClick(view);
        }
    }


    void invokeOnClick(View view) {
        if (hasLockedInteractions()) {
            onLockedInteractions();
        } else {
            doInvokeOnClick(view);
        }

    }

    private void doInvokeOnClick(View view) {
        Message message = new Message.Builder().id(view.getId()).content(view).build();
        notifyOnUpdateModel();
        viewBinder.onUpdate(new EventBuilder(R.id.onClick, message).execute(this));
    }

    final ViewBinder getViewBinder() {
        return viewBinder;
    }

    @Override
    public void onBackPressed() {
        if (hasLockedInteractions()) {
            onLockedInteractions();
        } else {
            invokeOnBackPressed();
        }
    }

    /**
     * set the {@link DrawerLayout} that will control the {@link NavigationView}
     *
     * @param drawerLayout the {@link DrawerLayout} to be used
     */
    void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    private void invokeOnBackPressed() {
        notifyOnUpdateModel();
        try {
            invokeSelfOnBackPressed();
            viewBinder.onUpdate(new EventBuilder(R.id.onBackPressed).execute(this));
            super.onBackPressed();
        } catch (OnBackPressStoppedException exception) {
            Logger.getInstance().error(getClass(), "onBackPressed() stopped");
        }
    }

    private void invokeSelfOnBackPressed() {
        if (drawerLayout != null && isCloseNavigationPerformed()) {
            throw new OnBackPressStoppedException();
        }
    }

    private boolean isCloseNavigationPerformed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            return true;
        }
        return false;
    }


    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        Message message = new Message.Builder().content(menu).build();
        notifyOnUpdateModel();
        viewBinder.onUpdate(new EventBuilder(R.id.onPrepareOptionsMenu, message).execute(this));
        return true;
    }

    @Override
    public final boolean onCreateOptionsMenu(android.view.Menu menu) {
        Message message = new Message.Builder().content(menu).build();
        notifyOnUpdateModel();
        if (menuId != 0) {
            getMenuInflater().inflate((int) menuId, menu);
            viewBinder.onUpdate(new EventBuilder(R.id.onCreateOptionsMenu, message).execute(this));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Message message = new Message.Builder().id(item.getItemId()).content(item).build();
        notifyOnUpdateModel();
        viewBinder.onUpdate(new EventBuilder(R.id.onOptionItemSelected, message).execute(this));
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            viewBinder.onUpdate(new EventBuilder(R.id.onTouchEvent, event).execute(this));
            return super.onTouchEvent(event);
        } catch (OnTouchEventConsumedException e) {
            Logger.getInstance().error(getClass(), "onTouchEvent() consumed");
            return true;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            OnKeyDownParams params = new OnKeyDownParams(keyCode, event);
            viewBinder.onUpdate(new EventBuilder(R.id.onKeyDown, params).execute(this));
            return super.onKeyDown(keyCode, event);
        } catch (OnKeyDownConsumedException e) {
            Logger.getInstance().error(getClass(), "onKeyDown() consumed");
            return true;
        }

    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public final T getModel() {
        return model;
    }

    /**
     * exchange the value model with a new {@link com.base.presentation.models.Model}, notice that this method will
     * trigger {@link R.id#onExchangeModel} event after changing the model
     *
     * @param newModel the new {@link com.base.presentation.models.Model}
     */
    public final void exchangeModel(T newModel) {
        this.model = new ModelExchanger<T>().execute(ArgsGen.create(model, newModel));
        viewBinder.onUpdate(new EventBuilder(R.id.onExchangeModel).execute(this));
    }

    @Override
    public final long getActorAddress() {
        return address;
    }


    @Override
    public View getView() {
        return findViewById(android.R.id.content);
    }

    /**
     * check if the value {@link AbstractActivity} is locked or not
     *
     * @return {@code true} if this Activity is locked, which means that interactions to the UI
     * should be disabled until a call to {@link #unlockInteractions()} is made
     */
    public final boolean hasLockedInteractions() {
        return lockedInteractions;
    }

    /**
     * lock all the UI interactions to this activity, you should call {@link #unlockInteractions()}
     * to enable interactions again, else the screen will stay locked for ever
     */
    public final void lockInteractions() {
        lockedInteractions = true;
    }

    /**
     * if {@link #lockInteractions()} was invoked, this method should be invoked to
     * unlock the UI and make it possible for the user to interact with it ... it is safe to invoke
     * this method multiple times
     */
    public final void unlockInteractions() {
        lockedInteractions = false;
    }


    public final void addFragment(int containerId, @NonNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(containerId, fragment).commit();
    }

    public final void removeFragment(@NonNull Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        } catch (Throwable e) {
            SystemLogger.getInstance().error(getClass(), "error @ removeFragment(" +
                    fragment.getClass().getSimpleName() + ")");
            SystemLogger.getInstance().exception(e);
        }

    }

    @Override
    public final void startActivityForResult(Intent intent, int requestCode) {
        new IntentSaver(model).execute(intent);
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public final ComponentName startService(Intent intent) {
        new IntentSaver(model).execute(intent);
        return super.startService(intent);
    }

    @Override
    public final void startActivityActionRequest(Event event) {
        ActivityActionRequest request = event.getMessage().getContent();
        new ActivityActionHandler(request).execute(viewBinder);
    }

    protected void notifyOnUpdateModel() {
        if (viewBinder != null) {
            viewBinder.onUpdate(new EventBuilder(R.id.onUpdateModel).execute(this));
        }
    }


    /**
     * get the resource id of an animation file to be set as the enter-animation
     * in {@link #overridePendingTransition(int, int)} (first parameter)
     *
     * @return the entering animation, the default is {@code 0} which means that there will
     * be no animation
     */
    protected int getEnterAnimationResourceId() {
        return 0;
    }

    /**
     * get the resource id of an animation file to be set as the exit-animation
     * in {@link #overridePendingTransition(int, int)} (second parameter)
     *
     * @return the exiting animation, the default is {@code 0} which means that there will
     * be no animation
     */
    protected int getExitAnimationResourceId() {
        return 0;
    }


    /**
     * this method is invoked when there is an attempt to interact with the UI while
     * the UI is locked and {@link #hasLockedInteractions()} returns {@code true}
     * <p>
     * for sub-classes, you can override this method to do there specific implementations,
     * the default implementation is to display "please wait" toast on the screen
     */
    public void onLockedInteractions() {
        Toast.makeText(this, AppResources.string(R.string.please_wait), Toast.LENGTH_SHORT).show();
    }

}
