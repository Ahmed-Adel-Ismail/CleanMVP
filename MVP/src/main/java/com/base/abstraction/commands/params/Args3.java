package com.base.abstraction.commands.params;

import com.base.abstraction.commands.Command;

/**
 * a class that represents three arguments for a method, can be used with {@link Command} classes
 * <p>
 * Created by Ahmed Adel on 11/21/2016.
 *
 * @param <FirstParameter>  the type of the first parameter
 * @param <SecondParameter> the type of the second parameter
 * @param <ThirdParameter>  the type of the third parameter
 */
@Deprecated
public class Args3<FirstParameter, SecondParameter, ThirdParameter> extends
        Args2 {

    private ThirdParameter third;

    public Args3() {
    }

    public Args3(FirstParameter first) {
        super(first);
    }

    public Args3(FirstParameter first, SecondParameter second) {
        super(first, second);
    }

    public Args3(FirstParameter first, SecondParameter second, ThirdParameter third) {
        super(first, second);
        this.third = third;
    }

    public ThirdParameter third() {
        return third;
    }

    public Args3<FirstParameter, SecondParameter, ThirdParameter> third(ThirdParameter value) {
        this.third = value;
        return this;
    }
}
