package com.appzoneltd.lastmile.customer.features.pickup.models;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.bitmaps.BitmapCompressor;
import com.base.abstraction.bitmaps.BitmapEncoder;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Message;
import com.base.abstraction.serializers.JsonLoader;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.abstraction.system.Behaviors;
import com.base.requesters.UploadImageParam;
import com.base.usecases.events.RequestMessage;
import com.entities.cached.Package;
import com.entities.cached.Pickup;
import com.entities.cached.UserInfo;
import com.entities.requesters.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A {@link CommandExecutor} to do the requests for {@link PickupModel}
 * <p/>
 * Created by Ahmed Adel on 9/28/2016.
 */
class RequestsExecutor extends CommandExecutor<Long, Message> {

    private PickupModel model;


    RequestsExecutor(PickupModel model) {
        this.model = model;
        Command<Message, Void> command = createRequestPickupTimeIntervalCommand();
        put((long) R.id.requestPickupTimeInterval, command);
        command = createRequestPackageTypesCommand();
        put((long) R.id.requestPackageTypes, command);
        command = createOnShipmentServiceCommand();
        put((long) R.id.requestShipmentService, command);
        command = createOnServiceTypesCommand();
        put((long) R.id.requestShipmentServiceTypes, command);
        command = createOnCountryCommand();
        put((long) R.id.requestCountries, command);
        command = createOnSubmitCommand();
        put((long) R.id.requestSubmitPickupRequest, command);
        command = createOnCreatePackageRequestCommand();
        put((long) R.id.requestCreatePackage, command);
        command = createOnCreatePickupRequestCommand();
        put((long) R.id.requestCreatePickupRequest, command);
        command = createOnCityCommand();
        put((long) R.id.requestCities, command);
        put((long) R.id.requestRating , createRatingRequestCommand());
    }

