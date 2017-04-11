package com.appzoneltd.lastmile.customer.features.pickup.pakage;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SeekBar;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.models.Package;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.ValidatorViewModel;
import com.base.presentation.base.presentation.ViewModel;
import com.base.presentation.listeners.OnEventListener;
import com.base.presentation.views.validators.EmptyEditTextValidatorCommand;
import com.base.presentation.views.validators.StringValidator;
import com.base.presentation.views.validators.ValidStringGenerator;

/**
 * A {@link ViewModel} for the Package details screen
 * <p/>
 * Created by Wafaa on 9/22/2016.
 */
class PackageDetailsViewModel extends ValidatorViewModel {


    private boolean wrapAndLabelChecked;
    private boolean packagingBoxChecked;
    private boolean labelingBoxVisible;
    private Package.PaymentTypes paymentTypes;
    private ImagesInvalidator imagesInvalidator;
    private PackageTypeInvalidator packageTypeInvalidator;
    private OnEventListener eventListener;

    PackageDetailsViewModel(ViewBinder feature) {
        super(feature);
        paymentTypes = Package.PaymentTypes.PICKUP;
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, View> createInvalidateCommands() {
        eventListener = new OnEventListener(getFeature());
        Command<View, Void> command;
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        commandExecutor.putAll(imagesInvalidator = new ImagesInvalidator(this, eventListener));
        commandExecutor.putAll(packageTypeInvalidator = new PackageTypeInvalidator());
        command = createOnInvalidatePackagingBoxCheckedCommand();
        commandExecutor.put((long) R.id.packaging_box, command);
        command = createOnInvalidateWrapAndLabelCheckedCommand();
        commandExecutor.put((long) R.id.wrap_label_package, command);
        command = createOnInvalidatePayAtPickupCheckedCommand();
        commandExecutor.put((long) R.id.rb_pickup, command);
        command = createOnInvalidatePayAtDeliveryCheckedCommand();
        commandExecutor.put((long) R.id.rb_delivery, command);
        commandExecutor.put((long) R.id.custom_label, createEmptyEditTextCommand());
        return commandExecutor;
    }

    @Override
    protected CommandExecutor<Long, Editable> createOnTextChangedCommands() {
        CommandExecutor<Long, Editable> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.package_brief,
                new EmptyEditTextValidatorCommand(R.id.package_brief, this));
        return commandExecutor;
    }

