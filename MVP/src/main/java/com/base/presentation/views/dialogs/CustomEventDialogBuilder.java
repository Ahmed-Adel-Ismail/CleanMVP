package com.base.presentation.views.dialogs;

/**
 * Created by Wafaa on 1/5/2017.
 */

public class CustomEventDialogBuilder extends EventDialogBuilder {

    private String titleText;
    private String messageText;

    public CustomEventDialogBuilder(int dialogId) {
        super(dialogId);
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
