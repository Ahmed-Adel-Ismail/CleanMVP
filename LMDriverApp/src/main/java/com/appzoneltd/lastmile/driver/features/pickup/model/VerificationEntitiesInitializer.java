package com.appzoneltd.lastmile.driver.features.pickup.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.commands.Command;
import com.base.presentation.exceptions.references.validators.InvalidNullValueException;
import com.base.presentation.exceptions.references.validators.InvalidValueException;
import com.base.abstraction.logs.Logger;
import com.base.presentation.references.Rule;
import com.base.presentation.references.Entity;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;
import com.entities.cached.pakage.PackageDetails;

import java.io.Serializable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

/**
 * a class to handle all the {@link Command} instances required to load package details
 * <p>
 * Created by Ahmed Adel on 2/5/2017.
 */
class VerificationEntitiesInitializer implements Command<PickupProcessModel, PickupProcessModel> {

    private Disposable packageDetailsDisposable;


    @Override
    public PickupProcessModel execute(PickupProcessModel model) {
        model.packageDetails
                .onRequest(requestAllRelatedEntities(model))
                .onComplete(clearAneNotifyRequestsCompleted(model));

        model.packageDetailsValidator.addRule(packageDetailsValidationRule());
        model.packageDetailsVerification
                .onRequestMessage(verifyPackageDetailsRequestMessage(model))
                .onComplete(requestInvoice(model));

        return model;
    }


    private Command<Entity.RequestParam<PackageDetails>, ?> requestAllRelatedEntities(
            final PickupProcessModel model) {

        return new Command<Entity.RequestParam<PackageDetails>, Void>() {
            @Override
            public Void execute(Entity.RequestParam<PackageDetails> p) {

                if (packageDetailsDisposable != null) {
                    logRequestInProgress();
                    return null;
                }

                Observable<Boolean> labels = createLabelsObservable(model);
                Observable<Boolean> types = createTypesObservable(model);
                Observable<Boolean> cancellationReasons = createCancellationReasonsObservable(model);

                packageDetailsDisposable = Observable
                        .zip(labels, types, cancellationReasons, readyStateZipper())
                        .subscribe(requestPackageDetailsOrClearDisposable(model));


                return null;
            }

            private void logRequestInProgress() {
                Logger.getInstance().error(PickupProcessModel.class, "started requesting package details");
            }
        };
    }


    private Observable<Boolean> createLabelsObservable(final PickupProcessModel model) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                model.packageLabels.onComplete(notifyOnNext(e)).request();
            }
        });
    }

    private Observable<Boolean> createTypesObservable(final PickupProcessModel model) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                model.packageTypes.onComplete(notifyOnNext(e)).request();
            }
        });
    }


    private Observable<Boolean> createCancellationReasonsObservable(final PickupProcessModel model) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                model.cancellationReasons.onComplete(notifyOnNext(e)).request();
            }
        });
    }


    @NonNull
    private Command<ResponseMessage, Void> notifyOnNext(final ObservableEmitter<Boolean> e) {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage p) {
                e.onNext(p.isSuccessful());
                return null;
            }
        };
    }

    @NonNull
    private Function3<Boolean, Boolean, Boolean, Boolean> readyStateZipper() {
        return new Function3<Boolean, Boolean, Boolean, Boolean>() {
            @Override
            public Boolean apply(
                    Boolean labelsReady,
                    Boolean typesReady,
                    Boolean cancellationReasonsReady) throws Exception {
                return labelsReady && typesReady && cancellationReasonsReady;
            }
        };
    }


    @NonNull
    private Rule<Entity<PackageDetails>> packageDetailsValidationRule() {
        return new Rule<Entity<PackageDetails>>() {
            @Override
            public Entity<PackageDetails> execute(Entity<PackageDetails> entity) {
                if (entity.isEmpty()) {
                    throw new InvalidNullValueException();
                }

                PackageDetails packageDetails = entity.get();
                if (packageDetails.getWeight() <= 0) {
                    throw new InvalidValueException("invalid weight");
                }

                if (TextUtils.isEmpty(packageDetails.getDescription())) {
                    throw new InvalidValueException("invalid description");
                }

                if (!packageDetails.isServerImagesUpdatedLocally()) {
                    throw new InvalidValueException("images not updated locally");
                }

                return entity;
            }
        };
    }

    @NonNull
    private Command<Serializable, RequestMessage> verifyPackageDetailsRequestMessage(
            final PickupProcessModel model) {

        return new Command<Serializable, RequestMessage>() {
            @Override
            public RequestMessage execute(Serializable messageContent) {
                RequestMessage.Builder builder = new RequestMessage.Builder();
                model.packageDetails.get().updateLabelIdsFromLabels();
                builder.content(model.packageDetails.get());
                return builder.build();
            }
        };
    }

    private Command<ResponseMessage, Void> requestInvoice(final PickupProcessModel model) {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage responseMessage) {
                if (responseMessage.isSuccessful()) {
                    model.pickupInvoice.request(model.pickupProcessId.get());
                }
                return null;
            }
        };
    }

    @NonNull
    private Consumer<Boolean> requestPackageDetailsOrClearDisposable(
            final PickupProcessModel model) {

        return new Consumer<Boolean>() {
            @Override
            public void accept(Boolean allReady) throws Exception {
                if (allReady) {
                    model.packageDetails.onRequest(null)
                            .onComplete(clearAneNotifyRequestsCompleted(model))
                            .requestFromRepository(model.pickupProcessId.get());
                } else {
                    clear();
                }
            }
        };
    }

    @NonNull
    private Command<ResponseMessage, Void> clearAneNotifyRequestsCompleted(
            final PickupProcessModel model) {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage responseMessage) {
                clear();

                if (responseMessage.isSuccessful()) {
                    buildFinalResponseAndNotify(responseMessage);
                }
                return null;
            }

            private void buildFinalResponseAndNotify(ResponseMessage responseMessage) {
                ResponseMessage newResponseMessage = new ResponseMessage.Builder()
                        .id(R.id.onPickupProcessModelRequestsCompleted)
                        .statusCode(responseMessage.getStatusCode())
                        .content(responseMessage.getContent())
                        .successful(responseMessage.isSuccessful())
                        .build();
                model.notifyOnRepositoryResponse(newResponseMessage);
            }

        };
    }


    private void clear() {
        if (packageDetailsDisposable != null) {
            if (!packageDetailsDisposable.isDisposed()) {
                packageDetailsDisposable.dispose();
            }
            packageDetailsDisposable = null;
        }
    }
}
