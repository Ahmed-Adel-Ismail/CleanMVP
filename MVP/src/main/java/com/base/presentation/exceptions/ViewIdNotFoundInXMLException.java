package com.base.presentation.exceptions;

import com.base.abstraction.system.AppResources;

/**
 * a {@link RuntimeException} thrown when a given view id is not found in xml
 * <p>
 * Created by Ahmed Adel on 12/18/2016.
 */
public class ViewIdNotFoundInXMLException extends RuntimeException {

    public ViewIdNotFoundInXMLException(int viewId) {
        super("[" + AppResources.resourceEntryName(viewId) +
                "] view not found in xml layout");
    }

}
