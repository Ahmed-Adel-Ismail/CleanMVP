package com.base.abstraction.system;

/**
 * an enum that indicates the state of the accepted behaviors across the application
 * <p/>
 * Created by Ahmed Adel on 9/20/2016.
 */
public enum Behaviors {

    /**
     * this version is for market, only market behaviors are accepted
     */
    MARKET(0),
    /**
     * this version is for debugging on production server, testing behaviors are not accepted,
     * but market behaviors are accepted
     */
    DEBUGGING(1),
    /**
     * this version is for testing, any behavior is accepted
     */
    TESTING(2),
    /**
     * this version is currently mocking responses locally
     */
    MOCKING(3);


    Behaviors(int flexibilityLevel) {
        this.flexibilityLevel = flexibilityLevel;
    }

    private final int flexibilityLevel;

    boolean isLessFlexible(Behaviors behavior) {
        return flexibilityLevel <= behavior.flexibilityLevel;
    }
}
