package com.appzoneltd.lastmile.driver.features.main.host;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;

import com.appzoneltd.lastmile.driver.Flow.MainActivity.HomeFragment;
import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.commands.Command;
import com.base.presentation.references.BooleanProperty;
import com.base.presentation.base.abstracts.features.AbstractFragment;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.ViewModel;

/**
 * the {@link ViewModel} for the main screen
 * <p>
 * Created by Ahmed Adel on 12/20/2016.
 */
class MainViewModel extends ViewModel {


    final BooleanProperty showRequestsProgressBar = new BooleanProperty();
    private AbstractFragment currentFragment;
    private HomeFragment homeFragment;


    @Override
    public void initialize(ViewBinder viewBinder) {
        super.initialize(viewBinder);
        homeFragment = new HomeFragment();
        showRequestsProgressBar.onUpdate(invalidateViewsOnToggleProgress());
    }

    @NonNull
    private Command<Boolean, Boolean> invalidateViewsOnToggleProgress() {
        return new Command<Boolean, Boolean>() {
            @Override
            public Boolean execute(Boolean showProgress) {
                invalidateViews();
                return showProgress;
            }
        };
    }

    @Executable(R.id.screen_main_requests_progress_bar)
    void requestsProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility((showRequestsProgressBar.isTrue()) ? View.VISIBLE : View.GONE);
    }

    @Executable(R.id.screen_host_container_id)
    void containerView(View view) {
        if (currentFragment == null) {
            attachFragment(homeFragment);
        }
    }

    private void attachFragment(AbstractFragment fragment) {
        if (fragment == null) {
            return;
        }
        getFeature().addFragment(R.id.screen_main_fragment_container_frame_layout, fragment);
        currentFragment = this.homeFragment;
    }


    @Override
    public void clear() {
        showRequestsProgressBar.clear();
        super.clear();
    }
}
