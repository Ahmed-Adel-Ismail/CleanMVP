package com.base.presentation.base.abstracts.features;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.events.Message;
import com.base.presentation.R;
import com.base.presentation.requests.ActivityResult;

/**
 * An implementer for the {@link ScreenLifeCycle} interface
 * <p/>
 * Created by Ahmed Adel on 10/16/2016.
 */
class ScreenLifeCycleImplementer implements ScreenLifeCycle {

    private Feature<?> feature;
    private ViewBinder viewBinder;

    ScreenLifeCycleImplementer(Feature<?> feature, ViewBinder viewBinder) {
        this.feature = feature;
        this.viewBinder = viewBinder;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinder.addObserver(feature.getModel());
        View v = null;
        if (feature instanceof AbstractActivity) {
            new ActivityInflater().execute(viewBinder);
        } else if (feature instanceof AbstractFragment) {
            v = invokeFragmentInflateView(inflater, container);
        } else {
            return throwUnsupportedOperationException();
        }
        return v;
    }

    private View invokeFragmentInflateView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View v;
        v = inflater.inflate(viewBinder.getContentView(), container, false);
        return v;
    }

    private View throwUnsupportedOperationException() {
        throw new UnsupportedOperationException("unknown implementer to "
                + Feature.class.getSimpleName() + " interface");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Message message = new Message.Builder().content(savedInstanceState).build();
        viewBinder.onUpdate(new EventBuilder(R.id.onCreate, message).execute(feature));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ActivityResult result = new ActivityResult(requestCode, resultCode, data);
        Message message = new Message.Builder().id(requestCode).content(result).build();
        viewBinder.onUpdate(new EventBuilder(R.id.onUpdateModel).execute(feature));
        viewBinder.onUpdate(new EventBuilder(R.id.onActivityResult, message).execute(feature));
    }

    @Override
    public void onResume() {
        viewBinder.onUpdate(new EventBuilder(R.id.onResume).execute(feature));
    }

    @Override
    public void onPause() {
        viewBinder.onUpdate(new EventBuilder(R.id.onPause).execute(feature));
    }

    @Override
    public void onDestroy() {
        viewBinder.onUpdate(new EventBuilder(R.id.onDestroy).execute(feature));
        viewBinder.clear();
        clearSelf();
    }

    private void clearSelf() {
        viewBinder = null;
        feature = null;
    }

}
