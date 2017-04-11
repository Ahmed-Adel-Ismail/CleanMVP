package com.entities.cached.pakage;

import com.base.annotations.MockEntity;
import com.base.cached.ServerImage;
import com.base.cached.ServerImagesGroup;
import com.entities.cached.payment.PaymentMethod;
import com.entities.cached.payment.PaymentType;
import com.entities.mocks.pakage.MockedPackageDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * the details of a package
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
@MockEntity(MockedPackageDetails.class)
public class PackageDetails implements Serializable {

    protected static final String BOXING_VALUE = "Packaging Box";

    protected long id;
    protected String nickname;
    protected PackageType type;
    protected double weight;
    protected String description;
    protected String wrappingLabel;
    protected String boxing;
    protected List<Long> packagingLabelsIds;
    protected PaymentType paymentType;
    protected PaymentMethod paymentMethod;
    protected List<Long> imageIds;

    private transient PackageLabelsGroup packageLabels;
    private transient ServerImagesGroup serverImages;


    public String getBoxing() {
        return boxing;
    }

    public void setBoxing(String boxing) {
        this.boxing = boxing;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Long> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<Long> imageIds) {
        this.imageIds = imageIds;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<Long> getPackagingLabelsIds() {
        return packagingLabelsIds;
    }

    public void setPackagingLabelsIds(List<Long> packagingLabelsIds) {
        this.packagingLabelsIds = packagingLabelsIds;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PackageType getType() {
        return type;
    }

    public void setType(PackageType type) {
        this.type = type;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getWrappingLabel() {
        return wrappingLabel;
    }

    public void setWrappingLabel(String wrappingLabel) {
        this.wrappingLabel = wrappingLabel;
    }

    public boolean containsPackageLabel(PackageLabel packageLabel) {
        return packageLabels != null && packageLabels.contains(packageLabel);
    }

    public boolean removePackageLabel(PackageLabel packageLabel) {
        if (packageLabel == null) {
            return false;
        }

        if (packageLabels == null) {
            packageLabels = new PackageLabelsGroup();
        }
        packageLabels.remove(packageLabel);

        if (packagingLabelsIds == null) {
            packagingLabelsIds = new ArrayList<>();
        }
        packagingLabelsIds.remove(packageLabel.getPackageLabelId());

        return true;
    }

    public boolean addPackageLabel(PackageLabel packageLabel) {

        if (packageLabel == null) {
            return false;
        }

        if (packageLabels == null) {
            packageLabels = new PackageLabelsGroup();
        }
        packageLabels.add(packageLabel);

        if (packagingLabelsIds == null) {
            packagingLabelsIds = new ArrayList<>();
        }
        packagingLabelsIds.add(packageLabel.getPackageLabelId());

        return true;
    }

    public void setPackageLabels(PackageLabelsGroup packageLabels) {
        this.packageLabels = packageLabels;
        updateLabelIdsFromLabels();
    }

    public void setBoxing(boolean boxing) {
        this.boxing = boxing ? BOXING_VALUE : null;
    }

    public void updateLabelIdsFromLabels() {
        if (packageLabels != null) {
            packagingLabelsIds = packageLabels.getIds();
        } else {
            packagingLabelsIds.clear();
        }

    }

    private boolean hasServerImages() {
        return serverImages != null && !serverImages.isEmpty();
    }

    /**
     * get the {@link ServerImage} by id
     *
     * @param index the id of the {@link ServerImage}
     * @return the {@link ServerImage}
     * @throws UnsupportedOperationException if no server image is available for the given
     *                                       id
     */
    public ServerImage getServerImageByIndex(int index) throws UnsupportedOperationException {
        if (hasServerImages()) {
            return serverImages.getByIndex(index);
        }
        throw new UnsupportedOperationException("no server images available");
    }

    public void addServerImage(int index, ServerImage serverImage) {
        initializeServerImagesIfNull();
        serverImages.put(index, serverImage);
    }

    public int getServerImagesCount() {
        initializeServerImagesIfNull();
        return serverImages.size();
    }

    public boolean isServerImagesUpdatedLocally() {
        return imageIds == null
                || (serverImages != null && (serverImages.size() >= imageIds.size()));
    }

    /**
     * check if all the server images has file ids on server or not
     *
     * @return {@code true} if all is updated with ids on server, else {@code false}
     */
    public boolean isServerImagesUpdatedOnServer() {
        boolean result = isServerImagesUpdatedLocally() && imageIds != null;
        if (result) {
            for (ServerImage serverImage : serverImages.values()) {
                if (result = !imageIds.contains(serverImage.getFileId())) {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * updates the {@link ServerImage} with the flag {@link ServerImage#FLAG_SELECTED} with the
     * passed fileId ... only the first instance found will be updated
     *
     * @param fileId the file id on server
     * @throws UnsupportedOperationException if a selected {@link ServerImage} has an id ... this
     *                                       should not happen
     */
    public void updateSelectedServerImage(long fileId) throws UnsupportedOperationException {
        if (imageIds != null && serverImages != null) {
            serverImages.updateSelectedServerImage(fileId);
        }
    }

    public ServerImagesGroup getServerImages() {
        initializeServerImagesIfNull();
        return serverImages;
    }

    /**
     * get an image ID that is not available in the {@link ServerImagesGroup} yet, which means it
     * needs updating
     *
     * @return the non-updated image ids
     * @throws UnsupportedOperationException if non is available
     */
    public long pollNonUpdatedImageId() throws UnsupportedOperationException {
        if (isServerImagesUpdatedLocally()) {
            throw new UnsupportedOperationException("no ids to be updated");
        }
        initializeServerImagesIfNull();
        for (Long imageId : imageIds) {
            if (!serverImages.containsImageId(imageId)) {
                return imageId;
            }
        }
        throw new UnsupportedOperationException("all ids are updated");
    }

    private void initializeServerImagesIfNull() {
        if (serverImages == null) {
            serverImages = new ServerImagesGroup();
        }
    }

    /**
     * update the current image ids with the {@link ServerImagesGroup} stored,
     *
     * @return {@code true} if successful, or {@code false} if nothing happened
     * @throws UnsupportedOperationException if any {@link ServerImage} does not have a file id from
     *                                       server
     */
    public boolean updateImagesIdsFromServerImages() throws UnsupportedOperationException {
        if (serverImages == null) {
            return false;
        }
        imageIds = new ArrayList<>();
        for (ServerImage serverImage : serverImages.values()) {
            if (serverImage.hasFileId()) {
                imageIds.add(serverImage.getFileId());
            } else {
                throw new UnsupportedOperationException("no fileId for server image : " + serverImage);
            }

        }
        return true;
    }
}
