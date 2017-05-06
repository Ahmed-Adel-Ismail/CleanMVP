package com.base.abstraction.interfaces;

/**
 * an interface implemented by classes that will need to be initialized after calling
 * there constructor
 * <p>
 * Created by Ahmed Adel on 11/24/2016.
 *
 * @param <Parameter> the parameter required for initializing this Object
 */
public interface Initializable<Parameter> {

    /**
     * initialize the current instance
     *
     * @param parameter the Object required for initialization
     */
    void initialize(Parameter parameter);

}
