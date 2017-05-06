package com.base.abstraction.observer;

import com.base.abstraction.actors.base.ActorAddressee;

/**
 * implement this interface if your class will need to be updated by another
 * class, like Observables for example
 * <p/>
 * Created by Ahmed Adel on 8/30/2016.
 */
public interface Observer extends ActorAddressee, Updatable {


}
