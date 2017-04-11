package com.appzoneltd.lastmile.customer.features.receipt.models;

import com.appzoneltd.lastmile.customer.features.receipt.repo.ReceiptRepository;
import com.base.abstraction.annotations.interfaces.UpdatesHandler;
import com.base.presentation.annotations.interfaces.Repository;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.models.Model;
import com.base.usecases.annotations.ResponsesHandler;
import com.entities.cached.PickupServiceGroup;
import com.entities.cached.Receipt;

/**
 * Created by Wafaa on 12/24/2016.
 */

@UpdatesHandler(ReceiptModelUpdatesHandler.class)
@ResponsesHandler(ReceiptModelResponsesHandler.class)
@Repository(ReceiptRepository.class)
public class ReceiptModel extends Model{

    private PickupServiceGroup pickupServiceGroup;
    @Sync("receipt")
    Receipt receipt;

    public PickupServiceGroup getPickupServiceGroup() {
        return pickupServiceGroup;
    }

    public void setPickupServiceGroup(PickupServiceGroup pickupServiceGroup) {
        this.pickupServiceGroup = pickupServiceGroup;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }
}
