package com.base.abstraction.messaging;

import com.base.abstraction.commands.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed Adel on 10/23/2016.
 */
class Executor implements Command<Integer, Void> {

    List<Integer> values = new ArrayList<>();

    @Override
    public Void execute(Integer value) {
        values.add(value);
        return null;
    }
}