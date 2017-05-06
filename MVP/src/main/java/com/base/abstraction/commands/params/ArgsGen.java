package com.base.abstraction.commands.params;

/**
 * a class that generates arguments / parameters objects, like
 * {@link Args2 Dual Arguments Object} and
 * {@link Args3 Triple Arguments Object}
 * <p>
 * Created by Ahmed Adel on 11/21/2016.
 */
@Deprecated
public class ArgsGen {

    /**
     * create a duel Arguments instance ... the {@link Args2} that
     * holds 2 arguments / parameters
     *
     * @param arg1   the first argument / parameter
     * @param arg2   the second argument / parameter
     * @param <Arg1> the type of the first argument / parameter
     * @param <Arg2> the type of the second argument / parameter
     * @return the {@link Args2 Duel Arguments Object}
     * to be used
     */
    public static <Arg1, Arg2> Args2<Arg1, Arg2> create(
            Arg1 arg1,
            Arg2 arg2) {
        return new Args2<Arg1, Arg2>(arg1, arg2);
    }

    /**
     * create a triple Arguments instance ... the {@link Args3}
     * that holds 3 arguments / parameters
     *
     * @param arg1   the first argument / parameter
     * @param arg2   the second argument / parameter
     * @param arg3   the third argument / parameter
     * @param <Arg1> the type of the first argument / parameter
     * @param <Arg2> the type of the second argument / parameter
     * @param <Arg3> the type of the third argument / parameter
     * @return the {@link Args3 Triple Arguments Object}
     * to be used
     */
    public static <Arg1, Arg2, Arg3> Args3<Arg1, Arg2, Arg3>
    create(Arg1 arg1, Arg2 arg2, Arg3 arg3) {
        return new Args3<Arg1, Arg2, Arg3>(arg1, arg2, arg3);
    }

}
