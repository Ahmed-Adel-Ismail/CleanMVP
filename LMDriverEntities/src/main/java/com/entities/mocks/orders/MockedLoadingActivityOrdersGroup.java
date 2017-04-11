package com.entities.mocks.orders;


import com.base.cached.AvailablePackageTypes;
import com.base.mocked.MockedImagesIds;
import com.entities.cached.orders.LoadingActivityOrder;
import com.entities.cached.orders.LoadingActivityOrdersGroup;
import com.entities.cached.pakage.PackageType;

import java.util.ArrayList;
import java.util.List;

public class MockedLoadingActivityOrdersGroup extends LoadingActivityOrdersGroup {

    private static final List<String> CONTENT_DOCUMENT = new ArrayList<>();
    private static final List<String> CONTENT_BOX = new ArrayList<>();

    static {
        CONTENT_BOX.add("A Fragile box");
        CONTENT_BOX.add("Tool box");
        CONTENT_BOX.add("Anniversary gift");
        CONTENT_BOX.add("Important content, be careful");
        CONTENT_BOX.add("Toys and Lego");
        CONTENT_BOX.add("Package for client");
        CONTENT_BOX.add("Spare parts");
        CONTENT_BOX.add("This box should be delivered as soon as possible, it should be available by tomorrow morning max");
        CONTENT_BOX.add("Car parts");
        CONTENT_BOX.add("Old Computer");
        CONTENT_BOX.add("Express Delivery pack");
        CONTENT_BOX.add("Clothes from the north, handle with care");
        CONTENT_BOX.add("My favourite books");
        CONTENT_BOX.add("Watches collection");


        CONTENT_DOCUMENT.add("Bills");
        CONTENT_DOCUMENT.add("Mail to GM");
        CONTENT_DOCUMENT.add("VIP papers");
        CONTENT_DOCUMENT.add("Bank required papers");
        CONTENT_DOCUMENT.add("Documents for client");
        CONTENT_DOCUMENT.add("Old News papers for museum");
        CONTENT_DOCUMENT.add("Important CDs");
        CONTENT_DOCUMENT.add("Delayed papers that should arrive today, no more delay accepted");
        CONTENT_DOCUMENT.add("Candidates files");
        CONTENT_DOCUMENT.add("Lottery papers");


    }

    public MockedLoadingActivityOrdersGroup() {

        LoadingActivityOrder order;
        MockedImagesIds imagesIds = MockedImagesIds.fromServer105();
        for (int i = 0; i < 40; i++) {
            order = new LoadingActivityOrder();
            order.setId(i);
            order.setImageId(imagesIds.getRandomId());
            order.setPackageType(createPackageType(i));
            order.setContent(createPackageContent(order.getAvailablePackageType()));
            order.setActualWeight(createActualWeight(order.getAvailablePackageType()));
            add(order);
        }

    }


    private PackageType createPackageType(int i) {
        AvailablePackageTypes availablePackageTypes = (i % 3 == 0)
                ? AvailablePackageTypes.DOCUMENT
                : AvailablePackageTypes.BOX;
        PackageType packageType = new PackageType();
        packageType.setPackageTypeId(availablePackageTypes.id);
        packageType.setExpectedWeight(availablePackageTypes.expectedWeight);
        packageType.setPackageType(availablePackageTypes.toString());
        packageType.setPackageDimension("10*10");
        return packageType;
    }


    private String createPackageContent(AvailablePackageTypes availablePackageTypes) {
        return AvailablePackageTypes.DOCUMENT.equals(availablePackageTypes)
                ? CONTENT_DOCUMENT.get((int) (Math.random() * CONTENT_DOCUMENT.size()))
                : CONTENT_BOX.get((int) (Math.random() * CONTENT_BOX.size()));
    }


    private double createActualWeight(AvailablePackageTypes availablePackageTypes) {
        return AvailablePackageTypes.DOCUMENT.equals(availablePackageTypes)
                ? Math.random() * 5
                : Math.random() * 25;
    }


}
