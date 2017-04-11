package com.entities.cached;

import java.io.Serializable;

/**
 * Created by Wafaa on 6/6/2016.
 */
public class Country implements Serializable {

    private Long countryId;
    private String name;

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
