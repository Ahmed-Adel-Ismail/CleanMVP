package com.appzoneltd.lastmile.customer.deprecated.utilities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Wafaa on 6/14/2016.
 *
 * @deprecated should be removed, this class causes memory leaks
 */
public class UIManager {

    private static UIManager instance = null;
    private Context mContext;

    private UIManager() {
    }

    /**
     * @deprecated DO NOT USE THIS METHOD
     */
    public static UIManager getInstance(Context context) {
        if (instance == null)
            instance = new UIManager();
        instance.setContext(context);
        return instance;
    }

    private void setContext(Context context) {
        this.mContext = context;
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void underline(TextView textView) {
        SpannableString content = new SpannableString(textView.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
    }

}
