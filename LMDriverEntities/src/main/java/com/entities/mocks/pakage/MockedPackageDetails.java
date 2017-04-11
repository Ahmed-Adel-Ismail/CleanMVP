package com.entities.mocks.pakage;

import com.entities.cached.pakage.PackageDetails;
import com.entities.cached.payment.PaymentMethod;
import com.entities.cached.payment.PaymentType;
import com.base.mocked.MockedServerImagesGroup;
import com.entities.mocks.cache.CacheKeys;
import com.entities.mocks.cache.MocksCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockedPackageDetails extends PackageDetails {

    private static final List<String> NICK_NAMES = new ArrayList<>();
    private static final Map<String, Integer> TYPES = new HashMap<>();
    private static final Map<String, Double> WEIGHTS = new HashMap<>();
    private static final Map<String, String> WRAPPING = new HashMap<>();
    private static final Map<String, long[]> LABELS = new HashMap<>();
    private static final Map<String, String> DESC = new HashMap<>();


    static {

        fillNames();
        fillTypes();
        fillWeights();
        fillWrapping();
        fillLabels();
        fillDesc();
    }

    private static void fillNames() {
        NICK_NAMES.add("rolex watch");          //0
        NICK_NAMES.add("3rd anniversary");      //1
        NICK_NAMES.add("graduation papers");    //2
        NICK_NAMES.add("tools kit");            //3
        NICK_NAMES.add("mother's day gift");    //4
        NICK_NAMES.add("order #62682432");      //5
        NICK_NAMES.add("order #53692548");      //6
        NICK_NAMES.add("order #46592041");      //7
        NICK_NAMES.add("order #28991500");      //8
        NICK_NAMES.add("bills");                //9
    }

    private static void fillTypes() {
        TYPES.put(NICK_NAMES.get(0), 0);
        TYPES.put(NICK_NAMES.get(1), 0);
        TYPES.put(NICK_NAMES.get(2), 1);
        TYPES.put(NICK_NAMES.get(3), 0);
        TYPES.put(NICK_NAMES.get(4), 0);
        TYPES.put(NICK_NAMES.get(5), 1);
        TYPES.put(NICK_NAMES.get(6), 0);
        TYPES.put(NICK_NAMES.get(7), 1);
        TYPES.put(NICK_NAMES.get(8), 0);
        TYPES.put(NICK_NAMES.get(9), 1);
    }

    private static void fillWeights() {
        WEIGHTS.put(NICK_NAMES.get(0), 0.5d);
        WEIGHTS.put(NICK_NAMES.get(1), 2d);
        WEIGHTS.put(NICK_NAMES.get(2), 0.3d);
        WEIGHTS.put(NICK_NAMES.get(3), 10d);
        WEIGHTS.put(NICK_NAMES.get(4), 1.5d);
        WEIGHTS.put(NICK_NAMES.get(5), 0.2d);
        WEIGHTS.put(NICK_NAMES.get(6), 4d);
        WEIGHTS.put(NICK_NAMES.get(7), 0.5d);
        WEIGHTS.put(NICK_NAMES.get(8), 1d);
        WEIGHTS.put(NICK_NAMES.get(9), 0.4d);
    }

    private static void fillWrapping() {
        WRAPPING.put(NICK_NAMES.get(0), "HAPPY BIRTH DAY");
        WRAPPING.put(NICK_NAMES.get(1), "happiness forever");
        WRAPPING.put(NICK_NAMES.get(2), "congratulations");
        WRAPPING.put(NICK_NAMES.get(3), "send ASAP");
        WRAPPING.put(NICK_NAMES.get(4), "to my lovely mother");
        WRAPPING.put(NICK_NAMES.get(5), "$120");
        WRAPPING.put(NICK_NAMES.get(6), "$40");
        WRAPPING.put(NICK_NAMES.get(7), "$80");
        WRAPPING.put(NICK_NAMES.get(8), "$80");
        WRAPPING.put(NICK_NAMES.get(9), "VIP");
    }

    private static void fillLabels() {
        LABELS.put(NICK_NAMES.get(0), new long[]{1});
        LABELS.put(NICK_NAMES.get(1), new long[]{2, 4, 5});
        LABELS.put(NICK_NAMES.get(2), new long[]{7});
        LABELS.put(NICK_NAMES.get(3), new long[]{3});
        LABELS.put(NICK_NAMES.get(4), new long[]{4});
        LABELS.put(NICK_NAMES.get(5), new long[]{6});
        LABELS.put(NICK_NAMES.get(6), new long[]{7});
        LABELS.put(NICK_NAMES.get(7), new long[]{2});
        LABELS.put(NICK_NAMES.get(8), new long[]{4});
        LABELS.put(NICK_NAMES.get(9), new long[]{7, 8});
    }

    private static void fillDesc() {
        DESC.put(NICK_NAMES.get(0), "a rolex watch");
        DESC.put(NICK_NAMES.get(1), "a big gift package wrapped with expensive material, be cautious");
        DESC.put(NICK_NAMES.get(2), "certificates papers");
        DESC.put(NICK_NAMES.get(3), "a big kit filled with tools");
        DESC.put(NICK_NAMES.get(4), "mothers day gift :)");
        DESC.put(NICK_NAMES.get(5), "chemical material that should be stored in cool place");
        DESC.put(NICK_NAMES.get(6), "big box for Client #69951, should stay dry");
        DESC.put(NICK_NAMES.get(7), "a cup for client #5241");
        DESC.put(NICK_NAMES.get(8), "LEGO box");
        DESC.put(NICK_NAMES.get(9), "this bills should be delivered to be paid ASAP");
    }

    public MockedPackageDetails() {
        super.id = (long) (Math.random() * NICK_NAMES.size());
        super.nickname = NICK_NAMES.get((int) id);
        super.type = new MockedPackageTypesGroup().getByIndex(TYPES.get(nickname));
        super.weight = WEIGHTS.get(nickname);
        super.description = DESC.get(nickname);
        super.wrappingLabel = WRAPPING.get(nickname);
        super.boxing = createMockedBoxing();
        super.packagingLabelsIds = createMockedPackagingLabelIds();
        super.paymentType = createMockedPaymentType();
        super.paymentMethod = createMockedPaymentMethod();
        MocksCache.getInstance().put(CacheKeys.PACKAGE_DETAILS_PAYMENT_METHOD, paymentMethod);
        super.imageIds = createMockedImageIds();
    }

    private List<Long> createMockedImageIds() {
        ArrayList<Long> list = new ArrayList<>();
        list.add(MockedServerImagesGroup.ID_ONE);
        if ((Math.random() * 10) % 2 == 0) {
            list.add(MockedServerImagesGroup.ID_TWO);
        }
        return list;
    }

    private String createMockedBoxing() {
        if (((Math.random() * 10) % 2) == 0) {
            return BOXING_VALUE;
        } else {
            return null;
        }
    }

    private List<Long> createMockedPackagingLabelIds() {
        long[] ids = LABELS.get(nickname);
        List<Long> result = new ArrayList<>(ids.length);
        for (long id : ids) {
            result.add(id);
        }
        return result;

    }

    private PaymentType createMockedPaymentType() {
        if (Math.random() * 10 % 2 == 0) {
            return PaymentType.DELIVERY;
        } else {
            return PaymentType.PICKUP;
        }
    }

    private PaymentMethod createMockedPaymentMethod() {
        if (Math.random() * 10 % 2 == 0) {
            return PaymentMethod.CASH;
        } else {
            return PaymentMethod.CREDIT_CARD;
        }
    }


}
