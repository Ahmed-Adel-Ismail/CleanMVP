package com.base.presentation.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.base.abstraction.observer.Observer;
import com.base.abstraction.references.CheckedReference;
import com.base.presentation.R;

import butterknife.ButterKnife;

import java.io.Serializable;

/**
 * Created by Wafaa on 11/14/2016.
 */
public abstract class AbstractViewHolder<T> extends RecyclerView.ViewHolder
        implements Observer {

    private CheckedReference<AbstractAdapter<T , AbstractViewHolder<T>>> abstractAdapterRef;
    private int parentViewId;

    public AbstractViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    protected abstract void draw(T item);

    public int getParentViewId() {
        return parentViewId;
    }

    public void setAbstractAdapter(AbstractAdapter<T , AbstractViewHolder<T>> abstractAdapter) {
        abstractAdapterRef = new CheckedReference<>(abstractAdapter);
    }

    public AbstractAdapter getAbstractAdapter() {
        return abstractAdapterRef.get();
    }

    public void setParentViewId(int parentViewId) {
        this.parentViewId = parentViewId;
    }

    @Override
    public long getActorAddress() {
        return R.id.eventCategoryItemListEventCommand;
    }

}