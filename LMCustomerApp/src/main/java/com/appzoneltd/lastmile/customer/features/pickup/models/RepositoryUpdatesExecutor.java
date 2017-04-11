package com.appzoneltd.lastmile.customer.features.pickup.models;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.cached.ServerMessage;
import com.base.cached.UploadImageResponse;
import com.base.usecases.events.ResponseMessage;
import com.entities.cached.City;
import com.entities.cached.Country;
import com.entities.cached.PackageType;
import com.entities.cached.PickupTime;
import com.entities.cached.ShipmentService;
import com.entities.cached.ShipmentServiceTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles {@link PickupModel} responses from RepositoriesGroup
 * <p/>
 * Created by Ahmed Adel on 9/28/2016.
 */
class RepositoryUpdatesExecutor extends CommandExecutor<Long, ResponseMessage> {

    private PickupModel model;
    private final List<Long> imageIds;

    RepositoryUpdatesExecutor(PickupModel model) {
        this.model = model;
        this.imageIds = model.getImageIds();
        Command<ResponseMessage, Void> command = createOnPickupTimeIntervalResponseCommand();
        put((long) R.id.requestPickupTimeInterval, command);
        command = createOnShipmentServiceResponseCommand();
        put((long) R.id.requestShipmentService, command);
        command = createOnServiceTypesResponseCommand();
        put((long) R.id.requestShipmentServiceTypes, command);
        command = createOnCountriesResponse();
        put((long) R.id.requestCountries, command);
        command = createOnSubmitResponse();
        put((long) R.id.requestSubmitPickupRequest, command);
        command = createOnPackageTypesResponse();
        put((long) R.id.requestPackageTypes, command);
        command = createOnCreatePackageResponse();
        put((long) R.id.requestCreatePackage, command);
        command = createOnCreateRequestResponse();
        put((long) R.id.requestCreatePickupRequest, command);
        command = createOnCitiesResponse();
        put((long) R.id.requestCities, command);
        put((long) R.id.requestRating, createOnRatingResponseCommand());
    }

    private Command<ResponseMessage, Void> createOnPickupTimeIntervalResponseCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                if (message.isSuccessful()) {
                    List<PickupTime> pickupTimes = message.getContent();
                    String[] pickupTimesStringArray = createPickupTimesStringArray(pickupTimes);
                    model.getSchedule().setPickupTimesStringArray(pickupTimesStringArray);
                    model.getSchedule().setSelectedTime(pickupTimesStringArray[0]);
                    model.getSchedule().setSelectedTimeId(pickupTimes.get(0).getPickupTimeId());
                    model.getSchedule().setPickupTimes(pickupTimes);
                }
                model.notifyOnRepositoryResponse(message);
                return null;
            }

            @NonNull
            private String[] createPickupTimesStringArray(List<PickupTime> pickupTimes) {
                int pickupTimesCount = pickupTimes.size();
                String[] pickupTimesStringArray = new String[pickupTimesCount];
                for (int i = 0; i < pickupTimesCount; i++) {
                    pickupTimesStringArray[i] = pickupTimes.get(i).createPickupTimeItem();
                }
                return pickupTimesStringArray;
            }
        };
    }

    private Command<ResponseMessage, Void> createOnShipmentServiceResponseCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                if (message.isSuccessful()) {
                    List<ShipmentService> shipmentServices = message.getContent();
                    model.getRecipient().setShipmentServices(shipmentServices);
                }
                model.notifyOnRepositoryResponse(message);
                return null;
            }
        };
    }

    private Command<ResponseMessage, Void> createOnServiceTypesResponseCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage p) {
                if (p.isSuccessful()) {
                    List<ShipmentServiceTypes> shipmentServiceTypes = p.getContent();
                    model.getRecipient().setShipmentServiceTypes(shipmentServiceTypes);
                }
                model.notifyOnRepositoryResponse(p);
                return null;
            }
        };
    }

    private Command<ResponseMessage, Void> createOnCountriesResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                if (message.isSuccessful()) {
                    List<Country> countries = message.getContent();
                    model.getRecipient().setCountries(countries);
                }
                model.notifyOnRepositoryResponse(message);
                return null;
            }
        };
    }

    private Command<ResponseMessage, Void> createOnCitiesResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                if (message.isSuccessful()) {
                    List<City> cities = message.getContent();
                    model.getRecipient().setCities(cities);
                }
                model.notifyOnRepositoryResponse(message);
                return null;
            }
        };
    }

    private Command<ResponseMessage, Void> createOnPackageTypesResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage response) {
                boolean successful = response.isSuccessful();
                model.getPackage().setPackageTypesLoaded(successful);
                if (successful) {
                    List<PackageType> packageTypes = response.getContent();
                    model.getPackage().setPackageTypes(packageTypes);
                }
                model.notifyOnRepositoryResponse(response);
                return null;
            }
        };
    }

    private Command<ResponseMessage, Void> createOnSubmitResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                if (message.isSuccessful()) {
                    addImageIdAndSendNextRequest(message);
                } else {
                    notifyFailure(message);
                }
                return null;
            }

            private void addImageIdAndSendNextRequest(ResponseMessage message) {
                UploadImageResponse response = message.getContent();
                if(response != null) {
                    fillImagesListAndRequestCreatePackage(response);
                }
            }

            private void fillImagesListAndRequestCreatePackage(UploadImageResponse response) {
                imageIds.add(response.getFileId());
                if (imageIds.size() == model.getPackage().getImagesCount()) {
                    requestCreatePackage();
                }
            }

            private void requestCreatePackage() {
                List<Long> newList = new ArrayList<>(imageIds);
                Message newMessage = new Message.Builder().content(newList).build();
                Event event = new Event.Builder(R.id.requestCreatePackage)
                        .message(newMessage).build();
                model.execute(event);
                imageIds.clear();
            }

            private void notifyFailure(ResponseMessage message) {
                imageIds.clear();
                model.notifyOnRepositoryResponse(message);
            }

        };
    }

    private Command<ResponseMessage, Void> createOnCreatePackageResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage p) {
                if (p.isSuccessful()) {
                    ServerMessage message = p.getContent();
                    long packageId = Long.valueOf(message.getProperty());
                    requestCreatePickup(packageId);
                } else {
                    model.notifyOnRepositoryResponse(p);
                }
                return null;
            }

            private void requestCreatePickup(long packageId) {
                Message message = new Message.Builder().content(packageId).build();
                Event event = new Event.Builder(R.id.requestCreatePickupRequest).message(message).build();
                model.execute(event);
            }

        };
    }

    private Command<ResponseMessage, Void> createOnCreateRequestResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                model.notifyOnRepositoryResponse(message);
                return null;
            }
        };
    }

    private Command<ResponseMessage, Void> createOnRatingResponseCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                model.notifyOnRepositoryResponse(message);
                return null;
            }

        };
    }

}
