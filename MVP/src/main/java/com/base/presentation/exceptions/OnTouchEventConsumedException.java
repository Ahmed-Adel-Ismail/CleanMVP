package com.base.presentation.exceptions;

import com.base.abstraction.R;
import com.base.abstraction.exceptions.propagated.DuckableException;

/**
 * An Exception that is thrown when the {@link R.id#onTouchEvent} has been consumed and should not
 * continue to the next consumer
 * <p>
 * Created by Ahmed Adel Ismail on 4/20/2017.
 */

public class OnTouchEventConsumedException extends DuckableException {
}
