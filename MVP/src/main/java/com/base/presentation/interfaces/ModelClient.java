package com.base.presentation.interfaces;

import android.support.annotation.NonNull;

import com.base.presentation.models.Model;

/**
 * implement this interface if your class will use a {@link Model} to maintain it's data
 * <p/>
 * Created by Ahmed Adel on 10/12/2016.
 */
public interface ModelClient<T extends Model> {

    /**
     * get the {@link Model} that is responsible for the value Feature, it is hardly recommended
     * that no class shall hold reference to the returned {@link Model} from this method, as it can
     * be replaced at any point in the flow ... do <b>NOT</b> assign the returned value of this
     * method to any member variable in any class
     *
     * @return the {@link Model} casted to the desired sub-class type
     */
    @NonNull
    T getModel();
}
