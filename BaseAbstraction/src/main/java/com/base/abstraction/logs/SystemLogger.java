package com.base.abstraction.logs;

import android.util.Log;

import com.base.abstraction.system.App;

import java.util.HashMap;

/**
 * a Class responsible for logging using {@link System} class
 * <p/>
 * Created by Ahmed Adel on 8/31/2016.
 */
public class SystemLogger {

    private static final String TAG_HANDLED_EXCEPTION = "Handled Exception";
    private static final HashMap<String, SystemLogger> instances = new HashMap<>();
    private boolean enabled;


    private SystemLogger(String name, boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * get a {@link SystemLogger} instance that is mapped to a certain name
     *
     * @param name    the name of the logger (added as a prefix to logged data)
     * @param enabled weather logging is enabled or disabled
     * @return the single {@link SystemLogger} instance mapped to the passed name
     */
    public static SystemLogger getInstance(String name, boolean enabled) {
        SystemLogger instance;
        if (instances.containsKey(name)) {
            instance = instances.get(name);
        } else {
            instance = new SystemLogger(name, enabled);
            instances.put(name, instance);
        }
        return instance;
    }

    /**
     * get the default {@link SystemLogger}
     *
     * @param enabled weather logging is enabled or disabled
     * @return the single default {@link SystemLogger} instance
     */
    public static SystemLogger getInstance(boolean enabled) {
        return getInstance(null, enabled);
    }

    /**
     * get the default {@link SystemLogger} and set weather it is enabled or disabled
     * based on {@link App#isDebugging()}
     *
     * @return the single default {@link SystemLogger} instance
     */
    public static SystemLogger getInstance() {
        return getInstance(App.getInstance().isDebugging());
    }


    public void info(String tag, Object message) {
        if (enabled) {
            Log.i(tag, String.valueOf(message));
        }
    }

    public void info(Class tag, Object message) {
        if (enabled) {
            String tagFromClass = tag.getSimpleName();
            Log.i(tagFromClass, String.valueOf(message));
        }
    }

    public void error(String tag, Object message) {
        if (enabled) {
            Log.e(tag, String.valueOf(message));
        }
    }

    public void error(Class tag, Object message) {
        if (enabled) {
            String tagFromClass = tag.getSimpleName();
            Log.e(tagFromClass, String.valueOf(message));
        }
    }

    public void exception(Throwable exception) {
        if (enabled) {
            Log.e(TAG_HANDLED_EXCEPTION, String.valueOf(exception), exception);
        }
    }


}
