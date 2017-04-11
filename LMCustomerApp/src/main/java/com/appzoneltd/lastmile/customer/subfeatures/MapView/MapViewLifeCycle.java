package com.appzoneltd.lastmile.customer.subfeatures.MapView;

/**
 * Created by Wafaa on 12/1/2016.
 */

public enum MapViewLifeCycle {

    ONCREATE(1), ONSTART(2),
    ONRESUME(3), ONPAUSE(4),
    ONSTOP(5), ONDESTROY(6),
    ONSAVEINSTANTSTATE(7), ONLOWMEMOTY(8);

    MapViewLifeCycle(int status) {
        this.status = status;
    }

    public int status;

}
