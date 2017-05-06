package com.base.presentation.base.presentation;

import android.view.MenuItem;

import com.base.presentation.models.Model;

/**
 * an {@link PresenterHandler} that handles clicks on {@link MenuItem}
 * <p>
 * Created by Ahmed Adel on 12/18/2016.
 */
public class PresenterNavigationMenuItemsHandler<
        P extends Presenter<P, V, M>,
        V extends ViewModel,
        M extends Model>
        extends PresenterHandler<MenuItem, P, V, M> {
}
