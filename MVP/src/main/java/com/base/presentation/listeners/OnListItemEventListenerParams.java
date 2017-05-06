package com.base.presentation.listeners;

import android.view.View;

import com.base.abstraction.interfaces.Immutable;

import java.io.Serializable;

/**
 * Created by Wafaa on 11/15/2016.
 */

public class OnListItemEventListenerParams<T extends Serializable & Cloneable> implements Immutable {

    private T entity;
    private View view;
    private long viewId;
    private int position;

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public long getViewId() {
        return viewId;
    }

    void setViewId(long viewId) {
        this.viewId = viewId;
    }

    public View getView() {
        return view;
    }

    void setView(View view) {
        this.view = view;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
