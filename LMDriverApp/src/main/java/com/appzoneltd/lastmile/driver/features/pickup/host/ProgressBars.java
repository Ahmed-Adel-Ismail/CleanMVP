package com.appzoneltd.lastmile.driver.features.pickup.host;

/**
 * an enum that indicates the random Progress bars in the pickup-process loading screen
 * <p>
 * Created by Ahmed Adel on 12/29/2016.
 */
enum ProgressBars {

    TOP_LEFT(0), TOP_RIGHT(1), BOTTOM_LEF(2), BOTTOM_RIGHT(3), CENTER(4);

    ProgressBars(int index) {
        this.index = index;
    }

    int index;

    ProgressBars next() {
        int nextIndex;
        ProgressBars[] values = values();
        do {
            nextIndex = (int) (Math.random() * values.length);
        } while (nextIndex == index);
        return values[nextIndex];
    }
}