    @NonNull
    private Command<View, Void> createEmptyEditTextCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                EditText packageCustomLabel = (EditText) view;
                if (wrapAndLabelChecked) {
                    packageCustomLabel.setVisibility(View.VISIBLE);
                    String s = new ValidStringGenerator().execute(packageCustomLabel.getText());
                    setError(view.getId(), new StringValidator().execute(s));
                } else {
                    packageCustomLabel.setVisibility(View.GONE);
                    setError(view.getId(), false);
                }
                return null;
            }
        };
    }


    private Command<View, Void> createOnInvalidatePackagingBoxCheckedCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                CheckBox checkBox = (CheckBox) view;
                checkBox.setChecked(packagingBoxChecked);
                return null;
            }
        };
    }

    private Command<View, Void> createOnInvalidateWrapAndLabelCheckedCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                CheckBox checkBox = (CheckBox) view;
                checkBox.setChecked(wrapAndLabelChecked);
                return null;
            }
        };
    }


    private Command<View, Void> createOnInvalidatePayAtPickupCheckedCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                RadioButton radioButton = (RadioButton) view;
                boolean checked = Package.PaymentTypes.PICKUP.equals(paymentTypes);
                radioButton.setChecked(checked);
                return null;
            }
        };
    }

    private Command<View, Void> createOnInvalidatePayAtDeliveryCheckedCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                RadioButton radioButton = (RadioButton) view;
                boolean checked = Package.PaymentTypes.DELIVERY.equals(paymentTypes);
                radioButton.setChecked(checked);
                return null;
            }
        };
    }

    void setLabelingBox(boolean labelingBox) {
        this.labelingBoxVisible = labelingBox;
    }

    String getPackageNicknameValue() {
        return getEditTextValue(R.id.shipment_nickname);
    }

    void setPackageNicknameValue(String packageNickname) {
        setEditTextValue(R.id.shipment_nickname, packageNickname);
    }

    void setPackageDescriptionValue(String packageDescription) {
        setEditTextValue(R.id.package_brief, packageDescription);
    }

    String getPackageDescriptionValue() {
        return getEditTextValue(R.id.package_brief);
    }

    String getCustomLabelValue() {
        return getEditTextValue(R.id.custom_label);
    }

    void setCustomLabelValue(String customLabel) {
        setEditTextValue(R.id.custom_label, customLabel);
    }

    void setFirstPhotoLayoutClicked(boolean firstPhotoLayoutClicked) {
        imagesInvalidator.setFirstPhotoLayoutClicked(firstPhotoLayoutClicked);
    }

    void setPopupWindow(PopupWindow popupWindow) {
        imagesInvalidator.setPopupWindow(popupWindow);
    }


    void setFirstPhotoBitmap(Bitmap firstPhotoBitmap) {
        imagesInvalidator.setFirstPhotoBitmap(firstPhotoBitmap);
    }

    void setSecondPhotoLayoutClicked(boolean secondPhotoLayoutClicked) {
        imagesInvalidator.setSecondPhotoLayoutClicked(secondPhotoLayoutClicked);
    }

    void setDrawOnFirstLayout(boolean drawOnFirstLayout) {
        imagesInvalidator.setDrawOnFirstLayout(drawOnFirstLayout);
    }

    void setDrawOnSecondLayout(boolean drawOnSecondLayout) {
        imagesInvalidator.setDrawOnSecondLayout(drawOnSecondLayout);
    }

    void setShowDeleteSecondPhoto(boolean showDeleteSecondPhoto) {
        imagesInvalidator.setShowDeleteSecondPhoto(showDeleteSecondPhoto);
    }

    void setSecondPhotoBitmap(Bitmap secondPhotoBitmap) {
        imagesInvalidator.setSecondPhotoBitmap(secondPhotoBitmap);
    }

    void setShowDeleteFirstImage(boolean showDeleteFirstImage) {
        imagesInvalidator.setShowDeleteFirstImage(showDeleteFirstImage);
    }

    void setOnSeekBarChanged(SeekBar.OnSeekBarChangeListener onSeekBarChanged) {
        packageTypeInvalidator.setOnSeekBarChanged(onSeekBarChanged);
    }

    void setSeekBarEnabled(boolean seekBarEnabled) {
        packageTypeInvalidator.setSeekBarEnabled(seekBarEnabled);
    }

    boolean isSeekBarEnabled() {
        return packageTypeInvalidator.isSeekBarEnabled();
    }

    void setSeekBarValue(int seekBarValue) {
        packageTypeInvalidator.setSeekBarValue(seekBarValue);
    }

    void setOnImagePopupWindowDismissCommand(Command<?, ?> onImagePopupWindowDismissCommand) {
        imagesInvalidator.setOnImagePopupWindowDismissCommand(onImagePopupWindowDismissCommand);
    }


    boolean isPopupWindowItemSelected() {
        return imagesInvalidator.isPopupWindowItemSelected();
    }

    void setPopupWindowItemSelected(boolean popupWindowItemSelected) {
        imagesInvalidator.setPopupWindowItemSelected(popupWindowItemSelected);
    }

    void setWrapAndLabelChecked(boolean wrapAndLabelChecked) {
        this.wrapAndLabelChecked = wrapAndLabelChecked;
    }

    void setPackagingBoxChecked(boolean packagingBoxChecked) {
        this.packagingBoxChecked = packagingBoxChecked;
    }

    public OnEventListener getEventListener() {
        return eventListener;
    }

    void setPaymentTypes(Package.PaymentTypes paymentTypes) {
        this.paymentTypes = paymentTypes;
    }
}
