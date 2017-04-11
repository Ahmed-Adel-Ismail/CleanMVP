package com.appzoneltd.lastmile.driver.subfeatures;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.presentation.references.CheckedProperty;
import com.base.cached.ServerImage;
import com.base.presentation.base.abstracts.features.AbstractActivity;

import java.net.URI;

/**
 * a class that draws {@link ServerImage} into a given {@link ImageView}
 * <p>
 * Created by Ahmed Adel on 1/6/2017.
 */
public class ServerImageDrawer implements Command<ImageView, Boolean> {

    private ServerImage serverImage;
    private CheckedProperty<AbstractActivity> activity = new CheckedProperty<>();

    public ServerImageDrawer(AbstractActivity activity, @NonNull ServerImage serverImage) {
        this.activity.set(activity);
        this.serverImage = serverImage;
    }

    @Override
    public Boolean execute(ImageView imageView) {
        try {
            String uri = serverImage.getUri();
            if (serverImage.hasFileId() && new URI(uri).getScheme() != null) {
                new PicassoUriDrawer(activity.get()).execute(uri).into(imageView);
            } else {
                new GlideFileDrawer(activity.get()).execute(uri).into(imageView);
            }
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
            return false;
        }
        return true;
    }
}
