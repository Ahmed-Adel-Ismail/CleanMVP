package com.appzoneltd.lastmile.customer.features.pickup.pakage;

import android.view.View;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.system.AppResources;
import com.base.presentation.commands.DelayedCommand;

/**
 * A {@link DelayedCommand} to handle invalidating the value of the Box quantity
 * <p/>
 * Created by Ahmed Adel on 10/12/2016.
 */
class BoxWeightValueInvalidator extends DelayedCommand<PackageTypeInvalidator, TextView> {

    private static final String BOX_TEXT = AppResources.string(R.string.weight_unit);

    public BoxWeightValueInvalidator(PackageTypeInvalidator host) {
        super(host);
    }

    @Override
    protected void delayedExecute(TextView textView) {
        PackageTypeInvalidator invalidator = getHost();
        if (invalidator != null) {
            if (invalidator.isSeekBarEnabled()) {
                updatePositionAndShow(invalidator, textView);
            } else {
                hide(textView);
            }
        }

    }

    private void updatePositionAndShow(PackageTypeInvalidator invalidator, TextView textView) {
        if (invalidator.getSeekBarValue() > 1 && invalidator.getSeekBarValue() < 25) {
            setPackageWeightTextPosition(invalidator, textView);
        } else if (invalidator.getSeekBarValue() == 1 || invalidator.getSeekBarValue() == 25) {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    private void setPackageWeightTextPosition(PackageTypeInvalidator invalidator, TextView textView) {
        textView.setVisibility(View.VISIBLE);
        String packageWeight = invalidator.getSeekBarValue() + BOX_TEXT;
        textView.setText(packageWeight);
        textView.setX(invalidator.getBoxWeightTextViewPosition() - textView.getWidth() / 4);
    }

    private void hide(View textView) {
        textView.setVisibility(View.INVISIBLE);
    }


}
