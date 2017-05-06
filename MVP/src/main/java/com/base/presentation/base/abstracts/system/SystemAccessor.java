package com.base.presentation.base.abstracts.system;

import com.base.presentation.system.SystemServices;

/**
 * implement this interface if your Class will need to access System related stuff, like
 * Manifest or System services for example
 * <p/>
 * Created by Ahmed Adel on 10/12/2016.
 */
public interface SystemAccessor {

    /**
     * get the {@link SystemServices} for the value context
     *
     * @return the {@link SystemServices}
     */
    SystemServices getSystemServices();


}
