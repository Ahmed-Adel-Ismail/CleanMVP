package com.appzoneltd.lastmile.customer.features.pickup.models;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.host.Titleable;
import com.base.abstraction.interfaces.Validateable;
import com.base.abstraction.bitmaps.ImageSaver;
import com.base.abstraction.converters.SerializableObject;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.system.AppResources;
import com.base.presentation.views.validators.StringValidator;
import com.base.presentation.views.validators.ValidStringGenerator;
import com.entities.cached.PackageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * a Class holds data related to Package Details
 * <p/>
 * Created by Ahmed Adel on 9/27/2016.
 */
public class Package extends SerializableObject implements
        Clearable,
        Validateable,
        Titleable {

    private static final String DIR_NAME = "packageImages";
    private static final String FILE_NAME_FIRST_IMG = "firstImage.png";
    private static final String FILE_NAME_SECOND_IMG = "secondImage.png";
    private int boxWeight;
    private String description;
    private transient Bitmap firstPhotoBitmap;
    private transient Bitmap secondPhotoBitmap;
    private String additionalServices;
    private String nickname;
    private boolean wrapAndLabel;
    private boolean packagingBox;
    private String labelMessage;
    private boolean boxSelected;
    private PaymentTypes paymentType;
    private List<PackageType> packageTypes;
    private PackageType packageType;
    private int boxWeightTextViewPosition;
    @NonNull
    private String packageTypeText;
    private boolean firstPhotoLayoutClicked;
    private boolean secondPhotoLayoutClicked;
    private boolean firstRemoveImageClicked;
    private boolean secondRemoveImageClicked;
    private boolean showImageErrorMsg;
    private boolean showCustomLabelErrorMsg;
    private boolean drawOnFirstLayout;
    private boolean drawOnSecondLayout;
    private boolean showDeleteFirstPhoto;
    private boolean showDeleteSecondPhoto;
    private boolean packageTypesLoaded;

    Package() {
        boxWeight = 12;
        paymentType = PaymentTypes.PICKUP;
        packageTypeText = PackageTypes.DOCUMENT.text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getFirstPhotoBitmap() {
        return firstPhotoBitmap;
    }

    public void setFirstPhotoBitmap(Bitmap firstPhotoBitmap) {
        this.firstPhotoBitmap = firstPhotoBitmap;
    }

    public String getLabelMessage() {
        return new ValidStringGenerator().execute(labelMessage);
    }

    public void setLabelMessage(String labelMessage) {
        this.labelMessage = labelMessage;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean hasPackagingBox() {
        return packagingBox;
    }

    public void setPackagingBox(boolean packagingBox) {
        this.packagingBox = packagingBox;
    }

    public int getBoxWeight() {
        return boxWeight;
    }

    public void setBoxWeight(int boxWeight) {
        this.boxWeight = boxWeight;
    }

    @NonNull
    public PaymentTypes getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(@NonNull PaymentTypes paymentType) {
        this.paymentType = paymentType;
    }

    public Bitmap getSecondPhotoBitmap() {
        return secondPhotoBitmap;
    }

    public void setSecondPhotoBitmap(Bitmap secondPhotoBitmap) {
        this.secondPhotoBitmap = secondPhotoBitmap;
    }

    PackageType getPackageType() {
        return packageType;
    }

    public boolean isWrapAndLabel() {
        return wrapAndLabel;
    }

    public void setWrapAndLabel(boolean wrapAndLabel) {
        this.wrapAndLabel = wrapAndLabel;
    }

    void setPackageTypes(List<PackageType> packageTypes) {
        this.packageTypes = packageTypes;
        selectPackageTypeAndWeight();
    }

    public void updatePackageTypeTextAndWeight(boolean boxSelected) {
        if (boxSelected) {
            packageTypeText = PackageTypes.BOX.text;
        } else {
            packageTypeText = PackageTypes.DOCUMENT.text;
        }

        if (packageTypes != null && !packageTypes.isEmpty()) {
            selectPackageTypeAndWeight();
        }
    }

    private void selectPackageTypeAndWeight() {
        for (PackageType packageType : packageTypes) {
            if (packageTypeText.equals(packageType.getPackageType())) {
                this.packageType = packageType;
            }
        }
    }

    public List<PackageType> getPackageTypes() {
        return packageTypes;
    }

    public int getExpectedWeight() {
        if (packageType != null) {
            return (int) packageType.getExpectedWeight();
        } else {
            return 0;
        }
    }

    @NonNull
    public String getPackageTypeText() {
        return packageTypeText;
    }


    int getImagesCount() {
        int i = 0;
        i = (firstPhotoBitmap != null) ? ++i : i;
        i = (secondPhotoBitmap != null) ? ++i : i;
        return i;
    }

    @Override
    public boolean isValid() {
        return isDescriptionValid() && isImageValid() && isCustomLabelValid() && isPackageTypesLoaded();
    }

    private boolean isDescriptionValid() {
        String s = new ValidStringGenerator().execute(description);
        return !(new StringValidator().execute(s));
    }

    public boolean isImageValid() {
        return firstPhotoBitmap != null;
    }

    public boolean isCustomLabelValid() {
        String s = new ValidStringGenerator().execute(labelMessage);
        return (!wrapAndLabel) || !(new StringValidator().execute(s));
    }

    public String getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(String additionalServices) {
        this.additionalServices = additionalServices;
    }

    public boolean isFirstPhotoLayoutClicked() {
        return firstPhotoLayoutClicked;
    }

    public void setFirstPhotoLayoutClicked(boolean firstPhotoLayoutClicked) {
        this.firstPhotoLayoutClicked = firstPhotoLayoutClicked;
    }

    public boolean isFirstRemoveImageClicked() {
        return firstRemoveImageClicked;
    }

    public void setFirstRemoveImageClicked(boolean firstRemoveImageClicked) {
        this.firstRemoveImageClicked = firstRemoveImageClicked;
    }

    public int getBoxWeightTextViewPosition() {
        return boxWeightTextViewPosition;
    }

    public void setBoxWeightTextViewPosition(int boxWeightTextViewPosition) {
        this.boxWeightTextViewPosition = boxWeightTextViewPosition;
    }

    public boolean isSecondPhotoLayoutClicked() {
        return secondPhotoLayoutClicked;
    }

    public void setSecondPhotoLayoutClicked(boolean secondPhotoLayoutClicked) {
        this.secondPhotoLayoutClicked = secondPhotoLayoutClicked;
    }

    public boolean isSecondRemoveImageClicked() {
        return secondRemoveImageClicked;
    }

    public void setSecondRemoveImageClicked(boolean secondRemoveImageClicked) {
        this.secondRemoveImageClicked = secondRemoveImageClicked;
    }

    public boolean isBoxSelected() {
        return boxSelected;
    }

    public void setBoxSelected(boolean boxSelected) {
        this.boxSelected = boxSelected;
    }

    public void setShowCustomLabelErrorMsg(boolean showCustomLabelErrorMsg) {
        this.showCustomLabelErrorMsg = showCustomLabelErrorMsg;
    }

    public boolean isShowCustomLabelErrorMsg() {
        return showCustomLabelErrorMsg;
    }

    public boolean isShowImageErrorMsg() {
        return showImageErrorMsg;
    }

    public void setShowImageErrorMsg(boolean showImageErrorMsg) {
        this.showImageErrorMsg = showImageErrorMsg;
    }

    public boolean hasDrawOnFirstLayout() {
        return drawOnFirstLayout;
    }

    public void setDrawOnFirstLayout(boolean drawOnFirstLayout) {
        this.drawOnFirstLayout = drawOnFirstLayout;
    }

    public boolean hasDrawOnSecondLayout() {
        return drawOnSecondLayout;
    }

    public void setDrawOnSecondLayout(boolean drawOnSecondLayout) {
        this.drawOnSecondLayout = drawOnSecondLayout;
    }

    public boolean hasShowDeleteFirstPhoto() {
        return showDeleteFirstPhoto;
    }

    public void setShowDeleteFirstPhoto(boolean showDeleteFirstPhoto) {
        this.showDeleteFirstPhoto = showDeleteFirstPhoto;
    }

    public boolean hasShowDeleteSecondPhoto() {
        return showDeleteSecondPhoto;
    }

    public void setShowDeleteSecondPhoto(boolean showDeleteSecondPhoto) {
        this.showDeleteSecondPhoto = showDeleteSecondPhoto;
    }

    private boolean isPackageTypesLoaded() {
        return packageTypesLoaded;
    }

    void setPackageTypesLoaded(boolean packageTypesLoaded) {
        this.packageTypesLoaded = packageTypesLoaded;
    }

    @Override
    public void clear() {
        description = null;

        if (firstPhotoBitmap != null) {
            firstPhotoBitmap.recycle();
            firstPhotoBitmap = null;
        }

        if (secondPhotoBitmap != null) {
            secondPhotoBitmap.recycle();
            secondPhotoBitmap = null;
        }

        nickname = null;
        labelMessage = null;
    }

    @Override
    public String getTile() {
        return AppResources.string(R.string.package_details_title);
    }

    public enum PaymentTypes {
        PICKUP(R.string.pay_at_pickup),
        DELIVERY(R.string.pay_at_delivery);

        PaymentTypes(int labelStringResource) {
            label = AppResources.string(labelStringResource);
        }

        public final String label;
    }

    private enum PackageTypes {
        BOX("Box"),
        DOCUMENT("Document");

        PackageTypes(String text) {
            this.text = text;
        }

        private final String text;
    }

    @Override
    protected void serializeObject(ObjectOutputStream stream)
            throws IOException {

        ImageSaver imageSaver = new ImageSaver().directoryName(DIR_NAME);

        if (firstPhotoBitmap != null) {
            imageSaver.fileName(FILE_NAME_FIRST_IMG).save(firstPhotoBitmap);
        }

        if (secondPhotoBitmap != null) {
            imageSaver.fileName(FILE_NAME_SECOND_IMG).save(secondPhotoBitmap);
        }

    }


    @Override
    protected void deserializeObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {

        ImageSaver imageSaver = new ImageSaver().directoryName(DIR_NAME);

        if (imageSaver.fileName(FILE_NAME_FIRST_IMG).isSaved()) {
            firstPhotoBitmap = imageSaver.load();
            imageSaver.delete();
        }

        if (imageSaver.fileName(FILE_NAME_SECOND_IMG).isSaved()) {
            secondPhotoBitmap = imageSaver.load();
            imageSaver.delete();
        }

    }


}
