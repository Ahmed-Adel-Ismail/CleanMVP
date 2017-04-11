package com.appzoneltd.lastmile.customer.deprecatred;

import android.content.Context;
import android.content.SharedPreferences;

import com.base.abstraction.system.App;
import com.base.cached.Token;
import com.entities.cached.Pickup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public class SharedManager {

    private static final String LASTMILE_SHARED_PREFERENCE = "lastmile_shared_preference";
    private static SharedPreferences sharedPreferencesInstance = null;
    private static SharedManager instance = null;
    public static final String URL = "url";
    public static final String CUSTOMER = "customer";
    public static Pickup pickup;
    public static final String CONNECTION_ERROR_TITLE = "error_title";
    public static final String LOGIN_TITLE = "SIGN IN";
    public static final String HOME_TITLE = "Pick Location";
    public static final String PACKAGE_DETAILS_TITLE = "Package Details";
    public static final String RECIPIENT_DETAILS_TITLE = "Recipient Details";
    public static final String SUBMIT_REQUEST_TITLE = "Submit JsonRequest";
    public static final String HEADERS_KEY = "requests_headers";
    public static final String REG_ID = "reg_id";
    public static final int CONNECTION_REQUEST_CODE = 9;


    private SharedManager() {

    }

    public static SharedManager getInstance() {
        if (instance == null) {
            instance = new SharedManager();
        }
        sharedPreferencesInstance = App.getInstance().
                getSharedPreferences(LASTMILE_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return instance;
    }


    public void setWebServiceURL(String url) {
        sharedPreferencesInstance.edit().putString(URL, url).apply();
    }

    public String getWebServiceURL() {
        return sharedPreferencesInstance.getString(URL, RequestConstants.BASE_URl);
    }

    public void setCustomer(Token token) {
        Gson gson = new Gson();
        String json = gson.toJson(token);
        sharedPreferencesInstance.edit().putString(CUSTOMER, json).apply();
    }

    public Token getCustomer() {
        Gson gson = new Gson();
        String json = sharedPreferencesInstance.getString(CUSTOMER, null);
        Type type = new TypeToken<Token>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    public Map<String, String> getHeaders() {
        Gson gson = new Gson();
        String json = sharedPreferencesInstance.getString(HEADERS_KEY, null);
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void setREgId(String s) {
        sharedPreferencesInstance.edit().putString(REG_ID, s).apply();
    }

    public String getRegId() {
        return sharedPreferencesInstance.getString(REG_ID, "");
    }


}
