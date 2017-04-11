package com.base.presentation.requests;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.base.abstraction.system.AppResources;
import com.base.presentation.R;
import com.base.presentation.base.abstracts.features.AbstractActivity;

import java.io.Serializable;

/**
 * A Class that represents a JsonRequest to do an action related to context, like
 * starting or finishing an Activity, or starting a service ... used with
 * {@link AbstractActivity}
 * <p/>
 * Created by Ahmed Adel on 10/3/2016.
 */
public class ActivityActionRequest implements Serializable {

    private static final int NO_CODE = -99;
    @NonNull
    private final ActionType actionType;
    private String actionString;
    private Class<?> actionClass;
    private Intent data;
    private int code = NO_CODE;


    public ActivityActionRequest(@NonNull ActionType actionType) {
        this.actionType = actionType;
    }

    /**
     * set the Action of the this {@link ActivityActionRequest} ... this is used to start a native
     * action like Camera or Media
     *
     * @param action action the action to start, like {@link MediaStore#ACTION_IMAGE_CAPTURE}
     *               for example
     * @return {@code this} instance for chaining
     */
    public ActivityActionRequest action(String action) {
        this.actionString = action;
        this.actionClass = null;
        return this;
    }

    /**
     * set the Action of the this {@link ActivityActionRequest} ... this is used to start an
     * Activity or service, or any similar actions
     *
     * @param action the {@link Class} of the target Activity or Service or similar components
     * @return {@code this} instance for chaining
     */
    public ActivityActionRequest action(Class<?> action) {
        this.actionClass = action;
        this.actionString = null;
        return this;
    }


    /**
     * add an extra to the {@link Intent} that can be accessed later through {@link #getData()}
     *
     * @param keyStringResource the string resource that will be the key of this extra
     * @param object            the {@link Serializable} object
     * @return {@code this} instance for chaining
     */
    public ActivityActionRequest extra(int keyStringResource, Serializable object) {
        data = createDataIfNull();
        data.putExtra(AppResources.string(keyStringResource), object);
        return this;
    }

    /**
     * add a {@link Bundle} to the {@link Intent} that can be accessed later through {@link #getData()}
     *
     * @param extras the {@link Bundle} to be passed to {@link Intent#putExtras(Bundle)}
     * @return {@code this} instance for chaining
     */
    public ActivityActionRequest extras(Bundle extras) {
        data = createDataIfNull();
        data.putExtras(extras);
        return this;
    }


    /**
     * add a {@link Uri} to the {@link Intent} that can be accessed later through {@link #getData()}
     *
     * @param uri the {@link Uri} to be passed to {@link Intent#setData(Uri)}
     * @return {@code this} instance for chaining
     */
    public ActivityActionRequest addData(Uri uri) {
        data = createDataIfNull();
        data.setData(uri);
        return this;
    }

    /**
     * add flags to the {@link Intent} that can be accessed later through {@link #getData()}
     *
     * @param flags the flags passed to {@link Intent#addFlags(int)}
     * @return {@code this} instance for chaining
     */
    public ActivityActionRequest flags(int flags) {
        data = createDataIfNull();
        data.addFlags(flags);
        return this;
    }


    /**
     * set the request/result Code for this {@link ActivityActionRequest} to make the starting Activity
     * to wait for result, or to make the finishing activity set result with one of the known 3
     * codes {@link com.base.presentation.R.integer#resultCodeOk resultCodeOk},
     * {@link com.base.presentation.R.integer#resultCodeCanceled resultCodeCanceled}, and
     * {@link com.base.presentation.R.integer#resultCodeFirstUser resultCodeFirstUser} ...
     * for setting result codes, you can use {@link #codeOk()}, {@link #codeCancel()} and
     * {@link #codeFirstUser()}
     *
     * @param codeIntegerResource the {@code integer} resource that holds the request/response Code
     *                            in it's value
     * @return {@code this} instance for chaining
     */
    public ActivityActionRequest code(int codeIntegerResource) {
        this.code = AppResources.integer(codeIntegerResource);
        return this;
    }

    /**
     * same as {@link #code(int)} but with the code
     * {@link com.base.presentation.R.integer#resultCodeOk}
     *
     * @return {@code this} instance for chaining
     */
    public ActivityActionRequest codeOk() {
        code(R.integer.resultCodeOk);
        return this;
    }

    /**
     * same as {@link #code(int)} but with the code
     * {@link com.base.presentation.R.integer#resultCodeCanceled}
     *
     * @return {@code this} instance for chaining
     */
    public ActivityActionRequest codeCancel() {
        code(R.integer.resultCodeCanceled);
        return this;
    }

    /**
     * same as {@link #code(int)} but with the code
     * {@link com.base.presentation.R.integer#resultCodeFirstUser}
     *
     * @return {@code this} instance for chaining
     */
    public ActivityActionRequest codeFirstUser() {
        code(R.integer.resultCodeFirstUser);
        return this;
    }


    public String getActionString() {
        return actionString;
    }

    public Class<?> getActionClass() {
        return actionClass;
    }

    public Intent getData() {
        return data;
    }

    /**
     * get the Object stored for the given key, and casted to the expected type, or
     * {@code null} if no such key (or if no {@link Intent} was available
     *
     * @param key the {@code String} resource that is the key for the stored value
     * @param <T> the epected {@link Serializable} type
     * @return the Object if found, or {@code null}
     * @throws ClassCastException if the expected type did not match the value stored
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getExtra(@StringRes int key) throws ClassCastException {
        if (data != null) {
            return (T) data.getSerializableExtra(AppResources.string(key));
        } else {
            return null;
        }

    }

    public int getCode() {
        return code;
    }

    public boolean hasCode() {
        return code != NO_CODE;
    }

    @NonNull
    public ActionType getActionType() {
        return actionType;
    }

    private Intent createDataIfNull() {
        if (data == null) {
            data = new Intent();
        }
        return data;
    }

}
