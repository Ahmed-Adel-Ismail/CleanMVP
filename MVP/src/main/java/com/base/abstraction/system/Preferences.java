package com.base.abstraction.system;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.base.abstraction.logs.Logger;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * This class is responsible for saving/loading data from preferences files, this file is cloned
 * from an old implementation of mine ... bad code, performance overheads, but does the job for now
 * <p>
 * Created by Ahmed Adel on 10/25/2016.
 */
@SuppressWarnings("ALL")
public class Preferences {

    private static final String COLLECTION_SEPARATOR = "@_@";
    private static final String MAP_KEY = "||KEY||";
    private static final String MAP_VALUE = "||VALUE||";
    private static final String LOAD_PREFIX = "loadAnnotatedElements";
    private static final String SAVE_PREFIX = "save";
    private static final String INVALID_TYPE_MESSAGE = "Type is invalid, can not be used in Preferences";
    private static final String TYPE_NOT_DETECTED = "Type not Detected, returning null";
    private static final String DIFF_TYPE_MESSAGE = "Saved Value in Preference is from a different type";
    private static final String NOT_STRING_VALUE = "passed default value as null, and it is not a String";
    private static final String NOT_BOOLEAN_VALUE = "passed default value as null, and it is not a Boolean";
    private static final String NOT_INTEGER_VALUE = "passed default value as null, and it is not a Integer";
    private static final String NOT_LONG_VALUE = "passed default value as null, and it is not a Long";
    private static final String NOT_FLOAT_VALUE = "passed default value as null, and it is not a Float";
    private static Map<String, Preferences> instances = null;
    private SharedPreferences preferenceFile = null;
    private String preferenceFileName = null;

    private Preferences(String preferencesFileName) {
        this.preferenceFileName = String.valueOf(preferencesFileName);
        this.preferenceFile = getSharedPreferences(preferencesFileName);
    }

    /**
     * get the single instance of the {@link Preferences} class, and supply the
     * {@link SharedPreferences} file name to handle
     *
     * @param preferencesFileName the name of the {@link SharedPreferences} to manage, or pass
     *                            {@code null} to get the instance handling the default
     *                            preferences file
     * @return the instance of the {@link Preferences} class
     */
    public static Preferences getInstance(String preferencesFileName) {
        Preferences instance = null;
        if (instances == null) {
            synchronized (Preferences.class) {
                if (instances == null) {
                    instances = new LinkedHashMap<String, Preferences>();
                }
            }
        }
        if (!instances.containsKey(preferencesFileName)) {
            synchronized (Preferences.class) {
                if (!instances.containsKey(preferencesFileName)) {
                    instance = new Preferences(preferencesFileName);
                    instances.put(preferencesFileName, instance);
                } else {
                    instance = instances.get(preferencesFileName);
                }
            }
        } else {
            instance = instances.get(preferencesFileName);
        }
        return instance;
    }

    /**
     * get the instance of the {@link Preferences} class that handles the
     * default {@link SharedPreferences} file
     *
     * @return the default instance of this class
     */
    public static Preferences getInstance() {
        return getInstance(null);
    }


    /**
     * get a preference value then remove it
     *
     * @param keyStringResource the string resource key to search for
     * @param defaultValue      the value to be returned if no such key available
     * @return the value saved for this key, then removes this key from
     * preferences
     */
    public synchronized <T> T pop(int keyStringResource, T defaultValue) {
        T t = load(keyStringResource, defaultValue);
        remove(keyStringResource);
        return t;
    }

    /**
     * loadAnnotatedElements the value saved in Preferences and saves the new one instead, if the
     * expected return type is different from the actual saved type (which will
     * cause <b>ClassCastException</b>), the Exception will be handled, and the
     * preference will be converted to the new type
     * <p/>
     * if there was no key saved for this value, it will be created with the
     * <b>defaultValue</b>
     *
     * @param keyStringResource the resource {@code int) value of the String Key for the saved value
     * @param newValue          the default value to be returned if it is not found or if an
     *                          error occurred ... and the one which will be saved after
     *                          loading the current value
     * @return the saved value, or the defaultValue if an error occurred, and
     * saves the new Value in all cases
     */
    public <T> T poll(int keyStringResource, T newValue) {
        String savedKey = AppResources.string(keyStringResource);
        T result = newValue;
        if (getSharedPreferences().contains(savedKey)) {
            result = load(keyStringResource, newValue);
        }
        save(keyStringResource, newValue);
        return result;
    }

