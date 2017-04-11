package com.base.cached;

import com.base.annotations.MockEntity;
import com.base.mocked.MockedServerImagesGroup;

import java.util.HashMap;

/**
 * a group of {@link ServerImage} instances, each value has an index as a key
 * <p>
 * Created by Ahmed Adel on 1/4/2017.
 */
@MockEntity(MockedServerImagesGroup.class)
public class ServerImagesGroup extends HashMap<Integer, ServerImage> {

    public ServerImagesGroup() {

    }

    /**
     * get the {@link ServerImage} based on it's index in this group, notice that
     * updating this group will change indexes, so you have to make sure that this
     * group is not updated after you have obtained an index t an item
     *
     * @param index the {@code index} of the {@link ServerImage} in the current group
     * @return the Object mapped to this index
     * @throws UnsupportedOperationException if the index is out of bounds
     */
    public ServerImage getByIndex(int index) throws UnsupportedOperationException {
        ServerImage serverImage = get(index);
        if (serverImage == null) {
            throw new UnsupportedOperationException("no ServerImage for the passed index");
        }
        return serverImage;
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
        for (ServerImage serverImage : values()) {
            if (serverImage.getFlags() == ServerImage.FLAG_SELECTED) {

                if (serverImage.hasFileId()) {
                    throw new UnsupportedOperationException("id already set : " + serverImage);
                }

                serverImage.setFileId(fileId);
                serverImage.resetFlags();
                break;
            }
        }

    }

    public boolean containsImageId(Long imageId) {
        for (ServerImage s : values()) {
            if (s.getFileId() == imageId) {
                return true;
            }
        }
        return false;
    }
}
