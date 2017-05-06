package com.base.presentation.exceptions;

import com.base.abstraction.exceptions.propagated.DuckableException;

/**
 * a {@link DuckableException} that is thrown to notify the caller that
 * the error handeling is completed and requires the parent to take no
 * action
 * <p>
 * Created by Ahmed Adel Ismail on 4/20/2017.
 */
public class OnMediaPlayerErrorStoppedException extends
        DuckableException {
}
