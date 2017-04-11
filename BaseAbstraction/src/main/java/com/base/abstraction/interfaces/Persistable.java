package com.base.abstraction.interfaces;

import java.io.Serializable;

/**
 * An interface implemented by any class that will provide a read/write mechanism to some source
 * <p/>
 * Created by Ahmed Adel on 10/13/2016.
 */
public interface Persistable<P extends Persistable<P, WriteToSource, ReadFromSource>, WriteToSource, ReadFromSource>
        extends Serializable {

    /**
     * implement this method to provide a way to write this Object to a source,
     * like serializing or writing to file
     *
     * @param source the source to write this Object to
     * @return the Object after being written, this can be used by the caller of the method
     * @throws RuntimeException if an error occurred
     */
    P writeObject(WriteToSource source) throws RuntimeException;


    /**
     * implement this method to provide a way to read this Object from a source,
     * like de-serializing or reading from file
     *
     * @param source the source to read this Object from
     * @return the Object after being read, this can be used by the caller of the method
     * @throws RuntimeException if an error occurred
     */
    P readObject(ReadFromSource source) throws RuntimeException;
}
