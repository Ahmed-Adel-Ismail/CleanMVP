package com.appzoneltd.lastmile.driver.features.pickup.verification;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.RxCommand;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.logs.Logger;
import com.base.cached.ServerImage;
import com.base.presentation.exceptions.references.RequestInProgressException;
import com.base.usecases.events.ResponseMessage;
import com.entities.cached.pakage.PackageDetails;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * a class that keeps trying to load images in {@link PackageDetails}, then it stops
 * <p>
 * Created by Ahmed Adel on 1/5/2017.
 */
class ImagesLoader implements
        Clearable,
        Command<VerificationPresenter, ImagesLoader> {

    private PickupProcessModel model;
    private Disposable disposable;


    @Override
    public ImagesLoader execute(VerificationPresenter verificationPresenter) {
        if (disposable == null) {
            model = verificationPresenter.getModel();
            disposable = createDisposable();
        }
        return this;
    }


    @NonNull
    private Disposable createDisposable() {
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext());
    }

    @NonNull
    private RxCommand<Long> onNext() {
        return new RxCommand<Long>() {
            @Override
            public Void execute(Long p) {

                if (model.packageDetails.isEmpty()) {
                    return logEmptyPackageDetailsAndExit();
                }

                if (model.packageDetails.get().isServerImagesUpdatedLocally()) {
                    return logAllImagesUpdatedAndExit();
                }

                try {
                    requestNextPackageDetailsServerImage();
                } catch (RequestInProgressException e) {
                    Logger.getInstance().exception(e);
                } catch (UnsupportedOperationException e) {
                    Logger.getInstance().error(ImagesLoader.class, "all server image ids updated");
                    stopImagesLoader();
                }
                return null;

            }
        };
    }

    private Void logEmptyPackageDetailsAndExit() {
        Logger.getInstance().error(ImagesLoader.class, "package details still empty");
        return null;
    }

    private Void logAllImagesUpdatedAndExit() {
        Logger.getInstance().error(ImagesLoader.class, "all server images updated @ package details");
        stopImagesLoader();
        return null;
    }

    private void requestNextPackageDetailsServerImage() {
        PackageDetails packageDetails = model.packageDetails.get();
        model.serverImage.onNext(entityOnNext()).request(packageDetails.pollNonUpdatedImageId());
    }

    @NonNull
    private Command<ResponseMessage, Boolean> entityOnNext() {
        return new Command<ResponseMessage, Boolean>() {
            @Override
            public Boolean execute(ResponseMessage responseMessage) {
                if (responseMessage.isSuccessful() && !model.packageDetails.isEmpty()) {
                    ServerImage serverImage = responseMessage.getContent();
                    int index = model.packageDetails.get().getServerImagesCount();
                    model.packageDetails.get().addServerImage(index, serverImage);
                }
                return true;
            }
        };
    }

    @Override
    public void clear() {
        clearImagesLoader();
    }

    private void clearImagesLoader() {
        if (disposable != null) {
            stopImagesLoader();
        }
    }

    private void stopImagesLoader() {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }


}
