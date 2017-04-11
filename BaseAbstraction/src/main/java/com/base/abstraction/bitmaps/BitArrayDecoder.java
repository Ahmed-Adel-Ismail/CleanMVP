package com.base.abstraction.bitmaps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;

/**
 * A class that is responsible for decoding a {@link Bitmap} from a {@code BitArray}
 * <p/>
 * Created by Ahmed Adel on 10/13/2016.
 */
public class BitArrayDecoder implements Command<BitArray, Bitmap> {

    @Override
    public Bitmap execute(@NonNull BitArray bitArray) {
        byte[] byteArray = bitArray.getByteArray();
        int byteArrayLength = bitArray.size();
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArrayLength);
    }


}
