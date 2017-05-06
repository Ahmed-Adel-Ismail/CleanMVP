package com.base.presentation.base.abstracts.features;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Window;

import com.base.abstraction.annotations.scanners.ClassAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.references.CheckedReference;
import com.base.presentation.annotations.interfaces.WindowBackgroundColor;
import com.base.presentation.annotations.interfaces.WindowFeature;

/**
 * a class that is responsible to draw the Activity based on the interfaces it implements
 * <p>
 * Created by Ahmed Adel on 11/21/2016.
 *
 * @deprecated this class crashes on requesting Window feature ... needs to be fixed before use
 */
@Deprecated
class ActivityThemeDrawer implements Command<Feature<?>, Void> {

    @Override
    public Void execute(Feature<?> feature) {
        if (feature instanceof AbstractActivity) {
            updateActivityTheme(feature);
        }
        return null;
    }

    private void updateActivityTheme(Feature<?> feature) {

        final CheckedReference<AbstractActivity<?>> activityRef = new CheckedReference<>(null);
        activityRef.set(feature.getHostActivity());
        try {
            createWindowFeatureScanner(activityRef).execute(activityRef);
            createWindowBackgroundScanner(activityRef).execute(activityRef.get());
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
        }

    }

    @NonNull
    private ClassAnnotationScanner<WindowFeature> createWindowFeatureScanner(
            final CheckedReference<AbstractActivity<?>> activityRef) {
        return new ClassAnnotationScanner<WindowFeature>(WindowFeature.class) {

            @Override
            protected void onAnnotationFound(WindowFeature annotation) {
                onActivityWindowFeatureComplete(activityRef);
            }
        };
    }

    @NonNull
    private Command<WindowFeature, Void> onActivityWindowFeatureComplete(
            final CheckedReference<AbstractActivity<?>> activityRef) {
        return new Command<WindowFeature, Void>() {
            @Override
            public Void execute(WindowFeature annotation) {
                int windowFeature = annotation.value();
                activityRef.get().requestWindowFeature(windowFeature);
                return null;
            }
        };
    }


    @NonNull
    private ClassAnnotationScanner<WindowBackgroundColor> createWindowBackgroundScanner(final CheckedReference<AbstractActivity<?>> activityRef) {
        return new ClassAnnotationScanner<WindowBackgroundColor>(WindowBackgroundColor.class) {
            @Override
            protected void onAnnotationFound(WindowBackgroundColor annotation) {
                onWindowBackgroundComplete(activityRef);
            }
        };
    }


    @NonNull
    private Command<WindowBackgroundColor, Void> onWindowBackgroundComplete(
            final CheckedReference<AbstractActivity<?>> activityRef) {
        return new Command<WindowBackgroundColor, Void>() {
            @Override
            public Void execute(WindowBackgroundColor annotation) {
                Window window = activityRef.get().getWindow();
                window.setBackgroundDrawable(new ColorDrawable(annotation.value()));
                return null;
            }
        };
    }

}
