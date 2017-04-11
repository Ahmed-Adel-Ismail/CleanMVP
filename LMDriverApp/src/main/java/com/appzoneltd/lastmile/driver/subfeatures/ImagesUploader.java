package com.appzoneltd.lastmile.driver.subfeatures;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.ExecutionThread;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.logs.Logger;
import com.base.presentation.references.CheckedProperty;
import com.base.cached.ServerImagesGroup;
import com.base.cached.UploadImageResponse;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.exceptions.subfeatures.NoImagesToUploadException;
import com.base.presentation.references.Requester;
import com.base.presentation.subfeatures.ImagesUploaderFuture;
import com.base.requesters.UploadImageParam;
import com.base.usecases.events.ResponseMessage;

/**
 * a class that handles multiple images uploading, every
 * <p>
 * Created by Ahmed Adel on 1/7/2017.
 */
public class ImagesUploader implements Command<ServerImagesGroup, Void>, Clearable {

    private final CheckedProperty<AbstractActivity> activity = new CheckedProperty<>();
    private Requester<UploadImageResponse> imageUploader;
    private Command<UploadImageResponse, ?> onNext;
    private Command<ServerImagesGroup, ?> onComplete;
    private Command<Throwable, ?> onException;
    private ServerImagesGroup serverImages;


    public ImagesUploader(AbstractActivity activity, Requester<UploadImageResponse> imageUploader) {
        this.activity.set(activity);
        this.imageUploader = imageUploader;
    }

    public ImagesUploader onComplete(Command<ServerImagesGroup, ?> onComplete) {
        this.onComplete = onComplete;
        return this;
    }

    public ImagesUploader onException(Command<Throwable, ?> onError) {
        this.onException = onError;
        return this;
    }

    public ImagesUploader onNext(Command<UploadImageResponse, ?> onNext) {
        this.onNext = onNext;
        return this;
    }

    @Override
    public Void execute(@NonNull ServerImagesGroup serverImages) {
        this.serverImages = serverImages;
        new ImagesUploaderFuture(activity.get()).execute(serverImages)
                .onThread(ExecutionThread.MAIN)
                .onComplete(imagesUploaderOnComplete())
                .onException(onException());
        return null;
    }

    @NonNull
    private Command<UploadImageParam, Void> imagesUploaderOnComplete() {
        return new Command<UploadImageParam, Void>() {
            @Override
            public Void execute(UploadImageParam p) {
                imageUploader
                        .onNext(modelImageUploaderOnNext())
                        .onComplete(modelImageUploaderOnComplete())
                        .request(p);
                return null;
            }
        };
    }

    private Command<ResponseMessage, Boolean> modelImageUploaderOnNext() {
        return new Command<ResponseMessage, Boolean>() {
            @Override
            public Boolean execute(ResponseMessage responseMessage) {
                if (responseMessage.isSuccessful()) {
                    UploadImageResponse r = responseMessage.getContent();
                    try {
                        serverImages.updateSelectedServerImage(r.getFileId());
                        executeOnNext(r);
                    } catch (Throwable e) {
                        Logger.getInstance().exception(e);
                    }
                }
                return true;
            }
        };
    }


    @NonNull
    private Command<ResponseMessage, Void> modelImageUploaderOnComplete() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage responseMessage) {
                if (responseMessage.isSuccessful()) {
                    ImagesUploader.this.execute(serverImages);
                } else {
                    onException().execute(new RuntimeException("failed to upload image"));
                }
                return null;
            }
        };
    }

    private Command<Throwable, Void> onException() {
        return new Command<Throwable, Void>() {
            @Override
            public Void execute(Throwable e) {

                if (e instanceof NoImagesToUploadException) {
                    e = executeOnComplete();
                    if (e == null) return null;
                }

                executeOnError(e);
                clear();
                return null;
            }
        };
    }

    private void executeOnNext(UploadImageResponse r) {
        if (onNext != null) {
            onNext.execute(r);
        }
    }

    @Nullable
    private Throwable executeOnComplete() {
        Throwable e = null;
        try {
            if (onComplete != null) {
                onComplete.execute(serverImages);
            }
        } catch (Throwable innerException) {
            e = innerException;
        }
        return e;
    }

    private void executeOnError(Throwable e) {
        Logger.getInstance().exception(e);
        if (onException != null) {
            onException.execute(e);
        }

    }

    @Override
    public void clear() {
        imageUploader = null;
        onNext = null;
        onComplete = null;
        onException = null;
        serverImages = null;
    }
}
