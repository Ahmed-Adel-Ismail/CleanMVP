package com.base.abstraction.converters;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * a {@link Serializable} Class that provides template methods to Serialize {@code transient}
 * members and non-serializable members easily, provides
 * {@link #serializeObject(ObjectOutputStream)} and {@link #deserializeObject(ObjectInputStream)}
 * to write and read non-serializable members easily
 * <p/>
 * Created by Ahmed Adel on 10/13/2016.
 */
public class SerializableObject implements Serializable {

    /**
     * serialize this Object
     *
     * @param stream the stream to write Object
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
        serializeObject(stream);
    }

    /**
     * this method is invoked in {@link #writeObject(ObjectOutputStream)} to serialize the
     * non-serializable members of the class
     *
     * @param stream the {@link ObjectOutputStream} that writes the writes to a stream for
     *               serializing this object
     */
    protected void serializeObject(ObjectOutputStream stream)
            throws IOException {
        // template method
    }

    /**
     * de-serialize this Object
     *
     * @param stream the stream to read Object
     */
    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        deserializeObject(stream);
    }

    /**
     * this method is invoked in {@link #writeObject(ObjectOutputStream)} to serialize the
     * non-serializable members of the class
     *
     * @param stream the {@link ObjectOutputStream} that writes the writes to a stream for
     *               serializing this object
     */
    protected void deserializeObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        // template method
    }

}
