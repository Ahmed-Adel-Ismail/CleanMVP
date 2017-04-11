package com.base.presentation.locations.commands;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.cached.Location;

/**
 * a class that is responsible to convert Android locations into Location Entities that
 * can be used to communicate with server
 * <p>
 * Created by Ahmed Adel on 12/23/2016.
 */
public class LocationConverter implements Command<android.location.Location, Location> {

    @Override
    public Location execute(@NonNull android.location.Location androidLocation) {
        Location location = new Location();
        location.setAccuracy(androidLocation.getAccuracy());
        location.setAltitude(androidLocation.getAltitude());
        location.setBearing(androidLocation.getBearing());
        location.setLatitude(androidLocation.getLatitude());
        location.setLongitude(androidLocation.getLongitude());
        location.setProvider(androidLocation.getProvider());
        location.setSpeed(androidLocation.getSpeed());
        location.setTime(androidLocation.getTime());
        return location;
    }
}
