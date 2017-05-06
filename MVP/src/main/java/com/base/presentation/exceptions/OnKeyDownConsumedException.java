package com.base.presentation.exceptions;

import com.base.abstraction.exceptions.propagated.DuckableException;
import com.base.presentation.base.abstracts.features.AbstractActivity;

/**
 * an exception thrown when the {@code onKeyDown()} method should stop
 * and return {@code true} in an {@link AbstractActivity}
 * <p>
 * Created by Ahmed Adel Ismail on 4/20/2017.
 */

public class OnKeyDownConsumedException extends DuckableException {
}
