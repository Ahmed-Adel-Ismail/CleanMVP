package com.appzoneltd.lastmile.customer.subfeatures.notification;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.exceptions.NotSavedInPreferencesException;
import com.base.abstraction.serializers.JsonLoader;
import com.base.abstraction.system.AppResources;
import com.base.abstraction.system.Preferences;
import com.base.presentation.base.abstracts.features.ViewBinder;

/**
 * Created by Wafaa on 12/12/2016.
 */

public class CounteredNotificationViewModel extends NotificationViewModel {

    public CounteredNotificationViewModel(ViewBinder viewBinder) {
        super(viewBinder);
    }

    @Override
    protected CommandExecutor<Long, View> createInvalidateCommands() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command = createOnInvalidateToolbarCommand();
        commandExecutor.put((long) R.id.activity_main_toolbar, command);
        commandExecutor.put((long) R.id.pickup_toolbar, command);
        return commandExecutor;
    }

    private Command<View, Void> createOnInvalidateToolbarCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                Drawable drawable = createNotificationCounterDrawable(R.drawable.notification);
                if (drawable != null) {
                    notificationItem.setIcon(drawable);
                }
                return null;
            }
        };
    }

    private Drawable createNotificationCounterDrawable(int backgroundImageId) {
        Integer notificationCount = loadNotificationCount();
        LayoutInflater inflater = LayoutInflater.from(getFeature().getHostActivity());
        View view = inflater.inflate(R.layout.layout_notification_count, null);
        view.setBackgroundResource(backgroundImageId);

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
