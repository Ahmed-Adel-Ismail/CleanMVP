package com.appzoneltd.lastmile.driver.subfeatures;

import android.widget.ImageView;

import com.appzoneltd.lastmile.driver.requesters.images.HttpsPicasso;
import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.presentation.references.CheckedProperty;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.cached.ServerImage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * a class that creates a {@link RequestCreator} for {@link Picasso}, which will
 * be ready for calling {@link RequestCreator#into(ImageView)} directly
 * <p>
 * Created by Ahmed Adel on 1/6/2017.
 */
public class PicassoUriDrawer implements Command<String, RequestCreator> {
    private final CheckedProperty<AbstractActivity> activity = new CheckedProperty<>();

    public PicassoUriDrawer(AbstractActivity activity) {
        this.activity.set(activity);
    }

    @Override
    public RequestCreator execute(String uri) throws CheckedReferenceClearedException {
        uri = uri + "/" + ServerImage.SIZE_LARGE;
        return HttpsPicasso.getInstance(activity.get()).load(uri).fit();
    }
}
