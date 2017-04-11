package com.base.abstraction.bitmaps;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.EmptyBitArrayException;
import com.base.abstraction.logs.Logger;

import java.io.ByteArrayOutputStream;

/**
 * A Class that is responsible for compressing a{@link Bitmap} into a
 * {@link BitArray} ... you can then use {@link BitArray#getByteArray()}
 * <p/>
 * if an error occurred while compressing, an empty {@link BitArray} will be returned
 * <p/>
 * Created by Ahmed Adel on 10/13/2016.
 */
public class BitmapCompressor implements Command<Bitmap, BitArray> {

    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    private int quality = 100;

    /**
     * compress the {@link Bitmap}
     *
     * @param bitmap the {@link Bitmap} to be compressed
     * @return the {@link BitArray}
     * @throws EmptyBitArrayException if the compression failed
     */
    @Override
    public BitArray execute(@NonNull Bitmap bitmap) {
        try {
            return compress(bitmap);
        } catch (Throwable e) {
            return throwEmptyBitArrayException(e);
        }
    }

    private BitArray compress(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, quality, byteArrayOutputStream);
        return new BitArray(byteArrayOutputStream.toByteArray());
    }

    private BitArray throwEmptyBitArrayException(Throwable e) {
        Logger.getInstance().exception(e);
        throw new EmptyBitArrayException("failed to compress bitmap : " + e.getMessage());
    }


    public void setCompressFormat(@NonNull Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
    }

    public BitmapCompressor quality(int quality) {
        this.quality = quality;
        return this;
    }

}
