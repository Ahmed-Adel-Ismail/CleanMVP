package com.base.abstraction.system;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;

import com.base.abstraction.actors.base.Actor;
import com.base.abstraction.actors.registries.ActorSystem;
import com.base.abstraction.annotations.interfaces.ApplicationLoader;
import com.base.abstraction.annotations.interfaces.Behavior;
import com.base.abstraction.annotations.interfaces.BroadcastsHandler;
import com.base.abstraction.annotations.interfaces.Integration;
import com.base.abstraction.api.usecases.AbstractIntegrationHandler;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.exceptions.failures.OAuth2Failure;
import com.base.abstraction.exceptions.failures.SessionExpiredFailure;
import com.base.abstraction.references.CheckedReference;
import com.base.abstraction.system.readers.AppAnnotationsReader;

import java.lang.ref.WeakReference;
import java.util.Locale;

import static com.base.abstraction.system.Behaviors.DEBUGGING;

/**
 * the base abstract class for any {@link Application} class
 * <p/>
 * <u>mandatory annotations :</u><br>
 * {@link Integration} : the {@link AbstractIntegrationHandler} implementer, there is a
 * default implementer in the use-cases layer which handles locating URLs and required
 * values to make integration / use-cases layer active through annotation processing<br>
 * <p>
 * <u>optional annotations :</u><br>
 * {@link com.base.abstraction.annotations.interfaces.Actor} : the {@link Actor} classes required
 * to be alive while application is running ... the main Actors required for an Authorized app
 * are a handler for {@link OAuth2Failure} and a handler for {@link SessionExpiredFailure}<br>
 * {@link Behavior} : the {@link Behaviors} of the application, the default is
 * {@link Behaviors#DEBUGGING} if this annotation is not mentioned<br>
 * {@link ApplicationLoader} : the {@link AppLoader} class or it's sub-class<br>
 * {@link BroadcastsHandler} : declare an {@link Executor} that will handle actions
 * triggered by the {@link GlobalBroadcastReceiver} of the application
 * <p>
 * Created by Ahmed Adel on 8/31/2016.
 *
 * @see Behavior
 * @see ApplicationLoader
 * @see Integration
 * @see com.base.abstraction.annotations.interfaces.Actor
 * @see BroadcastsHandler
 */
public abstract class App extends Application {


    private static CheckedReference<App> instance;

    private ActorSystem actorSystem;
    private Behaviors behavior;
    private AbstractIntegrationHandler integrationHandler;
    private AppLoader appLoader;
    private long startTime;
    private Locale locale;
    private boolean debugging;
    private Class<?> foregroundActivityClass;


    public App() {
        actorSystem = new ActorSystem();

    }

    /**
     * get the instance of the current {@link Application}
     *
     * @return the instance of this application, or {@code null} if the application is
     * not available, which is hard to happen (stored in a {@link WeakReference})
     */
    @SuppressWarnings("unchecked")
    public static <T extends App> T getInstance() {
        return (T) instance.get();
    }

    @Override
    public final void onCreate() {
        super.onCreate();
        startTime = System.currentTimeMillis();
        instance = new CheckedReference<>(this);
        locale = Locale.getDefault();


        AppAnnotationsReader.BEHAVIOR.execute(this);
        debugging = isBehaviorAccepted(DEBUGGING);

        AppAnnotationsReader.INTEGRATION.execute(this);
        AppAnnotationsReader.LOADER.execute(this);
        AppAnnotationsReader.ACTOR.execute(this);
        AppAnnotationsReader.BROADCASTS_EXECUTOR.execute(this);

        initialize();
        Thread.setDefaultUncaughtExceptionHandler(new FailureHandlingExceptionHandler());
    }


