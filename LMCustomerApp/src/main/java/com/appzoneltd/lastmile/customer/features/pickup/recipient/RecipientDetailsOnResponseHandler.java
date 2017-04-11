package com.appzoneltd.lastmile.customer.features.pickup.recipient;

import com.entities.cached.City;
import com.entities.cached.Country;
import com.entities.cached.ShipmentService;
import com.entities.cached.ShipmentServiceTypes;

import java.util.List;

/**
 * handel responses of recipient web services
 * Shipping services , shipping types , countries
 * Created by Wafaa on 10/4/2016.
 */
class RecipientDetailsOnResponseHandler {

    private RecipientDetailsPresenter detailsPresenter;
    private RecipientDetailsViewModel viewModel;

    RecipientDetailsOnResponseHandler(RecipientDetailsPresenter presenter, RecipientDetailsViewModel viewModel) {
        this.detailsPresenter = presenter;
        this.viewModel = viewModel;
    }

    void invalidateShippingResponse() {
        List<ShipmentService> shipmentServices =
                detailsPresenter.getModel().getRecipient().getShipmentServices();
        String[] shippingServiceArray = viewModel.getSpinnerManager()
                .generateShippingServiceSpinnerArray(shipmentServices);
        detailsPresenter.getModel().getRecipient().setShippingServiceArray(shippingServiceArray);
        viewModel.setShippingServiceArray(shippingServiceArray);
        viewModel.setValidateShippingService(true);
        viewModel.invalidateViews();
    }

    void invalidateServiceTypesSpinner() {
        List<ShipmentServiceTypes> shipmentServiceTypes =
                detailsPresenter.getModel().getRecipient().getShipmentServiceTypes();
        String[] shippingTypesArray = viewModel.getSpinnerManager()
                .generateServiceTypesSpinnerArray(shipmentServiceTypes);
        detailsPresenter.getModel().getRecipient().setShippingTypesArray(shippingTypesArray);
        viewModel.setShippingServiceTypesArray(shippingTypesArray);
        viewModel.invalidateViews();
    }

    void invalidateCountriesSpinner() {
        List<Country> countries = detailsPresenter.getModel().getRecipient().getCountries();
        String[] countriesArray = viewModel.getSpinnerManager()
                .generateCountriesSpinnerArray(countries);
        detailsPresenter.getModel().getRecipient().setCountriesArray(countriesArray);
        viewModel.setCountriesArray(countriesArray);
        viewModel.setValidateCountries(true);
        viewModel.invalidateViews();
    }

    void invalidateCitySpinner() {
        List<City> cities = detailsPresenter.getModel().getRecipient().getCities();
        String[] citiesArray = viewModel.getSpinnerManager()
                .generateCitySpinner(cities);
        detailsPresenter.getModel().getRecipient().setCitiesArray(citiesArray);
        viewModel.setCitiesArray(citiesArray);
        viewModel.invalidateViews();

    }

}
