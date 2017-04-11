package com.appzoneltd.lastmile.driver.features.pickup.payments;

import com.base.presentation.references.Property;

/**
 * the Object stored as Tag in the cancellation-reasons dialog
 * <p>
 * Created by Ahmed Adel on 1/3/2017.
 */
public class CancellationDialogTag {

    public final Property<Integer> selectedReasonIndex = new Property<>(0);
    public final Property<String> additionalNotes = new Property<>();
}
