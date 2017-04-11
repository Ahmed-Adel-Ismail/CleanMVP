package com.entities.cached;

import java.io.Serializable;

/**
 * Created by Wafaa on 11/6/2016.
 */

public class City implements Serializable {

    private Long cityId;
    private String name;
    private long countryId;

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCountryId() {
        return countryId;
    }

    public void setCountryId(long countryId) {
        this.countryId = countryId;
    }
}
