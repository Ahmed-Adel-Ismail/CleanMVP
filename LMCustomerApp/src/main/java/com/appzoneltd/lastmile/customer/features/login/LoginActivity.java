package com.appzoneltd.lastmile.customer.features.login;

import android.os.Bundle;

import com.appzoneltd.lastmile.customer.abstracts.LastMileActivity;
import com.appzoneltd.lastmile.customer.interfaces.ActivityHiddenKeyboard;
import com.appzoneltd.lastmile.customer.interfaces.ActivityNoTitle;
import com.appzoneltd.lastmile.customer.interfaces.ActivityTransparentWindow;
import com.base.presentation.base.abstracts.features.ViewBinder;

public class LoginActivity extends LastMileActivity<LoginModel> implements
        ActivityNoTitle,
        ActivityTransparentWindow,
        ActivityHiddenKeyboard {


    @Override
    public ViewBinder createLastMileViewBinder(Bundle savedInstanceState) {
        return new LoginViewBinder(this);
    }

}
