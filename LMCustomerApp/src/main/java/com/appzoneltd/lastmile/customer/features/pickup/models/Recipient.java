package com.appzoneltd.lastmile.customer.features.pickup.models;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.host.Titleable;
import com.base.abstraction.interfaces.Validateable;
import com.base.abstraction.converters.SerializableObject;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.system.AppResources;
import com.base.presentation.views.validators.StringValidator;
import com.base.presentation.views.validators.ValidStringGenerator;
import com.entities.cached.City;
import com.entities.cached.Country;
import com.entities.cached.ShipmentService;
import com.entities.cached.ShipmentServiceTypes;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that represents the model related data for the Recipient details
 * <p/>
 * Created by Ahmed Adel on 9/27/2016.
 */
public class Recipient extends SerializableObject implements
        Clearable,
        Validateable,
        Titleable {

    private static final String RGX_NAME;
    private static final String SHIPPING_SERVICE_TYPES_TITLE;
    private long serviceId;
    private long serviceTypeId;
    private long countryId;
    private long cityId;
    private long currentlySelectedCountryId;
    private long currentlySelectedServiceId;
    private int serviceSelectedPosition;
    private int serviceTypeSelectedPosition;
    private int countrySelectedPosition;
    private int citySelectedPosition;
    private String serviceValue;
    private String serviceTypeValue;
    private String countryValue;
    private String cityValue;
    private List<Country> countries;
    private List<City> cities;
    private List<ShipmentService> shipmentServices;
    private List<ShipmentServiceTypes> shipmentServiceTypes;
    private String[] shippingServiceArray;
    private String[] shippingTypesArray;
    private String[] countriesArray;
    private String[] citiesArray;
    private String name;
    private String phoneNumber;
    private String fullAddress;
    private String notes;

    static {
        RGX_NAME = AppResources.string(R.string.pickup_request_recipient_name_regex);
        SHIPPING_SERVICE_TYPES_TITLE = AppResources.string(R.string.service_type_title);
    }

    public Recipient() {
        shippingServiceArray = new String[]{SHIPPING_SERVICE_TYPES_TITLE};
        shippingTypesArray = new String[]{AppResources.string(R.string.service_time)};
        countriesArray = new String[]{AppResources.string(R.string.country)};
        citiesArray = new String[]{AppResources.string(R.string.city)};
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public List<ShipmentService> getShipmentServices() {
        return shipmentServices;
    }

    public void setShipmentServices(List<ShipmentService> shipmentServices) {
        this.shipmentServices = shipmentServices;
    }

    public List<ShipmentServiceTypes> getShipmentServiceTypes() {
        return shipmentServiceTypes;
    }

    public void setShipmentServiceTypes(List<ShipmentServiceTypes> shipmentServiceTypes) {
        this.shipmentServiceTypes = shipmentServiceTypes;
    }


    public long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public void setCountryId(long countryId) {
        this.countryId = countryId;
    }

    public long getCountryId() {
        return countryId;
    }

    public String getServiceValue() {
        return serviceValue;
    }

    public void setServiceValue(int pos, String serviceValue) {
        if (pos != 0) {
            this.serviceValue = serviceValue;
        }
    }

    public String getServiceTypeValue() {
        return serviceTypeValue;
    }

    public void setServiceTypeValue(int pos, String serviceTypeValue) {
        if (pos != 0) {
            this.serviceTypeValue = serviceTypeValue;
        }
    }

    public void setCountryValue(int pos, String countryValue) {
        if (pos != 0) {
            this.countryValue = countryValue;
        }
    }

    public void setCityValue(int pos, String cityValue) {
        if (pos != 0) {
            this.cityValue = cityValue;
        }
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name.trim();
        } else {
            this.name = null;
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return countryValue + ", " + cityValue + ", " + fullAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String[] getShippingServiceArray() {
        return shippingServiceArray;
    }

    public void setShippingServiceArray(String[] shippingServiceArray) {
        this.shippingServiceArray = shippingServiceArray;
    }

    public String[] getShippingTypesArray() {
        return shippingTypesArray;
    }

    public void setShippingTypesArray(String[] shippingTypesArray) {
        this.shippingTypesArray = shippingTypesArray;
    }

    public String[] getCountriesArray() {
        return countriesArray;
    }

    public void setCountriesArray(String[] countriesArray) {
        this.countriesArray = countriesArray;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public int getServiceSelectedPosition() {
        return serviceSelectedPosition;
    }

    public void setServiceSelectedPosition(int serviceSelectedPosition) {
        this.serviceSelectedPosition = serviceSelectedPosition;
    }

    public int getServiceTypeSelectedPosition() {
        return serviceTypeSelectedPosition;
    }

    public void setServiceTypeSelectedPosition(int serviceTypeSelectedPosition) {
        this.serviceTypeSelectedPosition = serviceTypeSelectedPosition;
    }

    public int getCountrySelectedPosition() {
        return countrySelectedPosition;
    }

    public void setCountrySelectedPosition(int countrySelectedPosition) {
        this.countrySelectedPosition = countrySelectedPosition;
    }

    public String[] getCitiesArray() {
        return citiesArray;
    }

    public void setCitySelectedPosition(int citySelectedPosition) {
        this.citySelectedPosition = citySelectedPosition;
    }

    public void setCitiesArray(String[] citiesArray) {
        this.citiesArray = citiesArray;
    }

    public long getCurrentlySelectedServiceId() {
        return currentlySelectedServiceId;
    }

    public void setCurrentlySelectedServiceId(long currentlySelectedServiceId) {
        this.currentlySelectedServiceId = currentlySelectedServiceId;
    }

    @Override
    public boolean isValid() {
        return isServiceValid() && isServiceTypeValid()
                && isCountryValid() && isNameValid()
                && isPhoneValid() && isAddressValid()
                && isCityValid();
    }

    public boolean isServiceValid() {
        serviceValue = new ValidStringGenerator().execute(serviceValue);
        return !(new StringValidator().execute(serviceValue))
                && !SHIPPING_SERVICE_TYPES_TITLE.equals(serviceValue);
    }


    public long getCurrentlySelectedCountryId() {
        return currentlySelectedCountryId;
    }

    public void setCurrentlySelectedCountryId(long currentlySelectedCountryId) {
        this.currentlySelectedCountryId = currentlySelectedCountryId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public int getCitySelectedPosition() {
        return citySelectedPosition;
    }


    public boolean isServiceTypeValid() {
        serviceTypeValue = new ValidStringGenerator().execute(serviceTypeValue);
        return !new StringValidator().execute(serviceTypeValue);
    }

    public boolean isCountryValid() {
        countryValue = new ValidStringGenerator().execute(countryValue);
        return !(new StringValidator().execute(countryValue));
    }

    public boolean isCityValid() {
        cityValue = new ValidStringGenerator().execute(cityValue);
        return !new StringValidator().execute(cityValue);
    }

    public boolean isNameValid() {
        name = new ValidStringGenerator().execute(name);
        return !new StringValidator().execute(name) && isRegexMatched(name, RGX_NAME);
    }

    public boolean isPhoneValid() {
        phoneNumber = new ValidStringGenerator().execute(phoneNumber);
        return !new StringValidator().execute(phoneNumber);
    }


    private boolean isRegexMatched(String value, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(value);
        return m.matches();
    }

    public boolean isAddressValid() {
        fullAddress = new ValidStringGenerator().execute(fullAddress);
        return !new StringValidator().execute(fullAddress);
    }

    @Override
    public void clear() {
        countries = null;
        shipmentServices = null;
        shipmentServiceTypes = null;
    }

    @Override
    public String getTile() {
        return AppResources.string(R.string.recipient_details_title);
    }
}
