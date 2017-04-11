package com.entities.cached.cancellation;

import com.base.annotations.MockEntity;
import com.entities.mocks.cancellation.MockedCancellationReasonsGroup;

import java.util.TreeSet;

/**
 * a group of {@link CancellationReason}
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
@MockEntity(MockedCancellationReasonsGroup.class)
public class CancellationReasonsGroup extends TreeSet<CancellationReason> {

    /**
     * create a {@code String[]} for all the current types available in this
     * {@link CancellationReason} ... retrieved through invoking
     * {@link CancellationReason#getName()}
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
        for (CancellationReason p : this) {
            types[i++] = p.getName();
        }
        return types;
    }

    /**
     * get the {@link CancellationReason} based on it's id in this list, notice that
     * updating this group will change indexes, so you have to make sure that this
     * group is not updated after you have obtained an id t an item
     *
     * @param index the {@code id} of the {@link CancellationReason} in the current list
     * @return the Object mapped to this id
     * @throws UnsupportedOperationException if the id is out of bounds
     */
    public CancellationReason getByIndex(int index) throws UnsupportedOperationException {
        if (index < 0 || index >= size()) {
            throw new UnsupportedOperationException("id out of bound");
        }

        int i = 0;
        for (CancellationReason p : this) {
            if (index == i++) {
                return p;
            }
        }

        throw new UnsupportedOperationException("id not found");
    }


}
