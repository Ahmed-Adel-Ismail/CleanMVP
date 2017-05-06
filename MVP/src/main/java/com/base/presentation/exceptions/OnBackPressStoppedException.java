package com.base.presentation.exceptions;

import com.base.abstraction.exceptions.propagated.DuckableException;

/**
 * An Exception that is thrown when the ApplicationFlow should stop and onBackPressed() method
 * <b>should not</b> be called
 * <p/>
 * Created by Ahmed Adel on 9/5/2016.
 */
public class OnBackPressStoppedException extends DuckableException {

}
