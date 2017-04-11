package com.appzoneltd.lastmile.customer.features.pickup.recipient;

import android.support.annotation.NonNull;
import android.view.View;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.commands.CashedRequesterCommand;
import com.base.presentation.listeners.OnItemSelectedParam;
import com.base.presentation.listeners.OnTouchParams;
import com.base.usecases.events.ResponseMessage;

/**
 * A Presenter for Recipient details Screen
 * <p/>
 * Created by Wafaa on 9/24/2016.
 */
class RecipientDetailsPresenter extends
        Presenter<RecipientDetailsPresenter, RecipientDetailsViewModel, PickupModel> {

    CashedRequesterCommand cityRequesterCommand;
    CashedRequesterCommand shippingTypesRequesterCommand;
    private final CommandExecutor<Long, OnItemSelectedParam> onItemSelectedExecutor;
    private OnItemSelectedCommandExecutor onItemSelectedCommandExecutor;
    private RecipientDetailsOnResponseHandler recipientDetailsOnResponseHandler;
    private CashedRequesterCommand shippingServiceRequesterCommand;
    private CashedRequesterCommand countryRequesterCommand;
    private OnTouchCommandExecutor onTouchExecutor;

    @Override
    public void preInitialize() {
        onItemSelectedCommandExecutor = new OnItemSelectedCommandExecutor(this, getViewModel());
        shippingServiceRequesterCommand = createShippingServiceRequesterCommand();
        shippingTypesRequesterCommand = createShippingTypesRequesterCommand();
        countryRequesterCommand = createCountryRequesterCommand();
        cityRequesterCommand = createCityRequesterCommand();
        onTouchExecutor = new OnTouchCommandExecutor(getHostActivity());
    }


    RecipientDetailsPresenter(RecipientDetailsViewModel viewModel) {
        super(viewModel);
        super.setUpdater(new RecipientDetailsUpdater());
        this.onItemSelectedExecutor = onItemSelectedCommandExecutor;
        recipientDetailsOnResponseHandler = new RecipientDetailsOnResponseHandler(this,
                getViewModel());
        recipientDetailsOnResponseHandler.invalidateCitySpinner();
    }


    private CashedRequesterCommand createShippingServiceRequesterCommand() {
        return new CashedRequesterCommand(this.getObservable(), R.id.requestShipmentService) {
            @Override
            public boolean isDataCashed() {
                if (getModel().getRecipient().getShipmentServices() != null) {
                    return true;
                }
                return false;
            }

            @Override
            protected boolean request() {
                Event event = new Event.Builder(R.id.requestShipmentService).build();
                getModel().execute(event);
                return true;
            }
        };
    }

    private CashedRequesterCommand createShippingTypesRequesterCommand() {
        return new CashedRequesterCommand(this.getObservable(), R.id.requestShipmentServiceTypes) {
            @Override
            public boolean isDataCashed() {
                if (getModel().getRecipient().getShipmentServiceTypes() != null) {
                    return true;
                }
                return false;
            }

            @Override
            protected boolean request() {
                Event event = new Event.Builder(R.id.requestShipmentServiceTypes).build();
                getModel().execute(event);
                return true;
            }
        };
    }

    private CashedRequesterCommand createCountryRequesterCommand() {
        return new CashedRequesterCommand(this.getObservable(), R.id.requestCountries) {
            @Override
            public boolean isDataCashed() {
                if (getModel().getRecipient().getCountries() != null) {
                    return true;
                }
                return false;
            }

            @Override
            protected boolean request() {
                Event event = new Event.Builder(R.id.requestCountries).build();
                getModel().execute(event);
                return true;
            }
        };
    }

    private CashedRequesterCommand createCityRequesterCommand() {
        return new CashedRequesterCommand(this, R.id.requestCities) {
            @Override
            public boolean isDataCashed() {
                if (getModel().getRecipient().getCities() != null) {
                    return true;
                }
                return false;
            }

            @Override
            protected boolean request() {
                Event event = new Event.Builder(R.id.requestCities).build();
                getModel().execute(event);
                return true;
            }
        };
    }


    @NonNull
    protected CommandExecutor<Long, View> createOnClickCommandExecutor() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command = createOnNextBtnClickedCommand();
        commandExecutor.put((long) R.id.pickup_next_review, command);
        return commandExecutor;
    }

    private Command<View, Void> createOnNextBtnClickedCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                checkErrorsAndInvalidateViews();
                return null;
            }

            private void checkErrorsAndInvalidateViews() {

                if (!getModel().getRecipient().isServiceValid()) {
                    getViewModel().setServiceErrorMSgVisibility(true);
                } else {
                    getViewModel().setServiceErrorMSgVisibility(false);
                }

                if (!getModel().getRecipient().isServiceTypeValid()) {
                    getViewModel().setServiceTypeErrorMSgVisibility(true);
                } else {
                    getViewModel().setServiceTypeErrorMSgVisibility(false);
                }

                if (!getModel().getRecipient().isCountryValid()) {
                    getViewModel().setCountryErrorMSgVisibility(true);
                } else {
                    getViewModel().setCountryErrorMSgVisibility(false);
                }

                if (!getModel().getRecipient().isCityValid()) {
                    getViewModel().setCityErrorMsgVisibility(true);
                } else {
                    getViewModel().setCityErrorMsgVisibility(false);
                }

                getViewModel().setValidationActive(true);
                getViewModel().invalidateViews();
            }
        };
    }

    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnItemSelectedCommand();
        commandExecutor.put((long) R.id.onItemSelected, command);
        command = createOnTouchCommand();
        commandExecutor.put((long) R.id.onTouch, command);
        return commandExecutor;
    }


    private Command<Message, Void> createOnItemSelectedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                long id = message.getId();
                OnItemSelectedParam onItemSelectedParam = message.getContent();
                onItemSelectedExecutor.execute(id, onItemSelectedParam);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnTouchCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                OnTouchParams params = p.getContent();
                onTouchExecutor.execute(p.getId(), params);
                return null;
            }
        };
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createResponseCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.requestShipmentService, shippingServiceRequesterCommand);
        commandExecutor.put((long) R.id.requestShipmentServiceTypes, shippingTypesRequesterCommand);
        commandExecutor.put((long) R.id.requestCountries, countryRequesterCommand);
        commandExecutor.put((long) R.id.requestCities, cityRequesterCommand);
        return commandExecutor;
    }

    CashedRequesterCommand getShippingServiceRequesterCommand() {
        return shippingServiceRequesterCommand;
    }

    CashedRequesterCommand getCountryRequesterCommand() {
        return countryRequesterCommand;
    }

    CashedRequesterCommand getCityRequesterCommand() {
        return cityRequesterCommand;
    }

    CashedRequesterCommand getShippingTypesRequesterCommand() {
        return shippingTypesRequesterCommand;
    }

    RecipientDetailsOnResponseHandler getRecipientDetailsOnResponseHandler() {
        return recipientDetailsOnResponseHandler;
    }

    @Override
    public void onDestroy() {
        if (cityRequesterCommand != null) {
            cityRequesterCommand.clear();
            cityRequesterCommand = null;
        }
        if (shippingServiceRequesterCommand != null) {
            shippingServiceRequesterCommand.clear();
            shippingServiceRequesterCommand = null;
        }
        if (shippingTypesRequesterCommand != null) {
            shippingTypesRequesterCommand.clear();
            shippingTypesRequesterCommand = null;
        }

        if (countryRequesterCommand != null) {
            countryRequesterCommand.clear();
            countryRequesterCommand = null;
        }
    }
}
