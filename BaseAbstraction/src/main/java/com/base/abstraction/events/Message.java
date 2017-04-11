package com.base.abstraction.events;

import com.base.abstraction.converters.SerializableObject;
import com.base.abstraction.interfaces.Immutable;
import com.base.abstraction.interfaces.Relationable;
import com.base.abstraction.system.ResourcesReader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * an Object that encapsulates the content that is supposed to be delivered from
 * one point in the flow to another point
 * <p>
 * notice that some instances holds non-serializable content, so before
 * using this instance in a serialization related operation, you should check for
 * {@link #isSerializable()} first
 * <p/>
 * Created by Ahmed Adel on 8/31/2016.
 */
public class Message extends SerializableObject implements
        Relationable,
        Immutable {

    private long id;
    private boolean serializable;
    private transient Object content;

    protected Message(long id, Object content) {
        this.id = id;
        this.content = content;
        this.serializable = content == null || content instanceof Serializable;
    }

    @Override
    public long getId() {
        return id;
    }

    /**
     * get the content of this {@link Message}
     *
     * @param <T> the expected type of Content that this Message holds
     * @return the content of the message
     */
    @SuppressWarnings("unchecked")
    public <T> T getContent() {
        return (T) content;
    }

    /**
     * check if the current message holds a {@link Serializable} Object in it's
     * {@link #getContent()} or not (if there is no content, this {@link Message} is valid for
     * serialization)
     *
     * @return {@code true} if this {@link Message} is valid for serialization
     */
    public boolean isSerializable() {
        return serializable;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[id=");
        builder.append(new ResourcesReader().execute(id));
        builder.append(", content=");
        builder.append(String.valueOf(content).trim());
        builder.append("]");
        return builder.toString();
    }

    @Override
    protected void serializeObject(ObjectOutputStream stream)
            throws IOException {
        super.serializeObject(stream);
        if (serializable) {
            stream.writeObject(content);
        }
    }

    @Override
    protected void deserializeObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        super.deserializeObject(stream);
        if (serializable) {
            content = stream.readObject();
        }
    }


    public Message.Builder copyBuilder() {
        return new Message.Builder().id(id).content(content);
    }


    /**
     * an Abstract class to be parent for all {@code Builder} classes that will build
     * {@link Message} sub-classes
     * <p>
     * this class is visible within the sub-classes scope, to ease creating {@code Builder}
     * classes for the sub-classes of the {@link Message}
     */
    protected static abstract class AbstractBuilder {

        protected long id;
        protected Object content;

        public AbstractBuilder id(long id) {
            this.id = id;
            return this;
        }

        public AbstractBuilder content(Object content) {
            this.content = content;
            return this;
        }

        public abstract Message build();
    }

    public static class Builder extends AbstractBuilder {

        @Override
        public Builder id(long id) {
            super.id(id);
            return this;
        }

        @Override
        public Builder content(Object content) {
            super.content(content);
            return this;
        }

        @Override
        public Message build() {
            return new Message(id, content);
        }
    }

}
