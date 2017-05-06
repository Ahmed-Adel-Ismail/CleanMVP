package com.base.abstraction.bitmaps;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.base.abstraction.commands.Command;

/**
 * A class that is responsible for encoding {@link Bitmap} into a {@code String}
 * <p/>
 * Created by Ahmed Adel on 10/13/2016.
 */
public class BitmapEncoder implements Command<Bitmap, String> {

    private int base64Flags = Base64.NO_WRAP;
    private final BitmapCompressor compressor;

    public BitmapEncoder(@NonNull BitmapCompressor compressor) {
        this.compressor = compressor;
    }

    @Nullable
    @Override
    public String execute(Bitmap bitmap) {
        BitArray bitArray = compressor.execute(bitmap);
        byte[] bytes = bitArray.getByteArray();
        return Base64.encodeToString(bytes, base64Flags);
    }

    public void setBase64Flags(int base64Flags) {
        this.base64Flags = base64Flags;
    }
}
