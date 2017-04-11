package com.base.presentation.views.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.base.abstraction.references.CheckedReference;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Wafaa on 11/14/2016.
 */

public abstract class AbstractAdapter<T , V extends AbstractViewHolder>
        extends RecyclerView.Adapter<V> {


    private CheckedReference<RecyclerView> recyclerViewCheckedRef;
    private int recyclerViewId;
    private LinkedList<T> entities;

    public void setup(@NonNull Collection<T> entities, @NonNull RecyclerView recyclerView) {
        this.entities = new LinkedList<>(entities);
        recyclerViewCheckedRef = new CheckedReference<>(recyclerView);
        this.recyclerViewId = recyclerView.getId();
        recyclerView.setAdapter(this);
    }

    @Override
    public final V onCreateViewHolder(ViewGroup parent, int viewType) {
        V abstractViewHolder = createAbstractViewHolder(parent, viewType);
        abstractViewHolder.setParentViewId(recyclerViewId);
        abstractViewHolder.setAbstractAdapter(this);
        return abstractViewHolder;
    }

    @Override
    public final void onBindViewHolder(V holder, int position) {
        holder.draw(entities.get(position));
    }

    @Override
    public final int getItemCount() {
        if (entities != null) {
            return entities.size();
        }
        return 0;
    }


    protected abstract V createAbstractViewHolder(ViewGroup parent, int viewType);

    public void invalidate(int position, LinkedList<T> newEntities) {
        this.entities = newEntities;
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, entities.size() - 1);
        notifyDataSetChanged();
        if (recyclerViewCheckedRef != null) {
            recyclerViewCheckedRef.get().invalidate();
        }
    }

}
