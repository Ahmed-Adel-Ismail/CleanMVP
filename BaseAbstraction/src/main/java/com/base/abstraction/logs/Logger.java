package com.base.abstraction.logs;

import android.util.Log;

import com.base.abstraction.system.App;

import java.util.HashMap;

/**
 * a Class responsible for logging using {@link System} class
 * <p/>
 * Created by Ahmed Adel on 8/31/2016.
 */
public class Logger {

    private static final String TAG_HANDLED_EXCEPTION = "Handled Exception";
    private static final HashMap<String, Logger> instances = new HashMap<>();
    private boolean enabled;


    private Logger(String name, boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * get a {@link Logger} instance that is mapped to a certain name
     *
     * @param name    the name of the logger (added as a prefix to logged data)
     * @param enabled weather logging is enabled or disabled
     * @return the single {@link Logger} instance mapped to the passed name
     */
    public static Logger getInstance(String name, boolean enabled) {
        Logger instance;
        if (instances.containsKey(name)) {
            instance = instances.get(name);
        } else {
            instance = new Logger(name, enabled);
            instances.put(name, instance);
        }
        return instance;
    }

    /**
     * get the default {@link Logger}
     *
     * @param enabled weather logging is enabled or disabled
     * @return the single default {@link Logger} instance
     */
    public static Logger getInstance(boolean enabled) {
        return getInstance(null, enabled);
    }

    /**
     * get the default {@link Logger} and set weather it is enabled or disabled
     * based on {@link App#isDebugging()}
     *
     * @return the single default {@link Logger} instance
     */
    public static Logger getInstance() {
        return getInstance(App.getInstance().isDebugging());
    }


    public void info(String tag, Object message) {
        if (enabled) {
            Log.i(tag, getLogMessage(message));
        }
    }

    public void info(Class tag, Object message) {
        if (enabled) {
            String tagFromClass = tag.getSimpleName();
            Log.i(tagFromClass, getLogMessage(message));
        }
    }

    public void error(String tag, Object message) {
        if (enabled) {
            Log.e(tag, getLogMessage(message));
        }
    }

    public void error(Class tag, Object message) {
        if (enabled) {
            String tagFromClass = tag.getSimpleName();
            Log.e(tagFromClass, getLogMessage(message));
        }
    }

    public void exception(Throwable exception) {
        if (enabled) {
            Log.e(TAG_HANDLED_EXCEPTION, exception.getMessage(), exception);
        }
    }

    private String getLogMessage(Object message) {
        return String.valueOf(message);
    }


}
