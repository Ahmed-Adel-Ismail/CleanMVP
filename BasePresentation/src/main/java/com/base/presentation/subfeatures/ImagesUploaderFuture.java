package com.base.presentation.subfeatures;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.logs.Logger;
import com.base.presentation.references.CheckedProperty;
import com.base.cached.ServerImage;
import com.base.cached.ServerImagesGroup;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.bitmaps.UploadImageParamGenerator;
import com.base.presentation.exceptions.subfeatures.NoImagesToUploadException;
import com.base.presentation.interfaces.ActivityHosted;
import com.base.requesters.UploadImageParam;

import static com.base.cached.ServerImage.FLAG_SELECTED;

/**
 * a class responsible for uploading the {@link ServerImage} that does not have a file id,
 * if all the images have file id, the {@link Future} will hold {@link NoImagesToUploadException}
 * <p>
 * if any error occurred, it will be passed to {@link Future#setException(Throwable)}
 * <p>
 * if the {@link ServerImage} needs to be uploaded, a {@link UploadImageParam} will be passed to
 * {@link Future#setResult(Object)} method
 * <p>
 * Created by Ahmed Adel on 1/6/2017.
 */
public class ImagesUploaderFuture implements Command<ServerImagesGroup, Future<UploadImageParam>> {


    protected final CheckedProperty<AbstractActivity> activity = new CheckedProperty<>();

    public ImagesUploaderFuture(ActivityHosted activityHosted) {
        this.activity.set(activityHosted.getHostActivity());
    }

    @Override
    public Future<UploadImageParam> execute(ServerImagesGroup serverImagesGroup) {
        Future<UploadImageParam> future = new Future<>();

        for (ServerImage serverImage : serverImagesGroup.values()) {
            if (isUploadingImageStarted(serverImage, future)) return future;
        }
        future.setException(new NoImagesToUploadException());
        return future;
    }


    private boolean isUploadingImageStarted(ServerImage image, Future<UploadImageParam> future) {
        if (!image.hasFileId() && image.getFlags() != FLAG_SELECTED) {
            upload(image, future);
            return true;
        }
        return false;
    }

    private void upload(ServerImage serverImage, Future<UploadImageParam> future) {
        serverImage.setFlags(FLAG_SELECTED);
        String uri = serverImage.getUri();
        try {
            UploadImageParamGenerator gen = new UploadImageParamGenerator(activity.get());
            gen.execute(uri).onComplete(onComplete(future)).onException(onException(future));
        } catch (CheckedReferenceClearedException e) {
            Logger.getInstance().exception(e);
        } catch (Throwable e) {
            future.setException(e);
        }
    }

    @NonNull
    private Command<Throwable, Void> onException(final Future<UploadImageParam> future) {
        return new Command<Throwable, Void>() {
            @Override
            public Void execute(Throwable p) {
                future.setException(p);
                return null;
            }
        };
    }

    @NonNull
    private Command<UploadImageParam, Void> onComplete(final Future<UploadImageParam> future) {
        return new Command<UploadImageParam, Void>() {
            @Override
            public Void execute(UploadImageParam p) {
                future.setResult(p);
                return null;
            }
        };
    }


}
