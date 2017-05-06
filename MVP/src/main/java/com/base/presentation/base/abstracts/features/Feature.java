package com.base.presentation.base.abstracts.features;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.base.abstraction.R;
import com.base.abstraction.actors.base.Actor;
import com.base.abstraction.actors.base.ActorAddressee;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.observer.Observer;
import com.base.presentation.base.abstracts.system.ManifestAccessor;
import com.base.presentation.base.abstracts.system.SystemAccessor;
import com.base.presentation.interfaces.ActivityHosted;
import com.base.presentation.interfaces.DestroyableClient;
import com.base.presentation.interfaces.ModelClient;
import com.base.presentation.models.Model;
import com.base.presentation.requests.ActivityActionRequest;

/**
 * an interface representing a Feature in the Application
 * <p/>
 * as for the Application should be divided in set of features / screens ... every screen
 * is a feature and can be accessed and used in a generic manner, this interface
 * provides this behavior, where the Activity, it's Model, it's Fragments and it's Presenters,
 * all providing one feature in the application
 * <p/>
 * Created by Ahmed Adel on 9/1/2016.
 */
public interface Feature<T extends Model> extends
        Actor,
        ScreenLifeCycle,
        ViewBindable,
        ActivityHosted,
        ModelClient<T>,
        SystemAccessor,
        ManifestAccessor,
        DestroyableClient,
        ActorAddressee {


    /**
     * get the root view of the value {@link Feature} implementer (Activity or fragment)
     *
     * @return the {@link View} that is the root of the View's Hierarchy of this {@link Feature}
     */
    View getView();


    /**
     * add a {@link Fragment} to the value {@link Feature}
     *
     * @param containerId the id of the View that will contain the Fragment as a child to it
     * @param fragment    the {@link Fragment} to add
     */
    void addFragment(int containerId, @NonNull Fragment fragment);

    /**
     * remove the previously added fragment
     *
     * @param fragment the previously added Fragment
     */
    void removeFragment(@NonNull Fragment fragment);

    /**
     * start a new action based on the {@link ActivityActionRequest} instance passed,
     * notice that before starting the new Activity or service (or finishing the value one),
     * 2 Events will be triggered, the first event is {@link R.id#onUpdateModel}, then
     * {@link R.id#onActivityActionRequest} will be triggered with the Action {@code String} value
     * in it's {@link Message#getContent()} for any {@link Observer} needs to do it's
     * stuff before starting the activity (or service
     *
     * @param event the {@link ActivityActionRequest} {@link Event}
     */
    void startActivityActionRequest(Event event);

}
