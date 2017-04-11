package com.appzoneltd.lastmile.customer.features.pickup.models;


import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.host.Titleable;
import com.appzoneltd.lastmile.customer.features.pickup.repos.PickupRepository;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.AppResources;
import com.base.presentation.models.Model;
import com.base.presentation.repos.base.Repository;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;
import com.entities.Notification;
import com.entities.cached.Rating;
import com.entities.cached.RatingRequestParams;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * A Class that is the {@link Model} for the Pickup JsonRequest feature in the application
 * <p/>
 * Created by Ahmed Adel on 9/21/2016.
 */
public class PickupModel extends Model implements
        Serializable,
        Titleable {

    public Notification notification;
    private static SimpleDateFormat dateFormatter;
    private final Schedule schedule;
    private final Package packageDetails;
    private final Recipient recipient;
    private String pickupLongitude;
    private String pickupLatitude;
    private String pickupFormattedAddress;
    private String pickupDisplayedAddress;
    private List<Long> imageIds;
    public RatingRequestParams rating;

    static {
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    @Override
    public void preInitialize() {
        this.imageIds = new LinkedList<>();
    }

    public PickupModel() {
        schedule = new Schedule();
        packageDetails = new Package();
        recipient = new Recipient();
    }


    public final String getPickupFormattedAddress() {
        return pickupFormattedAddress;
    }

    public String getPickupLongitude() {
        return pickupLongitude;
    }

    public String getPickupLatitude() {
        return pickupLatitude;
    }

    public final void setPickupFormattedAddress(String pickupFormattedAddress) {
        this.pickupFormattedAddress = pickupFormattedAddress;
    }

    public void setPickupLongitude(String pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public void setPickupLatitude(String pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    final List<Long> getImageIds() {
        return imageIds;
    }

    public static SimpleDateFormat getDateFormatter() {
        return dateFormatter;
    }

    @NonNull
    public Package getPackage() {
        return packageDetails;
    }

    @NonNull
    public Schedule getSchedule() {
        return schedule;
    }

    @NonNull
    public Recipient getRecipient() {
        return recipient;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(RatingRequestParams rating) {
        this.rating = rating;
    }

    public String getPickupDisplayedAddress() {
        return pickupDisplayedAddress;
    }

    public void setPickupDisplayedAddress(String pickupDisplayedAddress) {
        this.pickupDisplayedAddress = pickupDisplayedAddress;
    }

    @Override
    public void onClear() {
        schedule.clear();
        packageDetails.clear();
        recipient.clear();
        imageIds.clear();
    }

    @Override
    public final void requestFromRepository(long requestId, RequestMessage requestMessage) {
        super.requestFromRepository(requestId, requestMessage);
    }


    @NonNull
    @Override
    protected Repository createRepository() {
        return new PickupRepository();
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, Message> createOnViewsUpdatedCommands() {
        return new RequestsExecutor(this);
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createOnRepositoryUpdatedCommands() {
        return new RepositoryUpdatesExecutor(this);
    }

    @Override
    public String getTile() {
        return AppResources.string(R.string.review_title);
    }

}
