package com.base.presentation.base.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.base.abstraction.actors.base.Actor;
import com.base.abstraction.messaging.AbstractMailbox;
import com.base.abstraction.messaging.ConcurrentMailbox;
import com.base.abstraction.messaging.Mailbox;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.abstraction.annotations.readers.ClassAnnotationReader;
import com.base.abstraction.annotations.scanners.ClassAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.commands.executors.generators.ExecutableCommandsGenerator;
import com.base.abstraction.commands.executors.generators.ExecutableGenerator;
import com.base.abstraction.commands.executors.generators.ExecutorGenerator;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.exceptions.GenericTypeInitializerException;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.observer.Observer;
import com.base.presentation.references.FieldsCleaner;
import com.base.abstraction.reflections.GenericTypeInitializer;
import com.base.abstraction.reflections.Initializer;
import com.base.abstraction.system.App;
import com.base.abstraction.R;
import com.base.presentation.annotations.interfaces.Foreground;
import com.base.presentation.annotations.interfaces.MainThread;
import com.base.presentation.base.abstracts.system.SystemAccessor;
import com.base.presentation.interfaces.DestroyableClient;
import com.base.presentation.interfaces.IntentHolder;
import com.base.presentation.models.Model;
import com.base.presentation.system.SystemServices;

/**
 * The base class for any {@link Service}, when {@link #onStartCommand(Intent, int, int)} is invoked
 * for the first time, {@link R.id#onServiceStarted} {@link Event} will be triggered, when a call
 * to start service is invoked again and this method is triggered again,
 * {@link R.id#onServiceStartedAgain} will be triggered
 * <p>
 * the {@link Mailbox} of this {@link Service} runs in a background thread, if you want it to run
 * on the main thread, you need to annotate this class with {@link MainThread}
 * <p>
 * <u>mandatory annotations :</u><br>
 * {@link Address} : the Actor address of this Service<br>
 * <p>
 * <u>optional annotations :</u><br>
 * {@link MainThread} : let this Service's {@link Mailbox} run on the main thread rather than
 * running in background<br>
 * {@link Foreground} : make this service call {@link #startForeground(int, Notification)} to
 * not to be killed easily by the OS, this will
 * let this Service keep the CPU on while it is running through acquiring a
 * {@link android.os.PowerManager.WakeLock}, and it will handle calling
 * {@link PowerManager.WakeLock#release()} as well<br>
 * <p>
 * Created by Ahmed Adel on 1/16/2017.
 *
 * @see Address
 * @see MainThread
 * @see Foreground
 */
