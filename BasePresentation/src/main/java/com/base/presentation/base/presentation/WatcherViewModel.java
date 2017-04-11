package com.base.presentation.base.presentation;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.base.abstraction.annotations.scanners.MethodAnnotationScanner;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.AppResources;
import com.base.presentation.annotations.interfaces.EditTextWatcher;
import com.base.presentation.annotations.interfaces.EditTextWatcherCommand;
import com.base.presentation.annotations.scanners.EditTextWatcherCommandScanner;
import com.base.presentation.annotations.scanners.EditTextWatcherScanner;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.listeners.TextWatcher;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * An {@link Executor} that is sub-class for {@link ViewModel},
 * it tends to add {@link TextWatcher TextWatchers}
 * to it's {@link EditText} views
 * <p>
 * to watch any edit text, you should add the annotation
 * {@link EditTextWatcherCommand} or {@link EditTextWatcher} to the method that will be
 * invoked when the {@link EditText} value changes
 * <p/>
 * Created by Ahmed Adel on 10/9/2016.
 *
 * @see EditTextWatcher
 * @see EditTextWatcherCommand
 */
public class WatcherViewModel extends ViewModel {


    private Executor<Editable> onTextChangedCommands;
    private Map<Long, String> editTextsValues;

    /**
     * @deprecated use {@link #WatcherViewModel()} the {@link #initialize(ViewBinder)} rather than
     * this constructor, if you invoked this constructor, do not call
     * {@link #initialize(ViewBinder)}
     */
    @Deprecated
    WatcherViewModel(ViewBinder viewBinder) {
        super(viewBinder);
    }

    public WatcherViewModel() {

    }

    @Override
    @CallSuper
    public void initialize(ViewBinder viewBinder) {
        super.initialize(viewBinder);
        onTextChangedCommands = new Executor<>();
        editTextsValues = new HashMap<>();
        onTextChangedCommands.putAll(createOnTextChangedCommands());
        addEditTextWatcherViewIds();
        addEditTextWatcherCommandViewIds();
        addAllViewsFromViewBinder();
    }

    private void initialize() {

    }

    private void addEditTextWatcherViewIds() {
        new MethodAnnotationScanner<EditTextWatcher>(EditTextWatcher.class) {

            @Override
            protected void onAnnotationFound(Method element, EditTextWatcher annotation) {
                if (annotation.value().length > 1) {
                    for (long viewId : annotation.value()) {
                        viewsIds.add(viewId);
                    }
                } else {
                    viewsIds.add(annotation.value()[0]);
                }

            }
        }.execute(this);
    }

    private void addEditTextWatcherCommandViewIds() {
        new MethodAnnotationScanner<EditTextWatcherCommand>(EditTextWatcherCommand.class) {

            @Override
            protected void onAnnotationFound(Method element, EditTextWatcherCommand annotation) {
                if (annotation.value().length > 1) {
                    for (long viewId : annotation.value()) {
                        viewsIds.add(viewId);
                    }
                } else {
                    viewsIds.add(annotation.value()[0]);
                }

            }
        }.execute(this);
    }


    /**
     * createNativeMethod the {@link CommandExecutor} to handle text changes for {@link EditText}
     * views, this is optional, since the {@link EditText} is handled by default ... no
     * need to store the values of {@link EditText} views since they are stored and
     * can be accessed through {@link #getEditTextValue(long)} and
     * {@link #setEditTextValue(long, String)}
     *
     * @return the {@link CommandExecutor} to be executed while text is changing
     * @deprecated use {@link EditTextWatcherCommand} and {@link EditTextWatcher}
     * instead of overriding this method
     */
    protected CommandExecutor<Long, Editable> createOnTextChangedCommands() {
        Executor<Editable> executor = new Executor<>();
        executor = new EditTextWatcherCommandScanner<>(executor).execute(this);
        executor = new EditTextWatcherScanner<>(executor).execute(this);
        return executor;
    }

    /**
     * adds a {@link View} to the initial {@link ViewModel}
     *
     * @param view the {@link View} to add
     */
    public final void addView(@NonNull View view) {
        long viewId = view.getId();
        if (view instanceof EditText) {
            addTextWatcher(viewId, (EditText) view);
        }
        viewsReferences.put(viewId, new WeakReference<>(view));
    }

    private void addTextWatcher(long viewId, EditText editText) {
        editText.addTextChangedListener(new TextWatcher((int) viewId) {

            boolean nestedCall;

            @Override
            public void afterTextChanged(Editable editable) {
                if (nestedCall) {
                    return;
                }

                if (viewsEntities.hasEmptyEntities(viewId)) {
                    Logger.getInstance().error(WatcherViewModel.this.getClass(),
                            "empty entities for " + AppResources.resourceEntryName(viewId));
                    return;
                }

                editTextsValues.put((long) viewId, (editable != null) ? editable.toString() : null);
                if (onTextChangedCommands != null) {
                    onTextChangedCommands.execute((long) viewId, editable);
                    nestedCall = true;
                    invalidateView(viewId);
                    nestedCall = false;
                }
            }
        });
    }

    protected final String getEditTextValue(long viewId) {
        return editTextsValues.get(viewId);
    }

    protected final void setEditTextValue(long viewId, String value) {
        View v;
        WeakReference<View> viewWeakRef = viewsReferences.get(viewId);
        if (viewWeakRef != null && (v = viewWeakRef.get()) != null) {
            checkTypeAndUpdateEditTextValue(viewId, value, v);
        }
    }

    private void checkTypeAndUpdateEditTextValue(long viewId, String value, View v) {
        if (v instanceof TextView) {
            updateEditTextValue(viewId, value, (TextView) v);
        }
    }

    private void updateEditTextValue(long viewId, String value, TextView editText) {
        editText.setText((value == null) ? EMPTY_STRING : value);
        editTextsValues.put(viewId, value);
    }


    @Override
    public void clear() {
        editTextsValues.clear();
        if (onTextChangedCommands != null) {
            onTextChangedCommands.clear();
        }
        super.clear();
    }
}
