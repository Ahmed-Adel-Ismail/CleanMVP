package com.entities.cached;

import java.io.Serializable;

/**
 * Created by Wafaa on 2/15/2017.
 */

public class TrackedRequest implements Serializable {

    private boolean isTracked;

    public boolean isTracked() {
        return isTracked;
    }

    public void setTracked(boolean tracked) {
        isTracked = tracked;
    }
}
