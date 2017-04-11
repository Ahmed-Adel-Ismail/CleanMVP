package com.entities.mocks;

import com.entities.cached.PayloadActiveVehicleDetails;

/**
 * Created by Wafaa on 12/31/2016.
 */

public class MockedPayloadActiveVehicleDetails extends PayloadActiveVehicleDetails {

    public MockedPayloadActiveVehicleDetails(){
        super.driverName = "Mahmoud";
        super.driverRating = 3;
        super.driverPhoneNumber = "01003567603";
        super.driverImageId = 827973111;
        super.hubId = 730674704;
        super.vehicleId = 408;
        super.pickupLatitude = 29.985080;
        super.pickupLongitude = 30.959035;
        super.vehiclePlateNumber = "1256";
        super.vehicleModel = "suzuki";
    }
}
