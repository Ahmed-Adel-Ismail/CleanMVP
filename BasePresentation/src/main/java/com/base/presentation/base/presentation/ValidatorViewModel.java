package com.base.presentation.base.presentation;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.base.abstraction.annotations.scanners.MethodAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.Executor;
import com.base.presentation.annotations.interfaces.ViewValidationCommand;
import com.base.presentation.base.abstracts.features.ViewBinder;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * a sub-class for {@link WatcherViewModel} that tends to validate on it's
 * main views added through {@link #addView(View, View, Visibility)}, and based on the state of that
 * {@link View} input, it will show/hide the error {@link View} that is linked to it
 * error messages
 * <p/>
 * Created by Ahmed Adel on 10/9/2016.
 */
public class ValidatorViewModel extends WatcherViewModel {

    private boolean validationActive;
    private HashMap<Long, Long> errorViewsIds;
    private HashMap<Long, Visibility> errorViewsVisibility;
    private HashMap<Long, Visibility> errorViewsDefaultVisibility;
    private Executor<View> errorViewsVisibilityExecutor;
    private Command<View, Void> errorViewVisibilityCommand;

    public ValidatorViewModel() {

    }

    /**
     * @deprecated use {@link #ValidatorViewModel()} the {@link #initialize(ViewBinder)} rather than
     * this constructor, if you invoked this constructor, do not call
     * {@link #initialize(ViewBinder)}
     */
    @Deprecated
    public ValidatorViewModel(ViewBinder viewBinder) {
        super(viewBinder);

    }


    @Override
    public void initialize(ViewBinder viewBinder) {
        super.initialize(viewBinder);
        errorViewsIds = new HashMap<>();
        errorViewsVisibility = new HashMap<>();
        errorViewsDefaultVisibility = new HashMap<>();
        errorViewsVisibilityExecutor = new Executor<>();

        // not tested yet :
//        addValidatorViewIds();
//        addAllViewsFromViewBinder();

        errorViewVisibilityCommand = createErrorViewVisibilityCommand();
    }


    private void addValidatorViewIds() {
        new MethodAnnotationScanner<ViewValidationCommand>(ViewValidationCommand.class) {

            @Override
            protected void onAnnotationFound(Method element, ViewValidationCommand a) {
                viewsIds.add(a.viewId());
                viewsIds.add(a.errorViewId());
                errorViewsIds.put(a.viewId(), a.errorViewId());
                errorViewsVisibility.put(a.errorViewId(), a.defaultVisibility());
                errorViewsDefaultVisibility.put(a.errorViewId(), a.defaultVisibility());
                errorViewsVisibilityExecutor.put(a.errorViewId(), errorViewVisibilityCommand);
            }
        }.execute(this);
    }


    /**
     * add a {@link View} that will need to be validated, if it's data input is not valid,
     * and validation is active (through invoking {@link #setValidationActive(boolean)}),
     * the second {@link View} passed to this method will be visible (which is considered
     * an error view), else it will be hidden
     *
     * @param view      the main view to validate if {@link #setValidationActive(boolean)}
     *                  was set to {@code true}
     * @param errorView the {@link View} to be visible if an error occurred, or
     */
    public void addView(@NonNull View view, View errorView, Visibility defaultVisibility) {
        super.addView(view);
        super.addView(errorView);
        errorViewsIds.put((long) view.getId(), (long) errorView.getId());
        errorViewsVisibility.put((long) errorView.getId(), defaultVisibility);
        errorViewsDefaultVisibility.put((long) errorView.getId(), defaultVisibility);
        errorViewsVisibilityExecutor.put((long) errorView.getId(), errorViewVisibilityCommand);
    }


    @Override
    public void invalidateViews() {
        for (Map.Entry<Long, WeakReference<View>> entry : viewsReferences.entrySet()) {
            View v = entry.getValue().get();
            if (v != null) {
                invalidateAndExecuteErrorViewCommand(entry.getKey(), v);
            }
        }
    }

    private void invalidateAndExecuteErrorViewCommand(Long viewId, View v) {
        invalidateCommands.execute(viewId, v);
        invalidateErrorView(viewId);
    }

    @Override
    public void invalidateView(long viewId) {
        super.invalidateView(viewId);
        invalidateErrorView(viewId);
    }

    private void invalidateErrorView(Long viewId) {
        Long errorViewId = errorViewsIds.get(viewId);
        if (errorViewId != null) {
            invalidateSingleView(errorViewsVisibilityExecutor, errorViewId);
        }
    }


    /**
     * check if the error validation is active or not, where error validation is watching
     * over {@link android.widget.EditText EditTexts} in this {@link ViewModel} and
     * switching there error messages visibility
     *
     * @return {@code true} if the validation is active
     */
    public boolean isValidationActive() {
        return validationActive;
    }

    /**
     * set weather to activate validation over the obligatory
     * {@link android.widget.EditText EditTexts} or not
     *
     * @param validationActive set to {@code true} in the on click of the action to the next
     *                         screen, to activate validations over error message views
     */
    public void setValidationActive(boolean validationActive) {
        this.validationActive = validationActive;
    }

    /**
     * set the error state for an {@link EditText} to have an error so that it should be hidden
     * on next call to {@link #invalidateViews()}
     *
     * @param viewId the {@link EditText} that holds the error
     * @param error  weather it holds an error or not
     */
    public final void setError(long viewId, boolean error) {
        Long errorViewId = errorViewsIds.get(viewId);
        if (errorViewId != null && errorViewsDefaultVisibility.containsKey(errorViewId)) {
            doSetError(errorViewId, error);
        }
    }


    private void doSetError(long errorViewId, boolean error) {
        if (error) {
            errorViewsVisibility.put(errorViewId, errorViewsDefaultVisibility.get(errorViewId));
        } else {
            errorViewsVisibility.put(errorViewId, Visibility.VISIBLE);
        }
    }

    @Override
    public void clear() {
        errorViewsIds.clear();
        errorViewsVisibility.clear();
        errorViewsDefaultVisibility.clear();
        errorViewsVisibilityExecutor.clear();
        super.clear();
    }


    @NonNull
    private Command<View, Void> createErrorViewVisibilityCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                if (isValidationActive()) {
                    doValidation(view);
                }
                return null;
            }

            private void doValidation(View view) {
                long viewId = view.getId();
                if (hasError(viewId)) {
                    showErrorView(view);
                } else {
                    hideErrorView(view, viewId);
                }
            }

            private boolean hasError(long viewId) {
                Visibility errorViewVisibility = errorViewsVisibility.get(viewId);
                return errorViewVisibility != null && !errorViewVisibility.equals(Visibility.VISIBLE);

            }

            private void hideErrorView(View view, long viewId) {
                Visibility defaultVisibility = errorViewsDefaultVisibility.get(viewId);
                if (defaultVisibility != null) {
                    view.setVisibility((int) defaultVisibility.value);
                }
            }

            private void showErrorView(View view) {
                view.setVisibility(Visibility.VISIBLE.value);
            }
        };
    }

    /**
     * an {@code enum} that indicates the View Visibility state
     */
    public enum Visibility {
        VISIBLE(View.VISIBLE),
        INVISIBLE(View.INVISIBLE),
        GONE(View.GONE);

        Visibility(int value) {
            this.value = value;
        }

        private final int value;
    }


}
