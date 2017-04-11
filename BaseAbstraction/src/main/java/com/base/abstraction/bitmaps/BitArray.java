package com.base.abstraction.bitmaps;

import android.support.annotation.NonNull;

import com.base.abstraction.interfaces.Clearable;

import java.io.Serializable;

/**
 * A class that wraps around a {@code byte[]} that forms an image, and let this
 * {@code byte[]} be Declared as an Object type in Generic classes
 * <p/>
 * as for the naming conventions, this class should have been names {@code BitArray}, but since
 * it is used only with {@link android.graphics.Bitmap Bitmaps}, it will use ths same naming style
 * <p/>
 * Created by Ahmed Adel on 10/13/2016.
 *
 * @see BitArrayDecoder
 */
public class BitArray implements Serializable, Clearable {

    private byte[] byteArray;

    /**
     * create a {@link BitArray} instance
     *
     * @param byteArray the {@code byte[]} that holds an image
     */
    public BitArray(@NonNull byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public boolean isEmpty() {
        return size() != 0;
    }

    int size() {
        return byteArray != null ? byteArray.length : 0;
    }

    @Override
    public void clear() {
        byteArray = null;
    }
}
