package com.appzoneltd.lastmile.customer.subfeatures.menus;

import com.base.abstraction.interfaces.Immutable;

import java.io.Serializable;

/**
 * Created by Wafaa on 8/31/2016.
 */
public class FloatingMenuParams implements Serializable, Immutable {

    private int index;
    private int imageResourceId;
    private int textResourceId;

    FloatingMenuParams() {

    }

    public FloatingMenuParams(int index, int imageSource, int textSource) {
        this.imageResourceId = imageSource;
        this.index = index;
        this.textResourceId = textSource;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public int getIndex() {
        return index;
    }

    public int getTextResourceId() {
        return textResourceId;
    }

    public boolean hasTextResourceId() {
        return textResourceId > 0;
    }

    public static class Builder {

        private final int index;
        private int imageResourceId;
        private int textResourceId;

        public Builder(int index) {
            this.index = index;
        }

        public Builder imageResourceId(int id) {
            this.imageResourceId = id;
            return this;
        }

        public Builder textResourceId(int id) {
            this.textResourceId = id;
            return this;
        }

        public FloatingMenuParams build() {
            return new FloatingMenuParams(index, imageResourceId, textResourceId);
        }


    }


}
