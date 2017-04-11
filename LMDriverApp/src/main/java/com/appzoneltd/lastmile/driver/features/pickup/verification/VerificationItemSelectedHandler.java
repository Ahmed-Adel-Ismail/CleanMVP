package com.appzoneltd.lastmile.driver.features.pickup.verification;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.presentation.base.presentation.PresenterItemSelectedHandler;
import com.base.presentation.listeners.OnItemSelectedParam;
import com.entities.cached.pakage.PackageType;

class VerificationItemSelectedHandler extends PresenterItemSelectedHandler
        <VerificationPresenter, VerificationViewModel, PickupProcessModel> {

    @Executable(R.id.screen_pickup_process_verify_package_type_details_spinner)
    void onPackageTypeSelected(OnItemSelectedParam params) {
        PackageType type = getModel().packageTypes.get().getByIndex(params.position);
        getModel().packageDetails.get().setType(type);
        updateViewModel();
    }

}
