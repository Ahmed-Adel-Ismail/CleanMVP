package com.base.abstraction.api.usecases;


import com.base.abstraction.commands.Command;

/**
 * an interface implemented by classes that will generate URL from a request id
 * <p>
 * Created by Ahmed Adel on 10/18/2016.
 */
public interface RequestUrlLocator extends Command<Long, String> {

    @Override
    String execute(Long requestId);


}
