package com.base.presentation.interfaces;

import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.models.Model;

/**
 * implement this interface if your class is Hosted by a {@link Feature} implementer
 * <p/>
 * Created by Ahmed Adel on 10/12/2016.
 */
public interface FeatureHosted {

    /**
     * get the initial {@link Feature} implementer
     *
     * @return the {@link Feature} to be used, this can be Activity or a Fragment
     */
    <T extends Model> Feature<T> getFeature();
}
