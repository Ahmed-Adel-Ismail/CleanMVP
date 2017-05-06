package com.base.presentation.base.abstracts.features;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.abstraction.messaging.AbstractMailbox;
import com.base.abstraction.messaging.Mailbox;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.abstraction.annotations.readers.ClassAnnotationReader;
import com.base.abstraction.events.Event;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;
import com.base.abstraction.logs.SystemLogger;
import com.base.abstraction.observer.Observer;
import com.base.presentation.references.FieldsCleaner;
import com.base.abstraction.reflections.Initializer;
import com.base.presentation.annotations.interfaces.FragmentViewBinder;
import com.base.presentation.interfaces.DestroyableClient;
import com.base.presentation.models.Model;
import com.base.presentation.system.ManifestPermissions;
import com.base.presentation.system.SystemServices;

/**
 * The Base abstract Fragment for All Fragments for the Application, you must supply the
 * required annotations for this class to build properly
 * <p>
 * <u>mandatory annotations :</u><br>
 * {@link Address} : the actor address to identify this Fragment<br>
 * {@link FragmentViewBinder} : the {@link ViewBinder} for this Fragment
 * <p>
 * Created by Ahmed Adel on 8/31/2016.
 *
 * @see Address
 * @see FragmentViewBinder
 */
public abstract class AbstractFragment<T extends Model> extends Fragment implements
        Feature<T>,
        DestroyableClient,
        Observer,
        CreatableView {

    private Mailbox<Event> mailbox;
    private FragmentMailboxExecutor mailboxExecutor;
    private ViewBinder viewBinder;
    private ScreenLifeCycle screenLifeCycle;
    private long address;

    public AbstractFragment() {
        this.address = readAddressFromAnnotation();
    }

    private long readAddressFromAnnotation() throws AnnotationNotDeclaredException {
        return new ClassAnnotationReader<>(Address.class).execute(this).value();
    }

    @Nullable
    @Override
    public final View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mailboxExecutor = new FragmentMailboxExecutor(this);
        mailbox = new Mailbox<>(mailboxExecutor, Looper.getMainLooper());
        viewBinder = createViewBinder(savedInstanceState);
        screenLifeCycle = new ScreenLifeCycleImplementer(this, viewBinder);
        return screenLifeCycle.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public ViewBinder createViewBinder(Bundle savedInstanceState) {
        ClassAnnotationReader<FragmentViewBinder> reader;
        reader = new ClassAnnotationReader<>(FragmentViewBinder.class);
        ViewBinder viewBinder;
        viewBinder = new Initializer<ViewBinder>().execute(reader.execute(this).value());
        viewBinder.initialize(this);
        return viewBinder;
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHostActivity().addObserver(this);
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
    }

    @Override
    public final void onPause() {
        screenLifeCycle.onPause();
        super.onPause();
    }


    @Override
    public final void onStop() {
        screenLifeCycle.onStop();
        super.onStop();
    }

    @Override
    public final void onDestroy() {
        getHostActivity().removeObserver(this);
        screenLifeCycle.onDestroy();
        super.onDestroy();
        // do not null-reference the viewBinder before super.onDestroy()
        viewBinder = null;

        mailbox.clear();
        mailbox = null;
        mailboxExecutor = null;
        new FieldsCleaner().execute(this);
    }

    @Override
    public final AbstractMailbox<Event> getMailbox() {
        return mailbox;
    }

    @Override
    public void onUpdate(Event event) throws RuntimeException {
//        mailbox.execute(event);
        mailboxExecutor.execute(event);
    }

    final ViewBinder getViewBinder() {
        return viewBinder;
    }

    @Override
    public final boolean isDestroyed() {
        return getActivity() == null || getActivity().isDestroyed();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final AbstractActivity<T> getHostActivity() {
        return (AbstractActivity<T>) getActivity();
    }

    @Override
    public SystemServices getSystemServices() {
        return getHostActivity().getSystemServices();
    }

    @Override
    public ManifestPermissions getManifestPermissions() {
        return getHostActivity().getManifestPermissions();
    }

    @NonNull
    @Override
    public T getModel() {
        return getHostActivity().getModel();
    }


    @Override
    public final void addFragment(int containerId, @NonNull Fragment fragment) {
        getChildFragmentManager().beginTransaction().add(containerId, fragment).commit();
    }

    @Override
    public final void removeFragment(@NonNull Fragment fragment) {
        try {
            getChildFragmentManager().beginTransaction().remove(fragment).commit();
        } catch (Throwable e) {
            SystemLogger.getInstance().error(getClass(), "error @ removeFragment(" +
                    fragment.getClass().getSimpleName() + ")");
            SystemLogger.getInstance().exception(e);
        }
    }

    @Override
    public final long getActorAddress() {
        return address;
    }

    @Override
    public final void startActivityActionRequest(Event event) {
        getHostActivity().startActivityActionRequest(event);
    }
}
