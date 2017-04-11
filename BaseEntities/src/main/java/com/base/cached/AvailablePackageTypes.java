package com.base.cached;

/**
 * the Package types IDs mapped to the database on server
 * <p>
 * Created by Ahmed Adel on 2/16/2017.
 */
public enum AvailablePackageTypes {
    FACTORY(0, 0),
    DOCUMENT(1, 10),
    BOX(2, 25);

    AvailablePackageTypes(long id, double expectedWeight) {
        this.id = id;
        this.expectedWeight = expectedWeight;
    }

    public final long id;
    public final double expectedWeight;

    public AvailablePackageTypes getType(long id) {
        for (AvailablePackageTypes availablePackageTypes : values()) {
            if (availablePackageTypes.id == id) {
                return availablePackageTypes;
            }
        }
        return FACTORY;
    }

}
