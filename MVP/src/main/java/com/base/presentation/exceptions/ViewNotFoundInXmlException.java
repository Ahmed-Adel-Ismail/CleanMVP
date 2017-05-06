package com.base.presentation.exceptions;

import com.base.abstraction.system.AppResources;

/**
 * a {@link RuntimeException} thrown when a declared view is not found in xml
 * <p>
 * Created by Ahmed Adel on 12/18/2016.
 */
public class ViewNotFoundInXmlException extends RuntimeException {

    public ViewNotFoundInXmlException(int viewId) {
        super("[" + AppResources.resourceEntryName(viewId) +
                "] view not found in xml layout");
    }

    public ViewNotFoundInXmlException(String viewName) {
        super("[" + viewName + "] view not found in xml layout");
    }

}
