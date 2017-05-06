package com.base.abstraction.commands.params;

import com.base.abstraction.commands.Command;

/**
 * a class that represents two arguments for a method, can be used with {@link Command} classes
 * <p>
 * Created by Ahmed Adel on 11/21/2016.
 *
 * @param <FirstParameter>  the type of the first parameter
 * @param <SecondParameter> the type of the second parameter
 */
@Deprecated
public class Args2<FirstParameter, SecondParameter> {

    private FirstParameter first;
    private SecondParameter second;

    public Args2() {
    }

    public Args2(FirstParameter first) {
        this.first = first;
    }

    public Args2(FirstParameter first, SecondParameter second) {
        this.first = first;
        this.second = second;
    }

    public FirstParameter first() {
        return first;
    }

    public Args2<FirstParameter, SecondParameter> first(FirstParameter value) {
        this.first = value;
        return this;
    }

    public SecondParameter second() {
        return second;
    }

    public Args2<FirstParameter, SecondParameter> second(SecondParameter value) {
        this.second = value;
        return this;
    }
}