    /**
     * loadAnnotatedElements the value saved in Preferences, if the expected return type is
     * different from the actual saved type (which will cause
     * {@link ClassCastException}), the new <b>defaultValue</b> will overwrite
     * the existing one with the new type
     *
     * @param keyStringResource the resource {@code int) value of the String Key for the saved value
     * @param defaultValue      the default value to be returned if it is not found or if an
     *                          error occurred
     * @return the saved value, or the defaultValue if an error occurred
     */
    public <T> T load(int keyStringResource, T defaultValue) {
        String savedKey = AppResources.string(keyStringResource);
        return load(savedKey, defaultValue);
    }

    /**
     * loadAnnotatedElements the value saved in Preferences, if the expected return type is
     * different from the actual saved type (which will cause
     * <b>ClassCastException</b>), the new <b>defaultValue</b> will overwrite
     * the existing one with the new type
     *
     * @param keyStringResource the String Key for the saved value
     * @param defaultValue      the default value to be returned if it is not found or if an
     *                          error occurred
     * @return the saved value, or the defaultValue if an error occurred
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T> T load(String keyStringResource, T defaultValue) {

        T value = defaultValue;

        try {
            if (isBoolean(defaultValue)) {
                Boolean b = getBoolean(keyStringResource, (Boolean) defaultValue);
                value = (T) b;
                b = null;
            } else if (isIntOrLess(defaultValue)) {
                Integer i = getInteger(keyStringResource, (Integer) defaultValue);

                if (isInteger(defaultValue)) {
                    value = (T) i;
                    i = null;
                } else {
                    String s = String.valueOf(i);
                    if (isShort(defaultValue)) {
                        Short sh = Short.valueOf(s);
                        value = (T) sh;
                        sh = null;
                    } else if (isByte(defaultValue)) {
                        Byte b = Byte.valueOf(s);
                        value = (T) b;
                        b = null;
                    }
                    s = null;

                }
            } else if (isLong(defaultValue)) {
                Long l = getLong(keyStringResource, (Long) defaultValue);
                value = (T) l;
                l = null;
            } else if (isString(defaultValue)) {
                String s = getString(keyStringResource, String.valueOf(defaultValue));
                value = (T) s;
                s = null;
            } else if (isFloat(defaultValue)) {
                Float f = getFloat(keyStringResource, (Float) defaultValue);
                value = (T) f;
                f = null;
            } else if (isNull(defaultValue)) {
                try {
                    value = (T) loadObject(keyStringResource);
                } catch (Exception e) {
                    Logger.getInstance().error(Preferences.class,
                            LOAD_PREFIX + "(" + keyStringResource + ") : " + TYPE_NOT_DETECTED);
                }
                return value;
            } else if (isCollection(defaultValue)) {
                value = (T) loadCollection(keyStringResource, (Collection) defaultValue);
            } else if (isMap(defaultValue)) {
                value = (T) loadMap(keyStringResource, (Map) defaultValue);
            } else {
                Logger.getInstance().error(Preferences.class, LOAD_PREFIX + "(" + keyStringResource + ") : "
                        + INVALID_TYPE_MESSAGE);
                return value;
            }
        } catch (ClassCastException ex) {
            save(keyStringResource, value);
            Logger.getInstance().error(Preferences.class,
                    LOAD_PREFIX + "(" + keyStringResource + ") : " + DIFF_TYPE_MESSAGE);
        }

        return value;
    }

    /**
     * save the given value in preferences, if it is not supported type .. nothing
     * will happen, if you passed {@code null}, it will be saved as
     * {@link String}
     *
     * @param keyStringResource the String Key for the saved value
     * @param value             the value to be saved if possible
     * @return true if it is saved, or false if not
     */
    public <T> boolean save(int keyStringResource, T value) {
        String savedKey = AppResources.string(keyStringResource);
        return save(savedKey, value);
    }


