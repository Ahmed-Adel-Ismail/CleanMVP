package com.appzoneltd.lastmile.customer.deprecated;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.deprecated.utilities.UIManager;
import com.appzoneltd.lastmile.customer.deprecatred.SharedManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @deprecated old implementation that should be replaced
 */
public class ConnectionErrorActivity extends FragmentActivity {

    private String titleTxt;

    @BindView(R.id.tv_title)
    TextView title;

    @OnClick(R.id.retry)
    void OnRetryClicked() {
        Intent intent;
        if (UIManager.isConnectedToInternet(this)) {
            if (titleTxt.equals(SharedManager.LOGIN_TITLE)) {
                intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else if (titleTxt.equals(SharedManager.HOME_TITLE)) {
                intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else if (titleTxt.equals(SharedManager.PACKAGE_DETAILS_TITLE)) {
                intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else if (titleTxt.equals(SharedManager.RECIPIENT_DETAILS_TITLE)) {
                intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else if (titleTxt.equals(SharedManager.SUBMIT_REQUEST_TITLE)) {
                intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_error);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            titleTxt = bundle.getString(SharedManager.CONNECTION_ERROR_TITLE);
            if (titleTxt != null)
                title.setText(titleTxt);
        }
        setFinishOnTouchOutside(false);
    }

}
