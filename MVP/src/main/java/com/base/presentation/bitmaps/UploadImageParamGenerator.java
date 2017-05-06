package com.base.presentation.bitmaps;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.base.abstraction.bitmaps.BitmapCompressor;
import com.base.abstraction.bitmaps.BitmapEncoder;
import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.ExecutionThread;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.presentation.references.CheckedProperty;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.entities.requesters.UploadImageParam;
import com.bumptech.glide.Glide;

/**
 * a class that converts local images on disk into a {@link UploadImageParam} Object to
 * be sent to the server ... it's {@link #execute(String)} method throws
 * {@link UnsupportedOperationException} if an error happened while creating the
 * {@link Bitmap} from the file, also it may throw {@link CheckedReferenceClearedException}
 * if the {@link AbstractActivity} is destroyed
 * <p>
 * Created by Ahmed Adel on 1/6/2017.
 */
public class UploadImageParamGenerator implements Command<String, Future<UploadImageParam>> {

    private static final String IMG_EXTENSION = "jpeg";
    private static final String IMG_PREFIX = "Image_";
    private static final String IMG_SEPARATOR = "_";

    private CheckedProperty<AbstractActivity> activity = new CheckedProperty<>();

    public UploadImageParamGenerator(AbstractActivity activity) {
        this.activity.set(activity);
    }

    @Override
    public Future<UploadImageParam> execute(final String filePath)
            throws UnsupportedOperationException, CheckedReferenceClearedException {

        Future<UploadImageParam> resultFuture = new Future<>();
        resultFuture.onThread(ExecutionThread.CURRENT);

        Future<Future<UploadImageParam>> workerFuture = new Future<>();
        workerFuture.onThread(ExecutionThread.BACKGROUND);
        workerFuture.setResult(resultFuture);
        workerFuture.onComplete(workerFutureOnComplete(filePath));

        return resultFuture;
    }

    @NonNull
    private Command<Future<UploadImageParam>, Void> workerFutureOnComplete(final String filePath) {
        return new Command<Future<UploadImageParam>, Void>() {
            @Override
            public Void execute(Future<UploadImageParam> resultFuture) {
                Bitmap bmp;
                try {
                    bmp = Glide.with(activity.get()).load("file://" + filePath).asBitmap().into(-1, -1).get();
                } catch (Throwable e) {
                    resultFuture.setException(e);
                    return null;
                }
                String encodedString = new BitmapEncoder(new BitmapCompressor().quality(50))
                        .execute(bmp);
                UploadImageParam p = new UploadImageParam();
                p.setExtension(IMG_EXTENSION);
                p.setName(IMG_PREFIX + IMG_SEPARATOR + System.currentTimeMillis());
                p.setBase64ByteArray(encodedString);

//                bmp.recycle();


                resultFuture.setResult(p);
                return null;
            }
        };
    }
}