    /**
     * save the given value in preferences, if it is not supported type, nothing
     * will happen, if you passed {@code null}, it will be saved as
     * {@link String}
     *
     * @param keyStringResource the String Key for the saved value
     * @param value             the value to be saved if possible
     * @return true if it is saved, or false if not
     */
    @SuppressWarnings("rawtypes")
    private <T> boolean save(String keyStringResource, T value) {
        Context context = App.getInstance();
        boolean saved = false;
        try {
            saved = true;
            if (isBoolean(value)) {
                saveBoolean(keyStringResource, (Boolean) value);
            } else if (isIntOrLess(value)) {
                String s = String.valueOf(value);
                Integer i = Integer.valueOf(s);
                saveInteger(keyStringResource, i);
                s = null;
                i = null;
            } else if (isLong(value)) {
                saveLong(keyStringResource, (Long) value);
            } else if (isStringOrNull(value)) {
                saveString(context, keyStringResource, String.valueOf(value));
            } else if (isFloat(value)) {
                saveFloat(keyStringResource, (Float) value);
            } else if (isCollection(value)) {
                saved = saveCollection(keyStringResource, (Collection) value);
            } else if (isMap(value)) {
                saved = saveMap(keyStringResource, (Map) value);
            } else {
                saved = false;
            }

        } catch (Throwable ex) {
            Logger.getInstance().error(Preferences.class, ex.toString());
            saved = false;
        }

        if (!saved) {
            Logger.getInstance().error(Preferences.class,
                    SAVE_PREFIX + "(" + keyStringResource + ") : " + INVALID_TYPE_MESSAGE);
        }

        return saved;
    }

    /**
     * remove Keys from {@link SharedPreferences}
     *
     * @param keyStringResources the keys to be removed ... it should be a {@code int} keys
     *                           representing String resource ids
     * @return true if <b>ALL</b> keys are removed, if one key could not be
     * removed, it continues to remove the rest, but will return false
     */
    public boolean remove(int... keyStringResources) {
        if (keyStringResources == null || keyStringResources.length == 0) {
            return false;
        }
        String[] keys = new String[keyStringResources.length];
        Resources res = AppResources.getResources();
        int i = 0;
        for (int id : keyStringResources) {
            keys[i++] = res.getString(id);
        }
        return remove(keys);
    }

    /**
     * remove Keys from {@link SharedPreferences}
     *
     * @param keys the keys to be removed ... it should be a {@code String} keys,
     *             if not, it's {@link Object#toString() toString()} will be
     *             invoked to get the key
     * @return true if <b>ALL</b> keys are removed, if one key could not be
     * removed, it continues to remove the rest, but will return false
     */
    private boolean remove(String... keys) {
        SharedPreferences preferences = getSharedPreferences();
        Editor editor = preferences.edit();

        boolean complete = false;
        boolean exception = false;
        String key = null;
        for (Object o : keys) {
            key = o.toString();
            try {
                editor.remove(key);
            } catch (Throwable ex) {
                Logger.getInstance().error(Preferences.class, ex.toString());
                exception = true;
            }
        }
        key = null;
        editor.apply();
        complete = true;
        return (complete && !exception);
    }

    /**
     * remove All Keys from {@link SharedPreferences} file
     *
     * @return {@code true} if <b>ALL</b> keys are removed, if one key could not be
     * removed, it continues to remove the rest, but will return false
     */
    public boolean removeAll() {
        SharedPreferences preferences = getSharedPreferences();
        Set<String> keys = preferences.getAll().keySet();
        if (keys.size() == 0) {
            return true;
        }
        return remove(keys.toArray(new String[0]));
    }

    /**
     * causes a saved boolean value to be inversed, if it is <b>true</b>, it
     * will become <b>false</b>
     *
     * @param keyStringResource the key of the boolean value
     * @param defaultValue      the default value if this value was not found in the
     *                          Preferences
     * @return the result of the {@link Preferences#save(String, Object) save()}
     * method
     */
    public boolean inverseBoolean(int keyStringResource, boolean defaultValue) {
        boolean value = load(keyStringResource, defaultValue);
        return save(keyStringResource, !value);
    }

    /**
     * clear all available instances for {@link Preferences} class
     */
    public static void finish() {
        if (instances != null) {
            for (Preferences p : instances.values()) {
                p.preferenceFile = null;
            }
            instances.clear();
        }
        instances = null;

    }

    /**
     * get the given preference id from the {@link PreferenceActivity}, notice
     * that this method makes implicit casting to the desired type, so it may
     * through {@link ClassCastException} if it was assigned to a different type
     * than the actual {@link Preference}
     *
     * @param screen            the {@link PreferenceActivity} that is on the screen
     * @param keyStringResource the id of this {@link Preference} in the {@code strings.xml}
     *                          file
     * @return the desired {@link Preference} subclass casted to the assigned
     * type
     */
    @SuppressWarnings({"unchecked", "deprecation"})
    public static <T extends Preference> T find(PreferenceActivity screen, int keyStringResource) {
        Preference p = screen.findPreference(AppResources.string(keyStringResource));
        return (T) p;
    }

    /**
     * check if the current key is saved in preferences or not
     *
     * @param keyResource the resource id for the {@code String} key to look for
     * @return {@code true} if it exists, else {@code false}
     */
    public boolean hasKey(int keyResource) {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.contains(AppResources.string(keyResource));
    }