    /**
     * implement this method to initialize the instance, do not call {@code super.onCreate()}
     * in this method, it is already called
     */
    protected void initialize() {
        // template method
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * check if the {@link Application} state is currently accepting debugging / logging
     *
     * @return {@code true} if the debugging / logging behavior is accepted
     */
    public final boolean isDebugging() {
        return debugging;
    }

    /**
     * check if the Behavior is accepted or not based on the return of this method, for example,
     * if you want to Log an event while the instance's Behavior is {@link Behaviors#TESTING}
     * only, you can invoke this method before invoking your block of code (that logs), and if
     * the Application's Behavior is {@link Behaviors#TESTING}, it will return {@code true} so
     * you can proceed with your code, else it will return {@code false}
     *
     * @param behavior the {@link Behaviors Behavior} to check if it is compatible with the
     *                 current Application Behaviors (set through {@link #setBehavior(Behaviors)})
     *                 notice that if the Application's current Behavior was set to
     *                 {@link Behaviors#TESTING}, this method will always return {@code true}
     *                 <p/>
     *                 if it was set to {@link Behaviors#DEBUGGING}, it will return
     *                 {@code true} if the passed value was {@link Behaviors#DEBUGGING}
     *                 or {@link Behaviors#MARKET}
     *                 <p/>
     *                 if it was set to {@link Behaviors#MARKET}, it will return {@code true}
     *                 only if the passed value was {@link Behaviors#MARKET}
     * @return {@code true} if you can proceed with your code, or {@code false} if this block
     * of code (Behavior) should not be invoked
     */
    public final boolean isBehaviorAccepted(@NonNull Behaviors behavior) {
        return behavior.isLessFlexible(this.behavior);
    }

    /**
     * set the current accepted Behaviors, you can validate for those behaviors later on
     * through {@link #isBehaviorAccepted(Behaviors)}
     *
     * @param behavior the accepted {@link Behaviors} , either {@link Behaviors#MARKET},
     *                 {@link Behaviors#DEBUGGING} or {@link Behaviors#TESTING}
     *                 the default type is {@link Behaviors#MARKET}
     */
    public final void setBehavior(Behaviors behavior) {
        this.behavior = behavior;
    }


    /**
     * get the {@link ActorSystem} that holds all the addresses for actors
     *
     * @return the {@link ActorSystem} for the application
     */
    public ActorSystem getActorSystem() {
        return actorSystem;
    }

    /**
     * returns the {@link AbstractIntegrationHandler} sub-class for the project
     *
     * @param <T> a sub-class of {@link AbstractIntegrationHandler}
     * @return the instance casted to the desired type
     * @throws ClassCastException if the expected type did not match the desired type
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractIntegrationHandler> T getIntegrationHandler() throws ClassCastException {
        return (T) integrationHandler;
    }

    public Locale getLocale() {
        return locale;
    }

    /**
     * set the {@link Locale} of the application
     *
     * @param locale the new {@link Locale}
     */
    public void setLocale(@NonNull Locale locale) {
        this.locale = locale;
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = new Locale(locale.getLanguage().toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);
    }

    /**
     * set the passed activity as the current foreground {@link Activity}
     *
     * @param foregroundActivity the foreground {@link Activity}
     */
    public void setForegroundActivity(Activity foregroundActivity) {
        this.foregroundActivityClass = foregroundActivity.getClass();
    }

    /**
     * remove the passed {@link Activity} from being the current foreground {@link Activity}
     *
     * @param foregroundActivity the activity that should be removed
     */
    public void removeForegroundActivity(Activity foregroundActivity) {
        Class<?> klass = foregroundActivity.getClass();
        if (this.foregroundActivityClass != null && this.foregroundActivityClass.equals(klass)) {
            this.foregroundActivityClass = null;
        }
    }

    /**
     * check if there is currently a foreground {@link Activity} for the application or it is
     * only live in background
     *
     * @return {@code true} if the application is in foreground, or {@code false} if it is
     * running in the background
     */
    public boolean isInForeground() {
        return foregroundActivityClass != null;
    }

    /**
     * check if a specific {@link Activity} is in the foreground
     *
     * @param activityClass the {@link Class} of the {@link Activity} to check for
     * @return {@code true} if this {@link Activity} is currently in the foreground,
     * else {@code false}
     */
    public boolean isInForeground(Class<? extends Activity> activityClass) {
        return isInForeground() && foregroundActivityClass.equals(activityClass);
    }


    /**
     * get the {@link AppLoader} of this application
     *
     * @param <T> the sub-class of the {@link AppLoader} that relates to the application
     * @return the {@link AppLoader} already set through
     */
    @SuppressWarnings("unchecked")
    public <T extends AppLoader> T getAppLoader() {
        return (T) appLoader;
    }

    /**
     * get the starting time of the application
     *
     * @return a time-stamp for the starting time of the application
     */
    public long getStartTime() {
        return startTime;
    }

    public void setAppLoader(AppLoader appLoader) {
        this.appLoader = appLoader;
    }

    public void setIntegrationHandler(AbstractIntegrationHandler integrationHandler) {
        this.integrationHandler = integrationHandler;
    }


}
