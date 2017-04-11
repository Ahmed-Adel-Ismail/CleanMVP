package com.appzoneltd.lastmile.driver.features.pickup.model;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.annotations.interfaces.Retry;
import com.base.abstraction.commands.Command;
import com.base.cached.ServerImage;
import com.base.cached.ServerMessage;
import com.base.cached.UploadImageResponse;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.annotations.interfaces.Repository;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.models.Model;
import com.base.presentation.references.Entity;
import com.base.presentation.references.Property;
import com.base.presentation.references.Requester;
import com.base.presentation.references.Validator;
import com.base.usecases.events.RequestMessage;
import com.entities.cached.cancellation.CancellationReasonsGroup;
import com.entities.cached.pakage.PackageDetails;
import com.entities.cached.pakage.PackageLabelsGroup;
import com.entities.cached.pakage.PackageTypesGroup;
import com.entities.cached.pickup.PickupInvoice;
import com.entities.requesters.PickupProcessId;

import java.io.Serializable;

/**
 * the {@link Model} for the Pickup-process feature
 * <p>
 * Created by Ahmed Adel on 12/24/2016.
 */
@Repository(PickupProcessRepository.class)
public class PickupProcessModel extends Model {

    @Load(R.string.INTENT_KEY_PICKUP_PROCESS_ID)
    public final Property<PickupProcessId> pickupProcessId = new Property<>();

    @Retry
    @Sync("packageTypes")
    @JsonRequest(R.id.requestAllPackageTypes)
    public final Entity<PackageTypesGroup> packageTypes;

    @Retry
    @Sync("packageLabels")
    @JsonRequest(R.id.requestAllPackageLabels)
    public final Entity<PackageLabelsGroup> packageLabels;

    @Retry
    @Sync("packageDetails")
    @JsonRequest(R.id.requestPackageDetails)
    public final Entity<PackageDetails> packageDetails;

    @Retry
    @Sync("pickupInvoice")
    @JsonRequest(R.id.requestPickupInvoice)
    public final Entity<PickupInvoice> pickupInvoice;

    @Retry
    @JsonRequest(R.id.requestAllCancellationReasons)
    public final Entity<CancellationReasonsGroup> cancellationReasons;

    @JsonRequest(R.id.requestVerifyPackageDetails)
    public final Entity<ServerMessage> packageDetailsVerification;

    @JsonRequest(R.id.requestConfirmPickupInvoice)
    public final Entity<ServerMessage> pickupInvoiceConfirmation;

    @JsonRequest(R.id.requestCancelRequest)
    public final Entity<ServerMessage> pickupCancellation;

    @Sync("customer-id-image-path")
    public final Property<String> customerIdImagePath;

    @Sync("credit-card-image-path")
    public final Property<String> creditCardImagePath;

    @JsonRequest(R.id.requestConfirmPackageDocuments)
    public final Entity<ServerMessage> packageDocumentsConfirmation;

    @JsonRequest(value = R.id.requestFindImage, repository = GetMethodRepository.class)
    public final Requester<ServerImage> serverImage;

    @JsonRequest(value = R.id.requestUploadImage, repository = PutMethodRepository.class)
    public final Requester<UploadImageResponse> imageUploader;

    public final Validator<Entity<PackageDetails>> packageDetailsValidator;

    public PickupProcessModel() {
        packageTypes = new Entity<>();
        packageLabels = new Entity<>();
        cancellationReasons = new Entity<>();
        packageDetails = new Entity<>();
        packageDetailsValidator = new Validator<>(packageDetails);
        packageDetailsVerification = new Entity<>();

        pickupInvoice = new Entity<>();
        pickupInvoiceConfirmation = new Entity<>();
        pickupCancellation = new Entity<>();

        customerIdImagePath = new Property<>();
        creditCardImagePath = new Property<>();
        packageDocumentsConfirmation = new Entity<>();

        serverImage = new Requester<>();
        imageUploader = new Requester<>();
        new VerificationEntitiesInitializer().execute(this);
        pickupInvoiceConfirmation.onRequestMessage(addPickupProcessIdAsParameter());
        serverImage.onRequestMessage(serverImageOnRequestMessage());
    }

    private Command<Serializable, RequestMessage> addPickupProcessIdAsParameter() {
        return new Command<Serializable, RequestMessage>() {
            @Override
            public RequestMessage execute(Serializable p) {
                return new RequestMessage.Builder().content(pickupProcessId.get()).build();
            }
        };
    }


    @NonNull
    private Command<Serializable, RequestMessage> serverImageOnRequestMessage() {
        return new Command<Serializable, RequestMessage>() {
            @Override
            public RequestMessage execute(Serializable value) {
                return new RequestMessage.Builder().pathVariable(value).build();
            }
        };
    }

    @Override
    public void onClear() {
        super.onClear();
        packageDetailsValidator.clear();
    }
}
