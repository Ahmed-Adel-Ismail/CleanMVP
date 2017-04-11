package com.base.mocked;

import java.util.ArrayList;

/**
 * a class that holds some Ids on the static content server based on an ip
 * <p>
 * Created by Ahmed Adel on 2/16/2017.
 */
public class MockedImagesIds extends ArrayList<Long> {

    private MockedImagesIds() {

    }

    /**
     * load mocked images ids that are present on server 105
     *
     * @return a {@link MockedImagesIds} with some actual ids present on server 105
     */
    public static MockedImagesIds fromServer105() {
        MockedImagesIds ids = new MockedImagesIds();
        ids.add(62145202L);
        ids.add(62304053L);
        ids.add(62701283L);
        ids.add(995253936L);
        ids.add(995140605L);
        return ids;
    }

    public long getRandomId() {
        return get((int) (Math.random() * size()));
    }
}
