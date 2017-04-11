package com.base.presentation.views.dialogs;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;

import com.base.abstraction.system.AppResources;

/**
 * A class that is used to build {@link EventDialog}.
 * <br>
 * if you want the dialog to have a positive button, you have to invoke
 * {@link #setPositiveText(int)}.
 * <br>
 * if you want the dialog to have a negative button, you have to invoke
 * {@link #setNegativeText(int)}.
 * <br>
 * if you want the dialog to have a neutral button, you have to invoke
 * {@link #setNeutralText(int)}.
 * <p/>
 * Created by Wafaa on 9/29/2016.
 */
public class EventDialogBuilder {

    private int dialogId;
    private Object tag;
    private String title;
    private String message;
    private String positiveText;
    private String negativeText;
    private String neutralText;
    private EventDialogLayout layout;

    public EventDialogBuilder(int dialogId) {
        this.dialogId = dialogId;
    }

    public void setTitle(@StringRes int titleResourceId) {
        this.title = AppResources.string(titleResourceId);
    }

    public void setNeutralText(@StringRes int neutralTextResourceId) {
        this.neutralText = AppResources.string(neutralTextResourceId);
    }

    public void setDialogId(@IdRes int dialogId) {
        this.dialogId = dialogId;
    }

    public void setMessage(@StringRes int messageResourceId) {
        this.message = AppResources.string(messageResourceId);
    }

    public void setPositiveText(@StringRes int positiveTextResourceId) {
        this.positiveText = AppResources.string(positiveTextResourceId);
    }

    public void setNegativeText(@StringRes int negativeTextResourceId) {
        this.negativeText = AppResources.string(negativeTextResourceId);
    }

    public String getTitle() {
        return title;
    }

    String getNeutralText() {
        return neutralText;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNegativeText(String negativeText) {
        this.negativeText = negativeText;
    }

    public void setNeutralText(String neutralText) {
        this.neutralText = neutralText;
    }

    public void setPositiveText(String positiveText) {
        this.positiveText = positiveText;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getTag() {
        return tag;
    }

    /**
     * set an optional Tag Object to be available when a click is processed
     *
     * @param tag an Object to be available when clicks are done
     */
    public void setTag(Object tag) {
        this.tag = tag;
    }

    int getDialogId() {
        return dialogId;
    }

    public String getMessage() {
        return message;
    }

    public String getPositiveText() {
        return positiveText;
    }

    public String getNegativeText() {
        return negativeText;
    }

    EventDialogLayout getLayout() {
        return layout;
    }

    public void setLayout(EventDialogLayout customLayout) {
        this.layout = customLayout;
    }

    boolean hasPositiveTextResourceId() {
        return positiveText != null;
    }

    boolean hasNegativeTextResourceId() {
        return negativeText != null;
    }

    boolean hasNeutralTextResourceId() {
        return neutralText != null;
    }

}
