package com.appzoneltd.lastmile.customer.features.pickup.recipient;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.App;
import com.base.presentation.base.presentation.PresenterUpdater;
import com.base.presentation.commands.RequestBasedCommand;
import com.base.presentation.commands.RequesterCommand;

/**
 * a {@link PresenterUpdater} for {@link RecipientDetailsPresenter}
 * <p>
 * Created by Ahmed Adel on 10/17/2016.
 */
class RecipientDetailsUpdater extends
        PresenterUpdater<RecipientDetailsPresenter, RecipientDetailsViewModel, PickupModel> {

    @Override
    public void onUpdateViewModel() {
        getViewModel().setShippingServiceArray(getModel().getRecipient().getShippingServiceArray());
        getViewModel().setShippingServiceSelectedPosition(
                getModel().getRecipient().getServiceSelectedPosition());
        getViewModel().setShippingServiceTypesArray(
                getModel().getRecipient().getShippingTypesArray());
        getViewModel().setShippingServiceTypeSelectedPosition
                (getModel().getRecipient().getServiceTypeSelectedPosition());
        getViewModel().setCountriesArray(getModel().getRecipient().getCountriesArray());
        getViewModel().setCountrySelectedPosition(getModel().getRecipient()
                .getCountrySelectedPosition());
        getViewModel().setRecipientName(getModel().getRecipient().getName());
        getViewModel().setCitiesArray(getModel().getRecipient().getCitiesArray());
        getViewModel().setCitySelectedPosition(getModel().getRecipient()
                .getCitySelectedPosition());
        getViewModel().setRecipientPhoneNumber(getModel().getRecipient().getPhoneNumber());
        getViewModel().setRecipientAddress(getModel().getRecipient().getFullAddress());
        getViewModel().setRecipientNotes(getModel().getRecipient().getNotes());
        createOnShippingCommand(getPresenter().getShippingServiceRequesterCommand()).execute(null);
        createOnShippingTypesCommand(getPresenter().getShippingTypesRequesterCommand());
        createOnCountryCommand(getPresenter().getCountryRequesterCommand()).execute(null);
        createOnCityCommand(getPresenter().getCityRequesterCommand());
        getViewModel().invalidateViews();
    }

    private RequestBasedCommand<Void> createOnShippingCommand(RequesterCommand command) {
        return new RequestBasedCommand<Void>(command) {
            @Override
            protected Void onExecute(Message message) {
                getPresenter().getRecipientDetailsOnResponseHandler()
                        .invalidateShippingResponse();
                return null;
            }

            @Override
            protected Void onFailure(Message message) {
                getPresenter().getRecipientDetailsOnResponseHandler().invalidateShippingResponse();
                App.getInstance().getActorSystem()
                        .get((long) R.id.addressActivity)
                        .execute(createToastEvent(
                                R.string.error_loading_pickup_services));
                return null;
            }
        };
    }

    private RequestBasedCommand<Void> createOnShippingTypesCommand(RequesterCommand command) {
        return new RequestBasedCommand<Void>(command) {
            @Override
            protected Void onExecute(Message message) {
                getPresenter().getRecipientDetailsOnResponseHandler().invalidateServiceTypesSpinner();
                return null;
            }

            @Override
            protected Void onFailure(Message message) {
                App.getInstance().getActorSystem()
                        .get((long) R.id.addressActivity)
                        .execute(createToastEvent(R.string.error_loading_pickup_services_types));
                return null;
            }
        };
    }

    private RequestBasedCommand<Void> createOnCountryCommand(RequesterCommand command) {
        return new RequestBasedCommand<Void>(command) {
            @Override
            protected Void onExecute(Message message) {
                getPresenter().getRecipientDetailsOnResponseHandler().invalidateCountriesSpinner();
                return null;
            }

            @Override
            protected Void onFailure(Message message) {
                getPresenter().getRecipientDetailsOnResponseHandler().invalidateCountriesSpinner();
                App.getInstance().getActorSystem()
                        .get((long) R.id.addressActivity)
                        .execute(createToastEvent(R.string.error_loading_countries));
                return null;
            }
        };
    }

    private RequestBasedCommand<Void> createOnCityCommand(RequesterCommand command) {
        return new RequestBasedCommand<Void>(command) {
            @Override
            protected Void onExecute(Message message) {
                getPresenter().getRecipientDetailsOnResponseHandler().invalidateCitySpinner();
                return null;
            }

            @Override
            protected Void onFailure(Message message) {
                getPresenter().getRecipientDetailsOnResponseHandler().invalidateCitySpinner();
                App.getInstance().getActorSystem()
                        .get((long) R.id.addressActivity)
                        .execute(createToastEvent(R.string.error_loading_cities));
                return null;
            }
        };
    }


    private Event createToastEvent(int msgId) {
        Message message = new Message.Builder()
                .content(msgId).build();
        return new Event.Builder(R.id.showToast)
                .message(message)
                .build();
    }

    @Override
    public void onUpdateModel() {
        getModel().getRecipient().setName(getViewModel().getRecipientName());
        getModel().getRecipient().setPhoneNumber(getViewModel().getRecipientPhoneNumber());
        getModel().getRecipient().setFullAddress(getViewModel().getRecipientFullAddress());
        getModel().getRecipient().setNotes(getViewModel().getRecipientNotes());
    }
}