    private Command<Message, Void> createRequestPickupTimeIntervalCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                final RequestMessage message = new RequestMessage.Builder().build();
                model.requestFromRepository(R.id.requestPickupTimeInterval, message);
                return null;
            }
        };
    }

    private Command<Message, Void> createRequestPackageTypesCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                final RequestMessage message = new RequestMessage.Builder()
                        .build();
                if (model.getPackage().getPackageTypes() == null) {
                    model.requestFromRepository(R.id.requestPackageTypes, message);
                }
                return null;
            }
        };
    }

    private Command<Message, Void> createOnShipmentServiceCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                final RequestMessage message = new RequestMessage.Builder()
                        .build();
                model.requestFromRepository(R.id.requestShipmentService, message);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnServiceTypesCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                final RequestMessage message;
                RequestBody body = new RequestBody(model.getRecipient().getServiceId());
                message = new RequestMessage.Builder()
                        .content(body)
                        .build();
                model.requestFromRepository(R.id.requestShipmentServiceTypes,
                        message);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnCountryCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                final RequestMessage message = new RequestMessage.Builder()
                        .build();
                model.requestFromRepository(R.id.requestCountries, message);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnCityCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                RequestMessage message = new RequestMessage.Builder()
                        .content(new RequestBody(
                                model.getRecipient().getCurrentlySelectedCountryId()))
                        .build();
                model.requestFromRepository(R.id.requestCities, message);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnSubmitCommand() {
        return new Command<Message, Void>() {

            private static final String IMG_EXTENSION = "jpeg";
            private static final String IMG_PREFIX = "Image_";
            private static final String IMG_SEPARATOR = "_";
            private BitmapEncoder bitmapEncoder = new BitmapEncoder(new BitmapCompressor());

            @Override
            public Void execute(Message message) {
                RequestMessage requestMessage;
                List<String> encodedImages = createEncodedImageStrings();
                int index = 0;
                long timeStamp = new Date().getTime();
                for (String encodedImage : encodedImages) {
                    UploadImageParam p = new UploadImageParam();
                    p.setExtension(IMG_EXTENSION);
                    p.setName(IMG_PREFIX + (index++) + IMG_SEPARATOR + timeStamp);
                    p.setBase64ByteArray(encodedImage);
                    requestMessage = new RequestMessage.Builder().content(p).build();
                    model.requestFromRepository(R.id.requestSubmitPickupRequest, requestMessage);
                }
                return null;
            }

            private List<String> createEncodedImageStrings() {
                List<String> encodedImages = new ArrayList<>();
                Bitmap firstBitmap = model.getPackage().getFirstPhotoBitmap();
                Bitmap secondBitmap = model.getPackage().getSecondPhotoBitmap();
                if (firstBitmap != null) {
                    encodeAndAddImageString(encodedImages, firstBitmap);
                }

                if (secondBitmap != null) {
                    encodeAndAddImageString(encodedImages, secondBitmap);
                }

                return encodedImages;
            }

            private void encodeAndAddImageString(List<String> encodedImages, Bitmap bitmap) {
                encodedImages.add(bitmapEncoder.execute(bitmap));
            }

        };
    }

    private Command<Message, Void> createOnCreatePackageRequestCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                final RequestMessage requestMessage;
                List<Long> imageIds = message.getContent();
                Package p = createPackage(imageIds);
                requestMessage = new RequestMessage.Builder().content(p).build();
                model.requestFromRepository(R.id.requestCreatePackage, requestMessage);
                return null;
            }

            private com.entities.cached.Package createPackage(List<Long> imageIds) {
                com.entities.cached.Package pack = new Package();

                BigDecimal decimal;
                if (model.getPackage().isBoxSelected()) {
                    decimal = BigDecimal.valueOf(model.getPackage().getBoxWeight());
                } else {
                    decimal = BigDecimal.valueOf(model.getPackage().getPackageType().getExpectedWeight());
                }

                pack.setActualWeight(decimal);
                pack.setPackageTypeId(model.getPackage().getPackageType().getPackageTypeId());
                pack.setDescription(model.getPackage().getDescription());
                pack.setImageIds(imageIds);
                pack.setNickName(model.getPackage().getNickname());
                pack.setPackageId(0);
                pack.setShipmentServiceTypeId(model.getRecipient().getServiceTypeId());
                return pack;
            }
        };

    }

    private Command<Message, Void> createOnCreatePickupRequestCommand() {
        return new Command<Message, Void>() {


            @Override
            public Void execute(Message message) {
                long packageId = message.getContent();
                final RequestMessage requestMessage;
                Pickup pickup = createPickup(packageId);
                requestMessage = new RequestMessage.Builder().content(pickup).build();
                model.requestFromRepository(R.id.requestCreatePickupRequest, requestMessage);
                return null;
            }

            private Pickup createPickup(long packageId) {

                Date date = new Date();
                Pickup pickup = new Pickup();
                pickup.setAdditionalServices(model.getPackage().getAdditionalServices());
                pickup.setDescription(model.getPackage().getDescription());
                pickup.setLabelingText(model.getPackage().getLabelMessage());
                pickup.setPaymentType(model.getPackage().getPaymentType().label);
                pickup.setPickupDate(getPickupDate(date));
                pickup.setPickupFormatedAddress(model.getPickupFormattedAddress());
                pickup.setPickupLatitude(model.getPickupLatitude());
                pickup.setPickupLongitude(model.getPickupLongitude());
                pickup.setRecipientAdditionalInfo(model.getRecipient().getNotes());
                pickup.setRecipientFormatedAddress(model.getRecipient().getAddress());
                pickup.setRecipientMobile(model.getRecipient().getPhoneNumber());
                pickup.setRecipientName(model.getRecipient().getName());
                pickup.setRequestTime(String.valueOf(date.getTime()));
                pickup.setRequestType(getRequestType());
                pickup.setUserInfo(loadUserInfo());
                pickup.setPackageId(packageId);
                pickup.setPaymentMethod(AppResources.string(R.string.payment_method));
                setScheduledTime(pickup);
                return pickup;
            }

            @Nullable
            private UserInfo loadUserInfo() {
                JsonLoader<UserInfo> loader = new JsonLoader<>(R.string.PREFS_KEY_USER_INFO);
                UserInfo userInfo = loader.execute(UserInfo.class);
                if (userInfo == null && App.getInstance().isBehaviorAccepted(Behaviors.TESTING)) {
                    userInfo = UserInfo.createFakeUser();
                }
                return userInfo;
            }


            private String getPickupDate(Date defaultDate) {
                if (model.getSchedule().isScheduled()) {
                    defaultDate = model.getSchedule().getDate();
                }
                return PickupModel.getDateFormatter().format(defaultDate);
            }


            private String getRequestType() {
                if (model.getSchedule().isScheduled()) {
                    return AppResources.string(R.string.pickup_scheduled);
                } else {
                    return AppResources.string(R.string.on_demand);
                }
            }

            private void setScheduledTime(Pickup pickup) {
                if (model.getSchedule().isScheduled()) {
                    pickup.setTimeFrom(model.getSchedule().getPickupTimes()
                            .get(model.getSchedule().getPickupTimeSelectedIndex()).getFromTime());
                    pickup.setTimeTo(model.getSchedule().getPickupTimes()
                            .get(model.getSchedule().getPickupTimeSelectedIndex()).getToTime());
                }
            }

        };

    }

    private Command<Message, Void> createRatingRequestCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                RequestMessage requestMessage = new RequestMessage.Builder()
                        .content(model.getRating()).build();
                model.requestFromRepository(R.id.requestRating, requestMessage);
                return null;
            }

        };
    }

}
