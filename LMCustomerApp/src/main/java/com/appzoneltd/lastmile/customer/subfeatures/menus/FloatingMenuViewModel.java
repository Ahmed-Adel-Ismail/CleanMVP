package com.appzoneltd.lastmile.customer.subfeatures.menus;

import android.view.View;
import android.view.ViewGroup;

import com.base.abstraction.converters.PixelsConverter;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.listeners.OnEventListener;

import java.lang.ref.WeakReference;

/**
 * A Class that draws the FLoating Menu Buttons UI on a given {@link ViewGroup}
 * <p/>
 * Created by Ahmed Adel on 9/14/2016.
 */
public class FloatingMenuViewModel {

    private static final int MAX_WIDTH_DP = 320;
    private static final int MAX_HEIGHT_DP = 72;
    private final OnEventListener onClickListener;
    private final WeakReference<ViewGroup> viewGroupWeakReference;
    private final WeakReference<Feature> abstractContextWeakReference;

    public FloatingMenuViewModel(ViewBinder viewBinder, ViewGroup viewGroup) {
        this.viewGroupWeakReference = new WeakReference<>(viewGroup);
        this.abstractContextWeakReference = new WeakReference<Feature>(viewBinder.getFeature());
        this.onClickListener = new OnEventListener(viewBinder.getFeature());
    }

    public void draw(FloatingMenuParamsGroup floatingMenuParamsGroup) {
        ViewGroup viewGroup = viewGroupWeakReference.get();
        Feature feature = abstractContextWeakReference.get();
        if (viewGroup == null || feature == null) {
            return;
        }
        if (viewGroup.getChildCount() > 0) {
            removeOldFloatingMenuButtons(viewGroup);
        }
        for (final FloatingMenuParams params : floatingMenuParamsGroup) {
            drawFloatingMenuButton(viewGroup, feature, params);
        }

    }

    private void removeOldFloatingMenuButtons(ViewGroup viewGroup) {
        viewGroup.removeAllViews();
    }

    private void drawFloatingMenuButton(ViewGroup viewGroup, Feature feature,
                                        FloatingMenuParams params) {
        CustomLabelledFloatingButton fb;
        fb = new CustomLabelledFloatingButton(feature.getHostActivity());
        fb.setImageViewResource(params.getImageResourceId());
        fb.setTextViewResource(params.getTextResourceId());
        fb.setId(params.getIndex());
        int heightPx = new PixelsConverter(MAX_HEIGHT_DP).pixels();
        int widthPx = (params.hasTextResourceId()) ? new PixelsConverter(MAX_WIDTH_DP).pixels()
                : heightPx;
        viewGroup.addView(fb, widthPx, heightPx);
        fb.setOnClickListener(onClickListener);
    }

    /**
     * hide the floating menu buttons
     */
    public void hide() {
        ViewGroup viewGroup = viewGroupWeakReference.get();
        if (viewGroup != null) {
            switchViewGroupVisibility(viewGroup, View.GONE);
        }
    }

    /**
     * show the floating menu buttons
     */
    public void show() {
        ViewGroup viewGroup = viewGroupWeakReference.get();
        if (viewGroup != null) {
            switchViewGroupVisibility(viewGroup, View.VISIBLE);
        }
    }

    private void switchViewGroupVisibility(ViewGroup viewGroup, int visibility) {
        viewGroup.setVisibility(visibility);
    }


}
