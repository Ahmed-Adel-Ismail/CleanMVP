package com.base.usecases.requesters.server.urls;

import com.base.abstraction.api.usecases.RequestUrlLocator;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.abstraction.system.Behaviors;

/**
 * the default implementation for {@link RequestUrlLocator}
 * <p>
 * Created by Ahmed Adel on 10/18/2016.
 *
 * @see #execute(Long)
 */
public class ResourcesUrlLocator implements RequestUrlLocator {

    private BaseUrlLocator baseUrlLocator;

    /**
     * createNativeMethod a {@link RequestUrlLocator} that reads it's data from resources
     *
     * @param productionUrlResource the {@code string} resource of the production server
     * @param testingUrlResource    the {@code string} resource of the testing server
     */
    public ResourcesUrlLocator(int productionUrlResource, int testingUrlResource) {
        this.baseUrlLocator = new BaseUrlLocator(productionUrlResource, testingUrlResource);
    }

    /**
     * createNativeMethod the URL to request from server based on the request ID, the default implementation
     * as in {@link ResourcesUrlLocator} :
     * <p>
     * <u>step 1:</u> {@code implement} {@link BaseUrlLocator#execute(Boolean)}
     * to provide the first part of the URL which is common between all URLs (like the ip for example)
     * <p>
     * <u>step 2:</u> declare a {@code resource id} in a resources file with the name of the request
     * <p>
     * <u>step 3:</u> declare a {@code resource string} in a resources file with the same name as
     * the {@code resource id} (mentioned in step 2),
     * and it's value is the URL that will be appended to the base URL (mentioned in step 1)
     * <p>
     * example from a resources file :
     * <p>
     * {@code <string name="baseURL">https://192.168.1.242:</string>}<br>
     * {@code <item name="requestURL" type="id" />}<br>
     * {@code <string name="requestURL">8080/packagetypes/findall</string>}
     *
     * @param requestId the request id that is declared in resources ...
     *                  like {@code R.id.requestURL} in the example documented
     *                  in this method
     * @return the URL generated from resource files
     */
    @Override
    public final String execute(Long requestId) {
        boolean selectTestUrl = App.getInstance().isBehaviorAccepted(Behaviors.TESTING);
        String baseUrl = baseUrlLocator.execute(selectTestUrl);
        int id = Integer.valueOf(requestId.toString());
        String requestUrl = AppResources.stringWithSameId(id);
        return baseUrl + requestUrl;
    }
}
