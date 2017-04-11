package com.entities.cached.pakage;

import com.base.annotations.MockEntity;
import com.entities.mocks.pakage.MockedPackageTypesGroup;

import java.util.TreeSet;

/**
 * a group of {@link PackageType} instances
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
@MockEntity(MockedPackageTypesGroup.class)
public class PackageTypesGroup extends TreeSet<PackageType> {


    /**
     * get the {@link PackageType} based on it's id in this list, notice that
     * updating this group will change indexes, so you have to make sure that this
     * group is not updated after you have obtained an id t an item
     *
     * @param index the {@code id} of the {@link PackageType} in the current list
     * @return the Object mapped to this id
     * @throws UnsupportedOperationException if the id is out of bounds
     */
    public PackageType getByIndex(int index) throws UnsupportedOperationException {
        if (index < 0 || index >= size()) {
            throw new UnsupportedOperationException("id out of bound");
        }

        int i = 0;
        for (PackageType p : this) {
            if (index == i++) {
                return p;
            }
        }

        throw new UnsupportedOperationException("id not found");
    }


    /**
     * create a {@code String[]} for all the current types available in this
     * {@link PackageTypesGroup} ... retrieved through invoking {@link PackageType#getPackageType()}
     * on every item
     *
     * @return the {@code String[]} ... if this list is empty, it will return {@code null}
     */
    public String[] createTypesArray() {
        if (isEmpty()) {
            return null;
        }

        String[] types = new String[size()];
        int i = 0;
        for (PackageType p : this) {
            types[i++] = p.getPackageType();
        }
        return types;
    }


    /**
     * get the id of a given {@link PackageType} by it's {@link PackageType#getPackageType()}
     *
     * @param packageType the name / type of this {@link PackageType}, through invoking
     *                    {@link PackageType#getPackageType()}
     * @return the id of the given value
     * @throws UnsupportedOperationException if no id for the given package type
     */
    public int getIndexFor(PackageType packageType)
            throws UnsupportedOperationException {

        if (packageType == null) {
            throw new UnsupportedOperationException("null parameter not accepted");
        }

        int i = 0;
        boolean found = false;
        for (PackageType p : this) {
            if (p.getPackageType().equals(packageType.getPackageType())) {
                found = true;
                break;
            } else {
                i++;
            }
        }

        if (!found) {
            throw new UnsupportedOperationException("no matching package type found");
        }
        return i;
    }

}
