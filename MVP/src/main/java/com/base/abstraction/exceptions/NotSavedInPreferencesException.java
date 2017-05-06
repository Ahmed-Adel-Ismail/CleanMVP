package com.base.abstraction.exceptions;

import com.base.abstraction.exceptions.base.AbstractException;
import com.base.abstraction.system.AppResources;

/**
 * an exception thrown if loading the value from preferences failed
 * <p>
 * Created by Ahmed Adel on 10/26/2016.
 */
public class NotSavedInPreferencesException extends AbstractException {

    public NotSavedInPreferencesException(int keyStringResource) {
        super("no value found for the key : " + AppResources.string(keyStringResource));
    }
}