public class AbstractService<T extends Model> extends Service implements
        Clearable,
        SystemAccessor,
        DestroyableClient,
        Observer,
        Actor,
        IntentHolder {

    private static final int MODEL_GENERIC_ARG_INDEX = 0;

    private Executor<Message> executor;
    private T model;
    private SystemServices systemServices;
    private AbstractMailbox<Event> mailbox;
    private PowerManager.WakeLock wakeLock;

    private boolean startedAsForegroundService;
    private long address;
    private boolean onStartCommandInvoked;
    private boolean destroyed;
    private Intent intent;

    public AbstractService() {
        this.executor = new Executor<>();
        this.executor = new ExecutableGenerator<>(executor).execute(this);
        this.executor = new ExecutableCommandsGenerator<>(executor).execute(this);
        this.executor = new ExecutorGenerator<>(executor).execute(this);
        this.model = createModelFromGenericType();
        this.systemServices = new SystemServices(this);
        this.address = readAddressAnnotations();
        initializeMailbox();
        App.getInstance().getActorSystem().add(this);
    }

    private void initializeMailbox() {
        Class<?> klass = getClass();
        if (klass.isAnnotationPresent(MainThread.class)) {
            mailbox = new ConcurrentMailbox<>(createMailboxExecutor(), Looper.getMainLooper());
        } else {
            mailbox = new ConcurrentMailbox<>(createMailboxExecutor());
        }
    }


    protected T createModelFromGenericType() {
        T model = null;
        try {
            model = new GenericTypeInitializer<T>(MODEL_GENERIC_ARG_INDEX).execute(getClass());
            model.initialize(this);
        } catch (GenericTypeInitializerException e) {
            Logger.getInstance().exception(e);
        }
        return model;
    }

    private long readAddressAnnotations() {
        long address = 0;
        ClassAnnotationReader<Address> reader;
        reader = new ClassAnnotationReader<>(Address.class);
        try {
            address = reader.execute(this).value();
        } catch (AnnotationNotDeclaredException e) {
            new TestException().execute(e);
        }
        return address;
    }

    @NonNull
    private Command<Event, Void> createMailboxExecutor() {
        return new Command<Event, Void>() {
            @Override
            public Void execute(Event event) {
                if (executor != null) {
                    executor.execute(event.getId(), event.getMessage());
                }
                return null;
            }
        };
    }

    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {

        if (this.intent == null) {
            this.intent = intent;
        }

        if (!startedAsForegroundService) {
            readForegroundAnnotation();
        }

        ServiceStartCommandParams p = new ServiceStartCommandParams(this);
        p.setIntent(intent);
        p.setFlags(flags);
        p.setStartId(startId);

        long eventId = (onStartCommandInvoked) ? R.id.onServiceStartedAgain : R.id.onServiceStarted;
        Message message = new Message.Builder().id(startId).content(p).build();
        onUpdate(new Event.Builder(eventId).message(message).senderActorAddress(address).build());

        if (!onStartCommandInvoked) {
            onStartCommandInvoked = true;
        }

        return START_STICKY;
    }

    private void readForegroundAnnotation() {
        new ClassAnnotationScanner<Foreground>(Foreground.class) {
            @Override
            protected void onAnnotationFound(Foreground annotation) {
                startAsForegroundService(annotation);
            }
        }.execute(this);
    }

    private void startAsForegroundService(Foreground annotation) {
        startedAsForegroundService = true;
        acquireNewWakeLock();
        Class<? extends ForegroundServiceBuilder> klass = annotation.value();
        if (NullForegroundServiceBuilder.class.equals(klass)) {
            startForegroundWithoutNotification();
        } else {
            startForegroundWithNotification(klass);
        }
    }

    private void acquireNewWakeLock() {
        this.wakeLock = systemServices.getNewWakeLock(getClass().getSimpleName());
        this.wakeLock.acquire();
    }


    private void startForegroundWithoutNotification() {
        Intent intent = new Intent(this, FakeService.class);
        startService(intent);
        Notification notification = new NotificationCompat.Builder(this).build();
        startForeground(FakeService.NOTIFICATION_ID, notification);

    }

    private void startForegroundWithNotification(Class<? extends ForegroundServiceBuilder> klass) {
        ForegroundServiceBuilder builder;
        builder = new Initializer<ForegroundServiceBuilder>().execute(klass);
        startForeground((int) builder.getId(), builder.execute(this));
    }


    @Override
    public final void onUpdate(Event event) throws RuntimeException {
        if (mailbox != null) {
            mailbox.execute(event);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public long getActorAddress() {
        return address;
    }

    @Override
    public AbstractMailbox<Event> getMailbox() {
        return mailbox;
    }


    @Override
    public SystemServices getSystemServices() {
        return systemServices;
    }

    protected T getModel() {
        return model;
    }

    @Override
    public Intent getIntent() {
        return intent;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();
        App.getInstance().getActorSystem().remove(this);
        if (startedAsForegroundService) {
            stopForeground(true);
        }

        destroyed = true;
        executor.clear();
        if (model != null) {
            model.clear();
            model = null;
        }

        if (mailbox != null) {
            mailbox.clear();
            mailbox = null;
        }

        if (systemServices != null) {
            systemServices.clear();
            systemServices = null;
        }

        if (wakeLock != null) {
            wakeLock.release();
        }

        new FieldsCleaner(true).execute(this);
    }

    /**
     * in {@link AbstractService}, this method causes the service to call {@link #stopSelf()} and
     * do what is necessary for stopping (if required)
     */
    @Override
    public void clear() {
        stopSelf();
    }


}
