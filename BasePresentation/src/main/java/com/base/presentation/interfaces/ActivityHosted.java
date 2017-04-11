package com.base.presentation.interfaces;

import android.content.Context;

import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.models.Model;

/**
 * implement this interface if your class is Hosted by an Activity
 * <p/>
 * Created by Ahmed Adel on 10/12/2016.
 */
public interface ActivityHosted {

    /**
     * get the Host Activity Context
     *
     * @return the {@link Context} of the activity
     */
    @SuppressWarnings("unchecked")
    <T extends Model> AbstractActivity<T> getHostActivity();

}
