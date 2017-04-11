package com.appzoneltd.lastmile.customer.features.pickup.recipient;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.view.View;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.ValidatorViewModel;
import com.base.presentation.listeners.OnEventListener;
import com.base.presentation.views.validators.EmptyEditTextValidatorCommand;

/**
 * Created by Ahmed Adel on 9/25/2016.
 */
class RecipientDetailsViewModel extends ValidatorViewModel {

    private OnEventListener eventListener;
    private String[] shippingServiceArray;
    private String[] shippingServiceTypesArray;
    private String[] countriesArray;
    private String[] citiesArray;
    private int serviceSelectedPosition;
    private int serviceTypeSelectedPosition;
    private int countrySelectedPosition;
    private int citySelectedPosition;
    private RecipientsSpinnerManager spinnerManager;
    private boolean serviceErrorMSgVisibility;
    private boolean serviceTypeErrorMSgVisibility;
    private boolean countryErrorMSgVisibility;
    private boolean cityErrorMsgVisibility;
    private boolean validateShippingService;
    private boolean validateCountries;


    RecipientDetailsViewModel(ViewBinder viewBinder) {
        super(viewBinder);
        eventListener = new OnEventListener(viewBinder.getHostActivity());
        spinnerManager = new RecipientsSpinnerManager(getFeature().getHostActivity());
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, View> createInvalidateCommands() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        commandExecutor.putAll(new RecipientSpinnersInvalidator(this));
        commandExecutor.putAll(new RecipientDetailsViewsInvalidator(this));
        return commandExecutor;
    }


    @Override
    protected CommandExecutor<Long, Editable> createOnTextChangedCommands() {
        CommandExecutor<Long, Editable> commandExecutor = new CommandExecutor<>();
        Command<Editable, Void> command;
//        = new NameValidatorCommand(R.id.recip_name, this);
        command = new EmptyEditTextValidatorCommand(R.id.recip_name, this);
        commandExecutor.put((long) R.id.recip_name, command);
        command = new EmptyEditTextValidatorCommand(R.id.recipient_phone_number, this);
        commandExecutor.put((long) R.id.recipient_phone_number, command);
        command = new EmptyEditTextValidatorCommand(R.id.recipient_full_address, this);
        commandExecutor.put((long) R.id.recipient_full_address, command);
        return commandExecutor;
    }

    void setShippingServiceArray(String[] shippingServiceArray) {
        this.shippingServiceArray = shippingServiceArray;
    }

    void setShippingServiceTypesArray(String[] shippingServiceTypesArray) {
        this.shippingServiceTypesArray = shippingServiceTypesArray;
    }

    void setCountriesArray(String[] countriesArray) {
        this.countriesArray = countriesArray;
    }

    void setServiceErrorMSgVisibility(boolean serviceErrorMSgVisibility) {
        this.serviceErrorMSgVisibility = serviceErrorMSgVisibility;
    }

    void setServiceTypeErrorMSgVisibility(boolean serviceTypeErrorMSgVisibility) {
        this.serviceTypeErrorMSgVisibility = serviceTypeErrorMSgVisibility;
    }

    void setCountryErrorMSgVisibility(boolean countryErrorMSgVisibility) {
        this.countryErrorMSgVisibility = countryErrorMSgVisibility;
    }


    void setValidateShippingService(boolean validateShippingService) {
        this.validateShippingService = validateShippingService;
    }


    String getRecipientName() {
        return getEditTextValue(R.id.recip_name);
    }

    String getRecipientPhoneNumber() {
        return getEditTextValue(R.id.recipient_phone_number);
    }

    String getRecipientFullAddress() {
        return getEditTextValue(R.id.recipient_full_address);
    }

    String getRecipientNotes() {
        return getEditTextValue(R.id.recipient_notes);
    }

    void setRecipientName(String name) {
        setEditTextValue(R.id.recip_name, name);
    }

    void setRecipientPhoneNumber(String phone) {
        setEditTextValue(R.id.recipient_phone_number, phone);
    }

    void setRecipientAddress(String address) {
        setEditTextValue(R.id.recipient_full_address, address);
    }

    void setRecipientNotes(String notes) {
        setEditTextValue(R.id.recipient_notes, notes);
    }


    void setShippingServiceSelectedPosition(int serviceSelectedPosition) {
        this.serviceSelectedPosition = serviceSelectedPosition;
    }

    void setShippingServiceTypeSelectedPosition(int serviceTypeSelectedPosition) {
        this.serviceTypeSelectedPosition = serviceTypeSelectedPosition;
    }

    void setCountrySelectedPosition(int countrySelectedPosition) {
        this.countrySelectedPosition = countrySelectedPosition;
    }

    void setValidateCountries(boolean validateCountries) {
        this.validateCountries = validateCountries;
    }

    String[] getShippingServiceTypesArray() {
        return shippingServiceTypesArray;
    }

    String[] getCitiesArray() {
        return citiesArray;
    }

    void setCitiesArray(String[] citiesArray) {
        this.citiesArray = citiesArray;
    }

    int getServiceTypeSelectedPosition() {
        return serviceTypeSelectedPosition;
    }

    boolean isServiceTypeErrorMSgVisibility() {
        return serviceTypeErrorMSgVisibility;
    }

    String[] getShippingServiceArray() {
        return shippingServiceArray;
    }

    RecipientsSpinnerManager getSpinnerManager() {
        return spinnerManager;
    }

    int getServiceSelectedPosition() {
        return serviceSelectedPosition;
    }

    OnEventListener getEventListener() {
        return eventListener;
    }

    boolean isValidateShippingService() {
        return validateShippingService;
    }

    boolean isServiceErrorMSgVisibility() {
        return serviceErrorMSgVisibility;
    }

    String[] getCountriesArray() {
        return countriesArray;
    }

    int getCountrySelectedPosition() {
        return countrySelectedPosition;
    }

    boolean isCountryErrorMSgVisibility() {
        return countryErrorMSgVisibility;
    }

    public boolean isValidateCountries() {
        return validateCountries;
    }

    boolean isCityErrorMsgVisibility() {
        return cityErrorMsgVisibility;
    }

    void setCityErrorMsgVisibility(boolean cityErrorMsgVisibility) {
        this.cityErrorMsgVisibility = cityErrorMsgVisibility;
    }

    int getCitySelectedPosition() {
        return citySelectedPosition;
    }

    void setCitySelectedPosition(int citySelectedPosition) {
        this.citySelectedPosition = citySelectedPosition;
    }

    @Override
    public void onDestroy() {
        eventListener = null;
        shippingServiceArray = null;
        shippingServiceTypesArray = null;
        countriesArray = null;
        spinnerManager = null;
    }
}
