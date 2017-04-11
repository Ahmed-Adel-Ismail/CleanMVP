package com.base.presentation.base.presentation;

import android.view.View;

import com.base.presentation.models.Model;

/**
 * an {@link PresenterHandler} that handles clicks on {@link View}
 * <p>
 * Created by Ahmed Adel on 11/24/2016.
 */
public class PresenterClicksHandler<
        P extends Presenter<P, V, M>,
        V extends ViewModel,
        M extends Model>
        extends PresenterHandler<View, P, V, M> {


}
