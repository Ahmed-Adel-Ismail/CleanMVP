package com.base.presentation.base.presentation;

import android.support.annotation.NonNull;
import android.view.View;

import com.base.abstraction.aggregates.KeyAggregateAddable;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.interfaces.Emptyable;
import com.base.presentation.references.Entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * a class that holds {@link Entity} instances mapped to {@link View} ids,
 * every view-id is mapped to multiple {@link Entity} instances
 * <p>
 * Created by Ahmed Adel on 1/31/2017.
 */
class ViewsEntities implements
        Emptyable,
        KeyAggregateAddable<Long, Entity<?>>,
        Clearable {

    private HashMap<Long, Set<Entity<?>>> map = new HashMap<>();

    @Override
    public Entity<?> put(Long viewId, Entity<?> entity) {
        Set<Entity<?>> entitiesList = map.get(viewId);
        if (entitiesList == null) {
            entitiesList = createEntitiesGroupForView(viewId);
        }
        entitiesList.add(entity);
        return null;
    }

    boolean hasEmptyEntities(long viewId) {
        final Set<Entity<?>> entities = map.get(viewId);
        if (entities != null) {
            if (isAnyEntityEmpty(entities)) return true;
        }
        return false;
    }

    private boolean isAnyEntityEmpty(Set<Entity<?>> entities) {
        for (Entity<?> entity : entities) {
            if (entity.isEmpty()) return true;
        }
        return false;
    }

    /**
     * put the same {@link Entity} for multiple keys / view-ids
     *
     * @param viewsIds the view-ids that will hold this {@link Entity}
     * @param entity   the {@link Entity} to be appended, notice that for every view id, there
     *                 are multiple {@link Entity} objects in it's value
     */
    void putForKeys(long[] viewsIds, Entity<?> entity) {
        for (long viewId : viewsIds) {
            put(viewId, entity);
        }
    }

    @NonNull
    private Set<Entity<?>> createEntitiesGroupForView(Long viewId) {
        Set<Entity<?>> entitiesList;
        entitiesList = new HashSet<>();
        map.put(viewId, entitiesList);
        return entitiesList;
    }

    int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public void clear() {
        map.clear();
    }
}
