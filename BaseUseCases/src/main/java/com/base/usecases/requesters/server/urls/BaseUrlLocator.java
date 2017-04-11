package com.base.usecases.requesters.server.urls;


import com.base.abstraction.commands.Command;
import com.base.abstraction.system.AppResources;

/**
 * An interface to generate base URL for {@link ResourcesUrlLocator} classes ...
 * it creates the base URL from resources, check the documentation of
 * {@link ResourcesUrlLocator#execute(Long)}, it's {@link #execute(Boolean)} will be passed
 * {@code true} to get the testing server URL,  or {@code false} to get the production server URL ...
 * both should be declared in resources files and be retrieved from there through {@link AppResources#string(int)}
 * and it should return the {@code baseURL} value
 * <p>
 * Created by Ahmed Adel on 10/18/2016.
 */
class BaseUrlLocator implements Command<Boolean, String> {

    private final int productionServerUrlResource;
    private final int testingServerUrlResource;

    BaseUrlLocator(int productionServerUrlResource, int testingServerUrlResource) {
        this.testingServerUrlResource = testingServerUrlResource;
        this.productionServerUrlResource = productionServerUrlResource;
    }

    /**
     * createNativeMethod the base URL from resources, check the documentation of
     * {@link ResourcesUrlLocator#execute(Long)},
     * this method should return the {@code baseURL} value
     *
     * @param selectTestUrl pass {@code true} to get the testing server URL,
     *                      or pass {@code false} to get the production server URL ...
     *                      both should be declared in resources files and be
     *                      retrieved from there through {@link AppResources#string(int)}
     * @return the base URL that is the first part of all URL requests
     */
    @Override
    public String execute(Boolean selectTestUrl) {
        return (selectTestUrl) ? AppResources.string(testingServerUrlResource) :
                AppResources.string(productionServerUrlResource);
    }

}
