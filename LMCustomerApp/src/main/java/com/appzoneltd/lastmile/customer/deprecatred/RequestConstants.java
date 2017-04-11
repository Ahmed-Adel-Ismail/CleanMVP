package com.appzoneltd.lastmile.customer.deprecatred;

import android.content.Context;

import com.base.abstraction.system.App;
import com.base.abstraction.system.Behaviors;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by aateek on 5/7/2015.
 */
@SuppressWarnings("ALL")
public class RequestConstants {

    public static final boolean TEST_MODE = App.getInstance().isBehaviorAccepted(Behaviors.TESTING);

    // push notification constants
    public static final String SERVER_URL = "D:\\adt-bundle-windows-x86_64-20140702\\sdk\\" +
            "extras\\google\\gcm\\samples\\gcm-demo-server\\src\\com\\google\\android\\gcm\\demo\\server\\RegisterServlet";
    public static final String SENDER_ID = "950557936105";
    public static final String TAG = "GCMlastmile";
    public static final String DISPLAY_MESSAGE_ACTION =
            "com.appzoneltd.lastmile.notification.DISPLAY_MESSAGE";
    public static final String EXTRA_MESSAGE = "message";


    public static final String CONTENT_TYPE_KEY = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/json";
    public static final String BODY_KEY = "companyid";
    public static final String BODY_VALUE = "0";
    public static final String AUTHORIZATION_KEY = "Authorization";
    public static final String KYE2 = "Accept";
    public static final String VALUE2 = "application/json";
    public static final String BASE_URl = (TEST_MODE) ? "http://192.168.1.241:8080/" : "http://41.38.174.217:8080/";
    public static final String SHIPMENT_SERVICE_URL = "lookups/shipment_service";
    public static final String CHILD_LOOKUPS_URL = "lookups/childlookups";
    public static final String PACKAGE_TYPE_URL = "packagetypes/findall";
    public static final String COUNTRY_URL = "lookups/COUNTRY_NAMES";
    public static final String AREA_URL = "lookups/childlookups";
    public static final String CITY_URL = "lookups/childlookups";
    public static final String PACKAGE_LABELING = "lookups/package_labeling";

    public static Map<String, String> getHeadersMap()
            throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException,
            BadPaddingException, IllegalBlockSizeException {

        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE);
        headers.put(BODY_KEY, BODY_VALUE);
        return headers;
    }


    public static String getUrl(Context context, String operationId) {
        return (SharedManager.getInstance().getWebServiceURL() + operationId);
    }
}