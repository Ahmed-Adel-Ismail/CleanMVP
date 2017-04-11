package com.appzoneltd.lastmile.customer.abstracts;

import android.os.Bundle;
import android.widget.Toast;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.splash.LoaderViewBinder;
import com.appzoneltd.lastmile.customer.system.LastMileApp;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.models.Model;

/**
 * The base Activity for LastMile project
 * <p/>
 * Created by Ahmed Adel on 9/4/2016.
 */
public abstract class LastMileActivity<T extends Model> extends AbstractActivity<T> {


    @Override
    public final ViewBinder createViewBinder(Bundle savedInstanceState) {
        LastMileApp app = App.getInstance();
        if (app.getAppLoader().isReady()) {
            return createLastMileViewBinder(savedInstanceState);
        } else {
            return new LoaderViewBinder<>(this);
        }

    }

    /**
     * same as {@link #createViewBinder(Bundle)} but after checking if the current
     * {@link LastMileActivity} does not need to go for special {@link ViewBinder}
     * like Splash or Loading screens
     *
     * @param savedInstanceState the parameter passed to {@link #createViewBinder(Bundle)}
     * @return the {@link ViewBinder} to be used
     */
    protected abstract ViewBinder createLastMileViewBinder(Bundle savedInstanceState);


    @Override
    public void onLockedInteractions() {
        Toast.makeText(this, AppResources.string(R.string.please_wait), Toast.LENGTH_SHORT).show();
    }

}
