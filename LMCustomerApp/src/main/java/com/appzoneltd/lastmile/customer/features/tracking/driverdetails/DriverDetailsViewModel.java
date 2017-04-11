package com.appzoneltd.lastmile.customer.features.tracking.driverdetails;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.subfeatures.RingProgressBar;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.logs.Logger;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.ViewModel;
import com.base.presentation.requesters.images.HttpsPicasso;
import com.entities.cached.PayloadActiveVehicleDetails;


/**
 * Created by Wafaa on 11/27/2016.
 */

public class DriverDetailsViewModel extends ViewModel {

    protected String uri;
    protected long progress;
    protected int max = 1;
    String estimationTimeText = "0 mins";
    private final String imageSize = "large";
    @Sync("vehicleDetails")
    PayloadActiveVehicleDetails payloadActiveVehicleDetails;

    public DriverDetailsViewModel(ViewBinder viewBinder) {
        super(viewBinder);
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, View> createInvalidateCommands() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command;
        command = createOnTimeWheelCommand();
        commandExecutor.put((long) R.id.time_wheel, command);
        command = createOnNameCommand();
        commandExecutor.put((long) R.id.driver_name, command);
        command = createOnVehicleDetailsCommand();
        commandExecutor.put((long) R.id.vehicle_details, command);
        command = createOnRateCommand();
        commandExecutor.put((long) R.id.driver_rating, command);
        command = createOnImageCommand();
        commandExecutor.put((long) R.id.driver_details_image, command);
        return commandExecutor;
    }

    private Command<View, Void> createOnTimeWheelCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                try {
                    RingProgressBar wheel = (RingProgressBar) view;
                    if (max < 1) {
                        max = 1;
                    }
                    if (progress > 0) {
                        wheel.setProgress((int) progress);
                    }
                    wheel.setMax(max);
                } catch (Exception e) {
                    Logger.getInstance().exception(e);
                }

                return null;
            }
        };
    }


    private Command<View, Void> createOnNameCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                TextView name = (TextView) p;
                name.setText(payloadActiveVehicleDetails.getDriverName());
                return null;
            }
        };
    }

    private Command<View, Void> createOnVehicleDetailsCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                TextView details = (TextView) view;
                details.setText(String.valueOf(payloadActiveVehicleDetails.getVehicleModel()
                        + " "
                        + payloadActiveVehicleDetails.getVehiclePlateNumber()));
                return null;
            }
        };
    }

    private Command<View, Void> createOnRateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                RatingBar bar = (RatingBar) view;
                bar.setRating(payloadActiveVehicleDetails.getDriverRating());
                return null;
            }
        };
    }

    private Command<View, Void> createOnImageCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                if (!TextUtils.isEmpty(uri)) {
                    ImageView imageView = (ImageView) view;
                    HttpsPicasso.getInstance(getFeature().getHostActivity())
                            .load(uri + "/" + imageSize)
                            .into(imageView);
                }
                return null;
            }
        };
    }


    @Override
    public void onDestroy() {

    }
}
