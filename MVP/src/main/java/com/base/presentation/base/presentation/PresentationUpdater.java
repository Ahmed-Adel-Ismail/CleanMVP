package com.base.presentation.base.presentation;

import com.base.abstraction.events.Event;
import com.base.abstraction.R;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.models.Model;

/**
 * implement this interface if your class will act as a {@code Presenter}, which will exchange data
 * between views and model
 * <p>
 * for new classes, you can annotate any variables in {@link Model} and {@link ViewModel}
 * with {@link Sync} annotations, and the implementation of this interface will work
 * implicitly ... as in the new {@link Presenter} implementation
 * <p>
 * Created by Ahmed Adel on 10/17/2016.
 *
 * @see Sync
 */
interface PresentationUpdater {

    /**
     * this method is invoked when ever an  {@link R.id#onUpdateModel} {@link Event} is triggered
     * that requests from this class to update the {@link Model} Object
     * <p/>
     * if you have views that does not update the values in your presenter instantly (like
     * {@link android.widget.EditText} with no listener to it), you should read it's value and setVariable
     * it at in this method, and when ever you want to update these kinds of values, you can call
     * this method as well
     */
    void updateModel();

    /**
     * read the latest values from the {@link Model}, and update the {@link ViewModel}
     * with the new data, this method is usually invoked by default in {@link R.id#onResume}
     * in classes like {@link Presenter}
     * <p/>
     * if you handled the {@link Event} of {@link R.id#onResume} in your sub-class, you should
     * invoke this method by your self, the default implementation for {@link Presenter} for example
     * is to handle the {@link R.id#onResume} {@link Event} and invoke this method in it
     * <p/>
     * {@link ViewModel#invalidateViews()} is NOT invoked right after this method,
     * so you should invoke it manually
     */
    void updateViewModel();
}
