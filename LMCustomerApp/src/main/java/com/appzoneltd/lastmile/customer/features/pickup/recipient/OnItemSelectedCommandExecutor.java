package com.appzoneltd.lastmile.customer.features.pickup.recipient;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.presentation.listeners.OnItemSelectedParam;
import com.entities.cached.City;
import com.entities.cached.Country;
import com.entities.cached.ShipmentService;
import com.entities.cached.ShipmentServiceTypes;

import java.util.List;

/**
 * Created by Wafaa on 11/7/2016.
 */

class OnItemSelectedCommandExecutor extends CommandExecutor<Long, OnItemSelectedParam> {

    private RecipientDetailsPresenter presenter;
    private RecipientDetailsViewModel viewModel;
    private RecipientsSpinnerManager spinnerManager;

    OnItemSelectedCommandExecutor(RecipientDetailsPresenter presenter,
                                  RecipientDetailsViewModel viewModel) {
        this.presenter = presenter;
        this.viewModel = viewModel;
        spinnerManager = new RecipientsSpinnerManager(viewModel.getFeature().getHostActivity());
        Command<OnItemSelectedParam, Void> command;
        command = createOnShippingServiceSelectedCommand();
        put((long) R.id.shipping_service, command);
        command = createOnTypeItemSelectedCommand();
        put((long) R.id.shipment_service_type, command);
        command = createOnCountrySelectedCommand();
        put((long) R.id.country, command);
        command = createOnCitySelectedCommand();
        put((long) R.id.city, command);
    }


    private Command<OnItemSelectedParam, Void> createOnShippingServiceSelectedCommand() {
        return new Command<OnItemSelectedParam, Void>() {
            @Override
            public Void execute(OnItemSelectedParam p) {
                List<ShipmentService> services = getModel().getRecipient().getShipmentServices();
                if (services != null) {
                    updateModelAndInvokeRequestServiceTypes(p, services);
                }
                return null;
            }

            private void updateModelAndInvokeRequestServiceTypes(OnItemSelectedParam p,
                                                                 List<ShipmentService> services) {
                String[] shippingServiceArray = getModel().getRecipient().getShippingServiceArray();
                getModel().getRecipient().setServiceSelectedPosition(p.position);
                getModel().getRecipient().setServiceValue(p.position, shippingServiceArray[p.position]);
                getModel().getRecipient().setCurrentlySelectedServiceId
                        (spinnerManager.getShippingServiceSelectedId(p.position,
                                services));
                if (p.position != 0) {
                    resetModelAndViewModelData(p, services);
                }
            }

            private void resetModelAndViewModelData(OnItemSelectedParam p,
                                                    List<ShipmentService> services) {
                viewModel.setServiceErrorMSgVisibility(false);
                viewModel.setValidateShippingService(false);
                if (getModel().getRecipient().getServiceId() !=
                        getModel().getRecipient().getCurrentlySelectedServiceId()) {
                    doResetModelAndRequestShippingTypes(p, services);
                }
            }

            private void doResetModelAndRequestShippingTypes(OnItemSelectedParam p, List<ShipmentService> services) {
                getModel().getRecipient().setShipmentServiceTypes(null);
                getModel().getRecipient().setShippingTypesArray(null);
                viewModel.setShippingServiceTypeSelectedPosition(0);
                getModel().getRecipient().setServiceTypeSelectedPosition(0);
                getModel().getRecipient().setServiceTypeValue(p.position, null);
                getModel().getRecipient().setServiceId(
                        spinnerManager.getShippingServiceSelectedId(p.position,
                                services));
                presenter.shippingTypesRequesterCommand.startRequest();
            }

        };
    }


    private Command<OnItemSelectedParam, Void> createOnTypeItemSelectedCommand() {
        return new Command<OnItemSelectedParam, Void>() {
            @Override
            public Void execute(OnItemSelectedParam p) {
                String[] shippingTypes = getModel().getRecipient().getShippingTypesArray();
                List<ShipmentServiceTypes> shipmentServiceTypes =
                        getModel().getRecipient().getShipmentServiceTypes();
                if (shipmentServiceTypes != null) {
                    getModel().getRecipient().setServiceTypeSelectedPosition(p.position);
                    viewModel.setShippingServiceTypeSelectedPosition(p.position);
                    getModel().getRecipient().setServiceTypeValue(p.position, shippingTypes[p.position]);
                    getModel().getRecipient().setServiceTypeId(
                            spinnerManager.getServiceTypeSelectedId(p.position,
                                    shipmentServiceTypes));
                    if (p.position != 0) {
                        viewModel.setServiceTypeErrorMSgVisibility(false);
                    }
                    viewModel.invalidateViews();
                }
                return null;
            }
        };
    }

    private Command<OnItemSelectedParam, Void> createOnCountrySelectedCommand() {
        return new Command<OnItemSelectedParam, Void>() {
            @Override
            public Void execute(OnItemSelectedParam p) {
                List<Country> countries = getModel().getRecipient().getCountries();
                if (countries != null) {
                    updateModelAndInvalidate(p, countries);
                }
                return null;
            }

            private void updateModelAndInvalidate(OnItemSelectedParam p, List<Country> countries) {
                String[] countriesArray = getModel().getRecipient().getCountriesArray();
                getModel().getRecipient().setCountryValue(p.position, countriesArray[p.position]);
                getModel().getRecipient().setCurrentlySelectedCountryId(
                        spinnerManager.getCountrySelectedId(p.position, countries));
                getModel().getRecipient().setCountrySelectedPosition(p.position);
                viewModel.setCountrySelectedPosition(p.position);
                if (p.position != 0) {
                    updateModelAndRequestCities(p, countries);
                }
            }

            private void updateModelAndRequestCities(OnItemSelectedParam p, List<Country> countries) {
                viewModel.setCountryErrorMSgVisibility(false);
                viewModel.setValidateCountries(false);
                if (getModel().getRecipient().getCountryId() !=
                        getModel().getRecipient().getCurrentlySelectedCountryId()) {
                    resetModelAndViewModelData(p, countries);
                    presenter.cityRequesterCommand.startRequest();
                }
            }

            private void resetModelAndViewModelData(OnItemSelectedParam p, List<Country> countries) {
                getModel().getRecipient().setCities(null);
                getModel().getRecipient().setCitiesArray(null);
                getModel().getRecipient().setCitySelectedPosition(0);
                getModel().getRecipient().setCityValue(p.position, null);
                viewModel.setCitySelectedPosition(0);
                getModel().getRecipient().setCountryId(
                        spinnerManager.getCountrySelectedId(p.position, countries));
            }

        };
    }

    private Command<OnItemSelectedParam, Void> createOnCitySelectedCommand() {
        return new Command<OnItemSelectedParam, Void>() {
            @Override
            public Void execute(OnItemSelectedParam p) {
                List<City> cities = getModel().getRecipient().getCities();
                String[] citiesArray = getModel().getRecipient().getCitiesArray();
                if (cities != null && p.position != 0) {
                    getModel().getRecipient().setCityValue(p.position,
                            citiesArray[p.position]);
                    getModel().getRecipient().setCityId(
                            spinnerManager.getCitySelectedId(p.position, cities));
                    getModel().getRecipient().setCitySelectedPosition(p.position);
                    viewModel.setCitySelectedPosition(p.position);
                    viewModel.setCityErrorMsgVisibility(false);
                    viewModel.invalidateViews();
                }
                return null;
            }
        };
    }

    private PickupModel getModel() {
        return presenter.getModel();
    }
}
