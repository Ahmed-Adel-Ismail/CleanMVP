package com.base.presentation.system;

import android.content.Context;
import android.content.Intent;

import com.base.abstraction.concurrency.Future;
import com.base.abstraction.logs.Logger;
import com.base.presentation.base.abstracts.features.AbstractActivity;

/**
 * an enum that represents actions done by the {@link FineLocationDialog}
 * <p>
 * Created by Ahmed Adel on 12/22/2016.
 */
public enum FineLocationDialogAction {
    /**
     * the positive button action
     */
    POSITIVE_BUTTON {
        public Future<Boolean> process(AbstractActivity activity) {
            String action = android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS;
            Intent callGPSSettingIntent = new Intent(action);
            activity.startActivity(callGPSSettingIntent);
            return new Future<Boolean>().setResult(true);
        }
    },
    /**
     * the negative button action
     */
    NEGATIVE_BUTTON {
        @Override
        public Future<Boolean> process(AbstractActivity activity) {
            Logger.getInstance().error(FineLocationDialogAction.class,
                    "location not enabled - negative button clicked");
            return new Future<Boolean>().setResult(false);
        }
    },
    /**
     * the dialog dismissed
     */
    DISMISSED {
        @Override
        public Future<Boolean> process(AbstractActivity activity) {
            Logger.getInstance().error(FineLocationDialogAction.class,
                    "location not enabled - dialog dismissed");
            return new Future<Boolean>().setResult(false);
        }
    };

    /**
     * process the current action
     *
     * @param activity the {@link Context} to host the action
     * @return a {@link Future} that indicates weather the processed action lead to
     * opening the location settings or not, if settings were opened,
     * {@link Future#setResult(Object)} will be set to {@code true}, else
     * it will be set to {@code false}
     */
    public abstract Future<Boolean> process(AbstractActivity activity);
}