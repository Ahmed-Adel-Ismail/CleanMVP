package com.base.usecases.requesters.base;

import com.base.abstraction.aggregates.AggregateAddable;
import com.base.abstraction.aggregates.AggregateContainable;
import com.base.abstraction.aggregates.AggregateRemovable;
import com.base.abstraction.converters.Incrementer;
import com.base.abstraction.events.Event;
import com.base.abstraction.interfaces.Clearable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * a group of {@link Event} instances mapped to there request ids
 * <p/>
 * Created by Ahmed Adel on 9/19/2016.
 */
final class RequestGroup implements
        Clearable,
        AggregateContainable<Event>,
        AggregateAddable<Event, Event>,
        AggregateRemovable<Void, Long> {

    private final Map<Long, Event> requestIds = new LinkedHashMap<>();
    private final Map<Long, Integer> concurrentRequestCounts = new HashMap<>();
    private final Set<Long> concurrentRequestIds = new HashSet<>();


    protected void addConcurrentRequestId(long id) {
        concurrentRequestIds.add(id);
    }

    @Override
    public boolean contains(Event event) {
        return requestIds.containsKey(event.getId());
    }

    @Override
    public Event add(Event event) {
        long requestId = event.getId();
        if (isConcurrentRequest(requestId)) {
            incrementConcurrentRequestCount(requestId);
        }
        requestIds.put(requestId, event);
        return event;
    }

    /**
     * check if the passed {@link Event} is a concurrent request or normal request
     *
     * @param requestId the request id to look up for
     * @return {@code true} if this id is concurrent (previously added through
     * {@link #addConcurrentRequestId(long)}, else it will return {@code false}
     */
    public boolean isConcurrentRequest(long requestId) {
        return concurrentRequestIds.contains(requestId);
    }

    private void incrementConcurrentRequestCount(long requestId) {
        Integer requestsCount = concurrentRequestCounts.get(requestId);
        requestsCount = Incrementer.increment(requestsCount);
        concurrentRequestCounts.put(requestId, requestsCount);
    }


    @Override
    public Void remove(Long requestId) {
        Integer requestsCount = concurrentRequestCounts.get(requestId);
        requestsCount = Incrementer.decrement(requestsCount);
        if (requestsCount <= 0) {
            removeLastRequestId(requestId);
        } else {
            concurrentRequestCounts.put(requestId, requestsCount);
        }
        return null;
    }

    private void removeLastRequestId(Long requestId) {
        concurrentRequestCounts.remove(requestId);
        requestIds.remove(requestId);
    }


    @Override
    public void clear() {
        concurrentRequestIds.clear();
        concurrentRequestCounts.clear();
        requestIds.clear();
    }


}
