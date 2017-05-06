package com.base.usecases.annotations;

import com.base.abstraction.api.usecases.RequestUrlLocator;

/**
 * a {@code null} representation for {@link RequestUrlLocator} interface
 * <p>
 * Created by Ahmed Adel on 1/22/2017.
 */
public class NullUrlLocator implements RequestUrlLocator {

    @Override
    public String execute(Long requestId) {
        return null;
    }
}
