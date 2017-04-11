package com.appzoneltd.lastmile.customer.features.receipt;

import android.content.Intent;
import android.os.Bundle;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.receipt.models.ReceiptModel;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.abstraction.system.Behaviors;
import com.base.presentation.base.presentation.Presenter;
import com.entities.cached.PaymentMethod;
import com.entities.cached.Receipt;
import com.entities.mocks.MockedPickupServiceTypesGroup;
import com.entities.mocks.MockedReceipt;

/**
 * Created by Wafaa on 12/24/2016.
 */

public class ReceiptPresenter extends Presenter
        <ReceiptPresenter, ReceiptViewModel, ReceiptModel> {

    @Override
    public void initialize(ReceiptViewModel viewModel) {
        super.initialize(viewModel);
        if (App.getInstance().isBehaviorAccepted(Behaviors.MOCKING)) {
            MockedReceipt receipt = createAndReturnMockedReceipt();
            getModel().setReceipt(receipt);
            onUpdateViewModel();
            getViewModel().invalidateViews();
        } else {
            Receipt receipt = getExtra(getHostActivity().getIntent());
            if (receipt != null) {
                getModel().setReceipt(receipt);
                onUpdateViewModel();
                getViewModel().invalidateViews();
            }
        }
    }

    private Receipt getExtra(Intent intent) {
        Receipt receipt = null;
        if (intent != null) {
            Bundle extra = intent.getExtras();
            if (extra != null) {
                receipt = (Receipt) extra.getSerializable(
                        AppResources.string(R.string.INTENT_KEY_RECEIPT));
            }
        }
        return receipt;
    }

    private MockedReceipt createAndReturnMockedReceipt(){
        MockedReceipt receipt = new MockedReceipt("2920282828", "9,Dec,2017"
        , "9:45 AM", new MockedPickupServiceTypesGroup() , "960.00" , "96.00" , "1.001", PaymentMethod.CASH);
        return receipt;
    }




}
