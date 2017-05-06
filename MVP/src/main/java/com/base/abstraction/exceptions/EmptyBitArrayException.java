package com.base.abstraction.exceptions;

import com.base.abstraction.bitmaps.BitArray;
import com.base.abstraction.exceptions.base.AbstractException;

/**
 * an Exception thrown when an empty {@link BitArray} is created due to corrupted
 * {@link android.graphics.Bitmap} or similar issues
 * <p/>
 * Created by Ahmed Adel on 10/13/2016.
 */
public class EmptyBitArrayException extends AbstractException {


    public EmptyBitArrayException(String message) {
        super("empty byte[] in the " + BitArray.class.getSimpleName() + " is empty ... ");
    }

}
