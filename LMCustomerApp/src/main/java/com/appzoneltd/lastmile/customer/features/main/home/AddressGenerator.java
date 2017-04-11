package com.appzoneltd.lastmile.customer.features.main.home;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.abstracts.features.Feature;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Wafaa on 10/13/2016.
 */
abstract class AddressGenerator extends AsyncTask<Void, Void, Void> {

    private static final String NULL = "null";
    private static final String UNNAMED_ROAD = "Unnamed Road";
    private LatLng point;
    private ProgressBar progressBar;
    private WeakReference<Feature> contextWeakReference;
    private AddressGeneratorInterface manualGenerator;
    private AddressGeneratorInterface currentGenerator;

    AddressGenerator(Feature ctx, LatLng point, ProgressBar progressBar) {
        this.contextWeakReference = new WeakReference<>(ctx);
        this.point = point;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
        AddressGeneratorInterface geoCoderGenerator = new
                GeoCoderGenerator(contextWeakReference.get(), point);
        manualGenerator = new ManualGenerator(point);
        currentGenerator = geoCoderGenerator;
    }


    @Override
    protected Void doInBackground(Void... params) {
        Feature context = contextWeakReference.get();
        if (context == null) {
            return null;
        }
        try {
            currentGenerator.doInBackground();
        } catch (Exception ex) {
            currentGenerator = manualGenerator;
            currentGenerator.doInBackground();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (contextWeakReference.get() != null || !contextWeakReference.get().isDestroyed()) {
            onAddressGenerated(currentGenerator.onPost());
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressBar.setVisibility(View.GONE);
    }

    public abstract String onAddressGenerated(Addresses address);


    private static class GeoCoderGenerator implements AddressGeneratorInterface {

        private LatLng point;
        private Context context;
        private List<Address> addressList;

        GeoCoderGenerator(Feature ctx, LatLng point) {
            this.point = point;
            this.context = ctx.getHostActivity();
        }

        @Override
        public void doInBackground() {
            if (context == null) {
                return;
            }
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            addressList = new ArrayList<>();
            try {
                addressList = geocoder.getFromLocation(point.latitude, point.longitude, 4);
            } catch (IOException e) {
                Logger.getInstance().exception(e);
                throw new RuntimeException();
            }
        }

        @Override
        public Addresses onPost() {
            if (context == null || addressList == null) {
                return null;
            }
            Addresses addresses = new Addresses();
            addresses.setFormattedAddress(generateFormattedAddress());
            addresses.setDisplayedAddress(generateDisplayedAddress());
            return addresses;
        }

        private String getFirstItemInAddressLine(int i) {
            return addressList.get(i).getAddressLine(0);
        }

        private String getSecondItemInAddressLine(int i) {
            return addressList.get(i).getAddressLine(1);
        }

        private static boolean isValidLocationName(String name) {
            return !(TextUtils.isEmpty(name) || NULL.equalsIgnoreCase(name)
                    || UNNAMED_ROAD.equalsIgnoreCase(name));
        }

        private String generateFormattedAddress() {
            StringBuilder concatenatedAddress = new StringBuilder();
            int i = 0;
            for (Address address : addressList) {
                concatenatedAddress.append(address.getAddressLine(0));
                if (i == 0) {
                    concatenatedAddress.append(",");
                }
                concatenatedAddress.append(" ");
                i++;
            }
            return concatenatedAddress.toString();
        }

        private String generateDisplayedAddress() {
            StringBuilder concatenatedAddress = new StringBuilder();
            for (Address address : addressList) {
                concatenatedAddress.append(address.getAddressLine(0));
                concatenatedAddress.append(", ");
            }
            concatenatedAddress.deleteCharAt(concatenatedAddress.toString().length() - 2);
            return concatenatedAddress.toString();
        }
    }

    private static class ManualGenerator implements AddressGeneratorInterface {

        private static final String STATUES = "status";
        private static final String OK = "OK";
        private static final String RESULTS = "results";
        private static final String FORMATTED_ADDRESS = "formatted_address";
        private LatLng point;
        private JSONObject jsonObject;
        private String fullAddress = "";

        ManualGenerator(LatLng point) {
            this.point = point;
        }

        @Override
        public void doInBackground() {
            HttpResponse response = requestLocationAddress(point);
            try {
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream stream = entity.getContent();
                    int b;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((b = stream.read()) != -1) {
                        stringBuilder.append((char) b);
                    }
                    try {
                        jsonObject = new JSONObject(stringBuilder.toString());
                        try {
                            String Status = jsonObject.getString(STATUES);
                            if (Status.equalsIgnoreCase(OK)) {
                                JSONArray results = jsonObject.getJSONArray(RESULTS);
                                for (int i = 0; i < results.length(); i++) {
                                    fullAddress = results.getJSONObject(i).getString(FORMATTED_ADDRESS);
                                    if (!TextUtils.isEmpty(fullAddress)
                                            && !isContainedUnnamedRoad(fullAddress)
                                            && isRegexMatched(fullAddress)) {
                                        break;
                                    }
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            fullAddress = getFormattedAddress(fullAddress);
        }

        private String getFormattedAddress(String fullAddress) {
            if (fullAddress.split(",").length >= 2) {
                return fullAddress.split(",")[0] + ", " +
                        fullAddress.split(",")[1];
            }
            return null;
        }

        private HttpResponse requestLocationAddress(LatLng latLng) {
            HttpResponse response = null;
            double latitude = latLng.latitude;
            double longitude = latLng.longitude;
            String query = "http://maps.google.com/maps/api/geocode/json?latlng="
                    + latitude + "," + longitude + "&sensor=false";

            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(query);
            try {
                response = client.execute(httpGet);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        private boolean isContainedUnnamedRoad(String fullAddress) {
            String street = fullAddress.split(",")[0];
            if (street.equalsIgnoreCase(UNNAMED_ROAD)) {
                return true;
            }
            return false;
        }

        private boolean isRegexMatched(String value) {
            String pattern = AppResources.string(R.string.pickup_request_address_regex);
            String street = value.split(",")[0];
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(street);
            return m.matches();
        }

        @Override
        public Addresses onPost() {
            Addresses addresses = new Addresses();
            addresses.setDisplayedAddress(fullAddress);
            addresses.setFormattedAddress(fullAddress);
            return addresses;
        }
    }

    interface AddressGeneratorInterface {
        void doInBackground();

        Addresses onPost();
    }

}
