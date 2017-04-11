package com.entities.mocks.pickup;

import com.entities.cached.pickup.PickupService;
import com.entities.cached.pickup.PickupServicesGroup;

public class MockedPickupServicesGroup extends PickupServicesGroup {

    final int priceOne = (int) (Math.random() * 3000);
    final int priceTwo = (int) (Math.random() * 3000);
    final int priceThree = (int) (Math.random() * 3000);

    MockedPickupServicesGroup() {

        PickupService p = new PickupService();
        p.setType("On-demand pickup");
        p.setLocation("6 October, 6 Nabatat st.");
        p.setQuantity("1 Box (20kg)");
        p.setPrice(String.valueOf(priceOne));
        add(p);

        p = new PickupService();
        p.setType("Express Delivery");
        p.setLocation("6 October, 6 Nabatat st.");
        p.setQuantity("1 Box (5kg)");
        p.setPrice(String.valueOf(priceTwo));
        add(p);

        p = new PickupService();
        p.setType("On-demand pickup");
        p.setLocation("6 October, 6 Nabatat st.");
        p.setQuantity("1 Document (0.5kg)");
        p.setPrice(String.valueOf(priceThree));
        add(p);


    }

}
