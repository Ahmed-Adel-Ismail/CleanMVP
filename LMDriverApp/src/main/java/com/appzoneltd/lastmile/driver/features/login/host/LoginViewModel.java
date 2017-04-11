package com.appzoneltd.lastmile.driver.features.login.host;

import android.view.View;

import com.appzoneltd.lastmile.driver.Flow;
import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.presentation.base.presentation.ViewModel;

import static com.appzoneltd.lastmile.driver.Flow.LoginActivity.SignInFragment;

/**
 * the {@link ViewModel} for login host screen
 * <p>
 * Created by Ahmed Adel on 11/23/2016.
 */
class LoginViewModel extends ViewModel {


    private SignInFragment signInFragment;


    @Executable(R.id.screen_host_container_id)
    void invalidateFragment(View view) {
        if (signInFragment == null) {
            attachFragment();
        }
    }

    private void attachFragment() {
        signInFragment = new Flow.LoginActivity.SignInFragment();
        getFeature().getHostActivity().addFragment(R.id.screen_host_container_id, signInFragment);
    }

    @Override
    public void onDestroy() {
        signInFragment = null;
    }
}
