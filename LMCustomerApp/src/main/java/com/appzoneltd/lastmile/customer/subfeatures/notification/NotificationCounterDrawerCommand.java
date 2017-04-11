package com.appzoneltd.lastmile.customer.subfeatures.notification;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.system.AppResources;
import com.base.abstraction.system.Preferences;

import java.lang.ref.WeakReference;

/**
 * Created by Wafaa on 1/3/2017.
 */

public class NotificationCounterDrawerCommand implements Command<MenuItem, Drawable> {

    private WeakReference<Context> contextRef;

    public NotificationCounterDrawerCommand(Context context) {
        contextRef = new WeakReference<Context>(context);
    }


    @Override
    public Drawable execute(MenuItem p) {

        Integer notificationCount = loadNotificationCount();
        LayoutInflater inflater = LayoutInflater.from(contextRef.get());
        View view = inflater.inflate(R.layout.layout_notification_count, null);
        view.setBackgroundResource(R.drawable.notification);

        if (notificationCount == 0) {
            View counterTextPanel = view.findViewById(R.id.counter_layout);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + notificationCount);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(AppResources.getResources(), bitmap);
    }

    private Integer loadNotificationCount() {
        return Preferences.getInstance().load(R.string.PREFS_KEY_NOTIFICATION_COUNTER, 0);
    }
}
