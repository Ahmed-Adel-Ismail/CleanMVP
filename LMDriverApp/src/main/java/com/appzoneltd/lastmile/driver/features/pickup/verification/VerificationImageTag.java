package com.appzoneltd.lastmile.driver.features.pickup.verification;

import com.base.presentation.references.Property;
import com.base.cached.ServerImage;

public class VerificationImageTag {

    public final Property<ServerImage> serverImage = new Property<>();
    public final Property<Integer> requestCode = new Property<>(0);

}
