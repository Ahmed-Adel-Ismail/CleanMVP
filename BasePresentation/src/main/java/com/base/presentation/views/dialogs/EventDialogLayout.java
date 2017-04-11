package com.base.presentation.views.dialogs;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.base.abstraction.commands.Command;
import com.base.presentation.references.CheckedProperty;
import com.base.presentation.references.Property;
import com.base.presentation.base.abstracts.features.AbstractActivity;

/**
 * a Class that handles drawing custom layout on the {@link EventDialog}
 * <p>
 * Created by Ahmed Adel on 1/3/2017.
 */
public class EventDialogLayout {

    @LayoutRes
    private final long id;
    private Command<Params, Void> onInflate;

    public EventDialogLayout(int id) {
        this.id = id;
    }

    long getId() {
        return id;
    }

    Command<Params, Void> getOnInflate() {
        return onInflate;
    }

    /**
     * set the {@link Command} that will be executed after the dialog inflates this custom layout
     * in it's {@link android.app.AlertDialog#setView(View)}
     *
     * @param onInflate the {@link Command}, it's Activity's
     *                  {@link CheckedProperty#set(Object)} will be set by the dialog itself,
     *                  no need to set it
     * @return {@code this} instance for chaining
     */
    public EventDialogLayout onInflate(Command<Params, Void> onInflate) {
        this.onInflate = onInflate;
        return this;
    }

    public static class Params {
        public final CheckedProperty<AbstractActivity> activity = new CheckedProperty<>();
        public final CheckedProperty<View> dialogView = new CheckedProperty<>();
        public final Property<EventDialogBuilder> dialogBuilder = new Property<>();
    }
}
