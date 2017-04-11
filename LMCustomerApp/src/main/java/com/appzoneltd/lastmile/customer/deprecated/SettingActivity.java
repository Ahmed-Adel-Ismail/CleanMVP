package com.appzoneltd.lastmile.customer.deprecated;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.deprecatred.SharedManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Deprecated
public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.url)
    EditText url;

    @BindView(R.id.reg_id)
    TextView regId;

    @OnClick(R.id.ok)
    void ok() {
        SharedManager.getInstance().setWebServiceURL(url.getText().toString());
        finish();
    }

    @OnClick(R.id.cancel)
    void cancel() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_url);
        ButterKnife.bind(this);
        url.setText(SharedManager.getInstance().getWebServiceURL());
        regId.setText(SharedManager.getInstance().getRegId());
    }
}
