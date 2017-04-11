package com.appzoneltd.lastmile.customer.subfeatures.notificationcommands;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.RatingBar;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.Future;
import com.entities.cached.RatingRequestParams;

import java.lang.ref.WeakReference;

/**
 * Created by Wafaa on 12/29/2016.
 */

public class RatingDialogHandler implements Command<Activity, Future<RatingRequestParams>> {

    private RatingRequestParams rating;
    private Future<RatingRequestParams> future;

    public RatingDialogHandler() {
        rating = new RatingRequestParams();
        future = new Future<>();
    }

    @Override
    public Future<RatingRequestParams> execute(Activity context) {
        WeakReference<Activity> activityRef = new WeakReference<>(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(activityRef.get());
        builder.setTitle(R.string.rating_service_title);
        View layout = activityRef.get().getLayoutInflater()
                .inflate(R.layout.layout_reating_service, null);
        builder.setView(layout);
        RatingBar driverRating = (RatingBar) layout.findViewById(R.id.rating_driver_bar);
        driverRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rate, boolean fromUser) {
                rating.setDriverRate(rate);
            }
        });

        RatingBar serviceRating = (RatingBar) layout.findViewById(R.id.rating_service_bar);

        serviceRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rate, boolean fromUser) {
                rating.setServiceRate(rate);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                future.setResult(null);
            }
        });

        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                future.setResult(rating);
            }
        });
        builder.setCancelable(false);
        builder.create().show();
        return future;
    }
}
