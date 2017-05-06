package com.base.presentation.notifications.builders;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.base.abstraction.commands.Command;
import com.base.abstraction.system.App;


/**
 * generate {@link PendingIntent} for a {@link Class} or a {@code String} action
 * <p>
 * Created by Wafaa on 11/22/2016.
 */
public class PendingIntentGenerator implements Command<Object, PendingIntent> {

    private final PendingIntentType type;
    private int flags = PendingIntent.FLAG_UPDATE_CURRENT;
    private Bundle bundle;
    private int requestCode;

    private PendingIntentGenerator(PendingIntentType type) {
        this.type = type;
    }

    public static PendingIntentGenerator forActivity() {
        return new PendingIntentGenerator(PendingIntentType.ACTIVITY);
    }

    public static PendingIntentGenerator forService() {
        return new PendingIntentGenerator(PendingIntentType.SERVICE);
    }

    public static PendingIntentGenerator forBroadcast() {
        return new PendingIntentGenerator(PendingIntentType.BROADCAST);
    }

    public PendingIntentGenerator flags(int flags) {
        this.flags = flags;
        return this;
    }

    public PendingIntentGenerator bundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public PendingIntentGenerator requestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public PendingIntentGenerator randomRequestCode() {
        this.requestCode = (int) (Math.random() * 5000);
        return this;
    }


    /**
     * @param classOrStringAction the {@link Class} to be opened by the {@link Intent}, or the
     *                            {@code String} action of the {@link Intent}
     */
    @Override
    public PendingIntent execute(Object classOrStringAction) {
        return type.generate(classOrStringAction, flags, bundle, requestCode);
    }


    private enum PendingIntentType {
        ACTIVITY {
            @Override
            PendingIntent generate(Object classOrStringAction, int flags, Bundle bundle, int requestCode) {
                Intent notificationIntent = createIntent(classOrStringAction);
                if (bundle != null) {
                    notificationIntent.putExtras(bundle);
                }
                return PendingIntent.getActivity(App.getInstance(), requestCode, notificationIntent, flags);
            }
        }, SERVICE {
            @Override
            PendingIntent generate(Object classOrStringAction, int flags, Bundle bundle, int requestCode) {
                Intent notificationIntent = createIntent(classOrStringAction);
                if (bundle != null) {
                    notificationIntent.putExtras(bundle);
                }
                return PendingIntent.getService(App.getInstance(), requestCode, notificationIntent, flags);
            }
        }, BROADCAST {
            @Override
            PendingIntent generate(Object classOrStringAction, int flags, Bundle bundle, int requestCode) {
                Intent notificationIntent = createIntent(classOrStringAction);
                if (bundle != null) {
                    notificationIntent.putExtras(bundle);
                }
                return PendingIntent.getBroadcast(App.getInstance(), requestCode, notificationIntent, flags);
            }
        };

        abstract PendingIntent generate(Object classOrStringAction, int flags, Bundle bundle, int requestCode);

        protected Intent createIntent(Object classOrStringAction) {
            if (classOrStringAction instanceof String) {
                return new Intent(String.valueOf(classOrStringAction));
            } else if (classOrStringAction instanceof Class) {
                return new Intent(App.getInstance(), (Class) classOrStringAction);
            } else {
                throw new UnsupportedOperationException("passed parameter should be either a " +
                        "Class or a String, one of those will construct the Intent to be used");
            }
        }

    }

}
