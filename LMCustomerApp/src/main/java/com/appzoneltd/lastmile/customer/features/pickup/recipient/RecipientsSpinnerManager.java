package com.appzoneltd.lastmile.customer.features.pickup.recipient;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.system.AppResources;
import com.entities.cached.City;
import com.entities.cached.Country;
import com.entities.cached.ShipmentService;
import com.entities.cached.ShipmentServiceTypes;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * manage spinners by createNativeMethod arrays and adapter
 * Created by Wafaa on 9/26/2016.
 */
class RecipientsSpinnerManager {

    private final WeakReference<Activity> activityReference;

    RecipientsSpinnerManager(Activity activity) {
        this.activityReference = new WeakReference<>(activity);
    }

    ArrayAdapter<String> createSpinnerAdapter(String[] spinnerArray) {
        Activity activity = activityReference.get();
        if (activity == null) {
            return null;
        }

        ArrayAdapter<String> spinnerAdapter;
        spinnerAdapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, spinnerArray) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                }
                return view;
            }
        };

        return spinnerAdapter;
    }


    String[] generateShippingServiceSpinnerArray(List<ShipmentService> shipmentServices) {
        String SHIPPING_TITLE = AppResources.string(R.string.service_type_title);
        String[] spinnerArray = new String[]{SHIPPING_TITLE};
        if (shipmentServices != null) {
            int listSize = shipmentServices.size();
            spinnerArray = new String[listSize + 1];
            spinnerArray[0] = SHIPPING_TITLE;
            for (int i = 1; i <= listSize; i++) {
                spinnerArray[i] = shipmentServices.get(i - 1).getService();
            }
        }
        return spinnerArray;
    }

    String[] generateServiceTypesSpinnerArray(List<ShipmentServiceTypes> serviceTypes) {
        String SHIPPING_TIME = AppResources.string(R.string.service_time);
        String[] spinnerArray = new String[]{SHIPPING_TIME};
        if (serviceTypes != null) {
            int listSize = serviceTypes.size();
            spinnerArray = new String[listSize + 1];
            spinnerArray[0] = SHIPPING_TIME;
            for (int i = 1; i <= listSize; i++) {
                spinnerArray[i] = serviceTypes.get(i - 1).getType();
            }
        }
        return spinnerArray;
    }

    String[] generateCountriesSpinnerArray(List<Country> countries) {
        String COUNTRY = AppResources.string(R.string.country);
        String[] spinnerArray = new String[]{COUNTRY};
        if (countries != null) {
            int listSize = countries.size();
            spinnerArray = new String[listSize + 1];
            spinnerArray[0] = COUNTRY;
            for (int i = 1; i <= listSize; i++) {
                spinnerArray[i] = countries.get(i - 1).getName();
            }
        }
        return spinnerArray;
    }

    public String[] generateCitySpinner(List<City> cities) {
        String CITY = AppResources.string(R.string.city);
        String[] spinnerArray = new String[]{CITY};
        if (cities != null) {
            int listSize = cities.size();
            spinnerArray = new String[listSize + 1];
            spinnerArray[0] = CITY;
            for (int i = 1; i <= listSize; i++) {
                spinnerArray[i] = cities.get(i - 1).getName();
            }
        }
        return spinnerArray;
    }

    public long getShippingServiceSelectedId(int index, List<ShipmentService> list) {
        if (index > 0 && list != null) {
            ShipmentService shipmentService = list.get(index - 1);
            return shipmentService.getShipmentServiceId();
        }
        return 0;
    }

    public long getServiceTypeSelectedId(int index, List<ShipmentServiceTypes> list) {
        if (index > 0 && list != null) {
            ShipmentServiceTypes shipmentServiceTypes = list.get(index - 1);
            return shipmentServiceTypes.getShipmentServiceTypeId();
        }
        return 0;
    }

    public long getCountrySelectedId(int index, List<Country> countries) {
        if (index > 0 && countries != null) {
            Country country = countries.get(index - 1);
            return country.getCountryId();
        }
        return 0;
    }

    public long getCitySelectedId(int index, List<City> cities) {
        if (index > 0 && cities != null) {
            City city = cities.get(index - 1);
            return city.getCityId();
        }
        return 0;
    }

}
