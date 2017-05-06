package com.base.abstraction.system;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;

/**
 * a class that reads a given resource value name and returns it's name, can be used in the
 * {@link #toString()} method to print the ids and values that are mapped to resources
 * <p>
 * if the value is not found in resources, it will be returned as a {@code String}
 * <p>
 * Created by Ahmed Adel on 11/6/2016.
 */
public class ResourcesReader implements Command<Long, String> {

    @Override
    public String execute(Long resourceValue) {
        String result = String.valueOf(resourceValue);
        if (resourceValue > 0) {
            try {
                return AppResources.resourceEntryName(Integer.valueOf(result));
            } catch (Throwable e) {
                Logger.getInstance().error(getClass(), "resource not found : " + resourceValue);
            }
        }
        return result;
    }
}
