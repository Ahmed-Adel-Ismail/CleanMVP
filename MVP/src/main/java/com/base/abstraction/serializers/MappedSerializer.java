package com.base.abstraction.serializers;

/**
 * the parent class for any class that will need to serialize
 * Objects to any source that takes a key and a value (like preferences)
 * <p>
 * Created by Ahmed Adel on 10/25/2016.
 */
@SuppressWarnings("WeakerAccess")
class MappedSerializer {

    /**
     * this value should not be saved to preferences, it should be used to validate
     * {@code String} default values only
     */
    protected static final String NO_VALUE = "@&_(%$##r@";

    /**
     * the {@code String recourse key} .. all keys in our apps should be stored in resource files
     */
    protected final int keyResourceId;

    MappedSerializer(int keyResourceId) {
        this.keyResourceId = keyResourceId;
    }
}
