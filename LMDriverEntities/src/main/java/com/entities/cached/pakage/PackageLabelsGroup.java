package com.entities.cached.pakage;

import com.base.annotations.MockEntity;
import com.entities.mocks.pakage.MockedPackageLabelsGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * a group of {@link PackageLabel} instances
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
@MockEntity(MockedPackageLabelsGroup.class)
public class PackageLabelsGroup extends LinkedList<PackageLabel> {

    /**
     * get the {@link PackageLabel} instances with the passed IDs, this method does not
     * create new instances, it points references to the same instances held in this group
     *
     * @param ids the IDs to look up for
     * @return the sub-set of this group with the {@link PackageLabel} instances that holds
     * the desired Ids
     * @throws UnsupportedOperationException if no IDs where found in the current group, or
     *                                       if the passed list of IDs is {@code null} or empty
     */
    public PackageLabelsGroup getPackageLabelsWithIds(List<Long> ids)
            throws UnsupportedOperationException {

        if (ids == null || ids.isEmpty()) {
            throw new UnsupportedOperationException("no IDs to look for");
        }

        PackageLabelsGroup newGroup = new PackageLabelsGroup();
        for (PackageLabel p : this) {
            if (ids.contains(p.getPackageLabelId())) {
                newGroup.add(p);
            }
        }

        if (newGroup.isEmpty()) {
            throw new UnsupportedOperationException("no elements that match the given IDs");
        }

        return newGroup;
    }

    public List<Long> getIds() {
        List<Long> result = new ArrayList<>(size());
        for (PackageLabel p : this) {
            result.add(p.getPackageLabelId());
        }
        return result;
    }


}