    // implementation

    /**
     * save given {@code String} into default preferences
     *
     * @deprecated use {@link #save(int, Object)} or
     * {@link #save(String, Object)} instead
     */
    private void saveString(Context context, String key, String value) {
        SharedPreferences preferences = getSharedPreferences();
        saveString(preferences, key, String.valueOf(value));
    }

    /**
     * save {@code String} into given preferences
     *
     * @deprecated use {@link #save(int, Object)} or
     * {@link #save(String, Object)} instead
     */
    @Deprecated
    private void saveString(SharedPreferences preferences, String key,
                            String value) {
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * loadAnnotatedElements {@code String} of given key from default preferences
     *
     * @deprecated use {@link #load(int, Object)} or
     * {@link #load(String, Object)} instead
     */
    private String getString(String key, String defaultVal) {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getString(key, defaultVal);
    }

    /**
     * save {@code int} into default preferences
     *
     * @deprecated use {@link #save(int, Object)} or
     * {@link #save(String, Object)} instead
     */
    private void saveInteger(String key, Integer value) {
        SharedPreferences preferences = getSharedPreferences();
        saveInteger(preferences, key, value);
    }

    /**
     * save {@code int} into given preferences
     *
     * @deprecated use {@link #save(int, Object)} or
     * {@link #save(String, Object)} instead
     */
    private void saveInteger(SharedPreferences preferences, String key, Integer value) {
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * get {@code int} from default preferences
     *
     * @deprecated use {@link #load(int, Object)} or
     * {@link #load(String, Object)} instead
     */
    private Integer getInteger(String key, Integer defaultVal) {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getInt(key, defaultVal);
    }

    /**
     * save {@code long} value into preferences
     *
     * @deprecated use {@link #save(int, Object)} or
     * {@link #save(String, Object)} instead
     */
    private void saveLong(String key, Long value) {
        SharedPreferences preferences = getSharedPreferences();
        saveLong(preferences, key, value);
    }

    /**
     * save {@code long} into given preferences
     *
     * @deprecated use {@link #save(int, Object)} or
     * {@link #save(String, Object)} instead
     */
    private void saveLong(SharedPreferences preferences, String key, Long value) {
        Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * get {@code long} from default preferences
     *
     * @deprecated use {@link #load(int, Object)} or
     * {@link #load(String, Object)} instead
     */
    private Long getLong(String key, Long defaultVal) {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getLong(key, defaultVal);
    }

    /**
     * save {@code boolean} into default preferences
     *
     * @deprecated use {@link #save(int, Object)} or
     * {@link #save(String, Object)} instead
     */
    private void saveBoolean(String key, boolean value) {
        SharedPreferences preferences = getSharedPreferences();
        saveBoolean(preferences, key, value);
    }

    /**
     * save {@code boolean} into given preferences
     *
     * @deprecated use {@link #save(int, Object)} or
     * {@link #save(String, Object)} instead
     */
    private void saveBoolean(SharedPreferences preferences, String key, boolean value) {
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Get {@code boolean} from default preferences
     *
     * @deprecated use {@link #load(int, Object)} or
     * {@link #load(String, Object)} instead
     */
    private boolean getBoolean(String key, Boolean defaultVal) {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getBoolean(key, defaultVal);
    }

    private void saveFloat(String key, float value) {
        SharedPreferences preferences = getSharedPreferences();
        saveFloat(preferences, key, value);
    }

    /**
     * save {@code float} into given preferences
     *
     * @deprecated use {@link #save(int, Object)} or
     * {@link #save(String, Object)} instead
     */
    private void saveFloat(SharedPreferences preferences, String key, float value) {
        Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * Get {@code float} from default preferences
     *
     * @deprecated use {@link #load(int, Object)} or
     * {@link #load(String, Object)} instead
     */
    private float getFloat(String key, float defaultVal) {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getFloat(key, defaultVal);
    }

    private SharedPreferences getSharedPreferences() {
        return getSharedPreferences(null);
    }

    private SharedPreferences getSharedPreferences(String preferencesName) {
        SharedPreferences preferences = null;
        Context c = App.getInstance();
        if (preferencesName == null) {
            if (preferenceFile == null) {
                preferences = PreferenceManager.getDefaultSharedPreferences(c);
            } else {
                preferences = preferenceFile;
            }
        } else {
            preferences = c.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        }
        return preferences;
    }

    private boolean saveCollection(String key, Collection<?> collection) {
        String stringCollection = "";
        try {
            for (Object value : collection) {
                stringCollection += String.valueOf(value)
                        + COLLECTION_SEPARATOR;
            }
            return save(key, stringCollection);
        } catch (Throwable e) {
            Logger.getInstance().error(Preferences.class, e.toString());
            return false;
        }
    }

    @SuppressWarnings("rawtypes")
    private boolean saveMap(String key, Map value) {
        Collection keySet = value.keySet();
        Collection valueSet = value.values();
        String mapKeys = key + MAP_KEY;
        String mapValues = key + MAP_VALUE;
        boolean saved = false;
        saved = saveCollection(mapKeys, keySet);
        saved &= saveCollection(mapValues, valueSet);
        return saved;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map loadMap(String key, Map map) {
        if (map != null) {
            int size = 0;
            LinkedList keys = null;
            LinkedList values = null;
            String newKey = key + MAP_KEY;
            keys = (LinkedList) loadCollection(newKey, new LinkedList());
            if (keys != null && (size = keys.size()) > 0) {
                newKey = key + MAP_VALUE;
                values = (LinkedList) loadCollection(newKey, new LinkedList());
                map.clear();
                Object k = null;
                Object v = null;
                for (int i = 0; i < size; i++) {
                    k = keys.get(i);
                    try {
                        v = values.get(i);
                    } catch (Exception e) {
                        v = null;
                        Logger.getInstance().error(Preferences.class, e.toString());
                    }
                    map.put(k, v);
                }
                k = null;
                v = null;
            }
        }
        return map;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Collection loadCollection(String key, Collection collection) {

        String savedString = load(key, "");
        Object[] values = savedString.split(COLLECTION_SEPARATOR);

        if (values != null && values.length > 0) {
            if (!(values.length == 1 && values[0].equals(""))) {
                collection.clear();
                collection.addAll(Arrays.asList(values));
            }
        }
        return collection;
    }

    private boolean isString(Object o) {
        return o instanceof String;
    }

    private boolean isStringOrNull(Object o) {
        return o instanceof String || o == null;
    }

    private boolean isIntOrLess(Object o) {
        return isInteger(o) || isShort(o) || isByte(o);
    }

    private boolean isInteger(Object o) {
        return o instanceof Integer;
    }

    private boolean isShort(Object o) {
        return o instanceof Short;
    }

    private boolean isByte(Object o) {
        return o instanceof Byte;
    }

    private boolean isLong(Object o) {
        return o instanceof Long;
    }

    private boolean isFloat(Object o) {
        return o instanceof Float;
    }

    private boolean isBoolean(Object o) {
        return o instanceof Boolean;
    }

    private boolean isCollection(Object o) {
        return o instanceof Collection;
    }

    private boolean isMap(Object o) {
        return o instanceof Map;
    }

    private boolean isNull(Object o) {
        return o == null;
    }

    private Object loadObject(String key) {

        Context context = App.getInstance();
        Object value = null;

        do {
            try {
                String s = getString(key, null);
                value = s;
                break;
            } catch (Exception e) {
                Logger.getInstance().error(Preferences.class,
                        LOAD_PREFIX + "(" + key + ") : " + NOT_STRING_VALUE);
            }

            try {
                Boolean b = getBoolean(key, false);
                value = b;
                break;
            } catch (Exception e) {
                Logger.getInstance().error(Preferences.class,
                        LOAD_PREFIX + "(" + key + ") : " + NOT_BOOLEAN_VALUE);
            }

            try {
                Integer i = getInteger(key, 0);
                value = i;
                break;
            } catch (Exception e) {
                Logger.getInstance().error(Preferences.class,
                        LOAD_PREFIX + "(" + key + ") : " + NOT_INTEGER_VALUE);
            }

            try {
                Long l = getLong(key, 0L);
                value = l;
                break;
            } catch (Exception e) {
                Logger.getInstance().error(Preferences.class,
                        LOAD_PREFIX + "(" + key + ") : " + NOT_LONG_VALUE);
            }

            try {
                Float f = getFloat(key, 0F);
                value = f;
                break;
            } catch (Exception e) {
                Logger.getInstance().error(Preferences.class,
                        LOAD_PREFIX + "(" + key + ") : " + NOT_FLOAT_VALUE);
            }

        } while (false);
        return value;
    }

    @Override
    public int hashCode() {
        return preferenceFileName.length();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Preferences) {
            Preferences p = (Preferences) o;
            String s1 = p.preferenceFileName;
            String s2 = preferenceFileName;
            s1 = String.valueOf(s1);
            s2 = String.valueOf(s2);
            return s1.equals(s2);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(preferenceFileName);
    }

}