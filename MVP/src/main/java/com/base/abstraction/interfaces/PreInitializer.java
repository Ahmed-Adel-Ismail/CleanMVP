package com.base.abstraction.interfaces;

import com.base.abstraction.commands.executors.CommandExecutor;

/**
 * Implement this interface if you want to make sub-classes initialize them selves
 * in the {@code super} call (parent constructor), this case is when you have abstract or template
 * methods called in the parent (this implementer) class and the sub-class needs to
 * be initialized before executing those methods in the parent class
 * <p/>
 * Created by Ahmed Adel on 10/5/2016.
 */
public interface PreInitializer {

    /**
     * {@code Override} this method if your sub-class needs to initialize member-variables
     * that will be used in the abstract or template methods invoked in the parent Constructor,
     * like {@link CommandExecutor Command Executors} creation related methods for example
     * <br>
     * note that this method is invoked before the constructor of the sub-class
     */
    void preInitialize();
}
