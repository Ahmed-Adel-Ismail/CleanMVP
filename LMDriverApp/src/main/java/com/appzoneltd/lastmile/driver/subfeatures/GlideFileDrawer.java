package com.appzoneltd.lastmile.driver.subfeatures;

import android.widget.ImageView;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.presentation.references.CheckedProperty;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

/**
 * a class that creates a {@link DrawableRequestBuilder} for {@link Glide}, which will
 * be ready for calling {@link DrawableRequestBuilder#into(ImageView)} directly
 * <p>
 * Created by Ahmed Adel on 1/6/2017.
 */
public class GlideFileDrawer implements Command<String, DrawableRequestBuilder> {

    private final CheckedProperty<AbstractActivity> activity = new CheckedProperty<>();

    public GlideFileDrawer(AbstractActivity activity) {
        this.activity.set(activity);
    }

    @Override
    public DrawableRequestBuilder execute(String uri) throws CheckedReferenceClearedException {
        return Glide.with(activity.get()).load("file://" + uri).fitCenter().crossFade();
    }
}
