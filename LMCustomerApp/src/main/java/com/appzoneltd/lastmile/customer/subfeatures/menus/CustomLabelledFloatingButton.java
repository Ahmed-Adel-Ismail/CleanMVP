package com.appzoneltd.lastmile.customer.subfeatures.menus;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;


/**
 * Created by Wafaa on 8/31/2016.
 */
class CustomLabelledFloatingButton extends RelativeLayout {

    private static final int NO_RESOURCES = 0;
    private ImageView primaryButtonImageView;
    private TextView textView;

    public CustomLabelledFloatingButton(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_custom_floating, this, true);
        primaryButtonImageView = (ImageView) findViewById(R.id.lm_floating_menu_item_primary_image_view);
        primaryButtonImageView.setVisibility(GONE);
        textView = (TextView) findViewById(R.id.lm_floating_menu_item_label);
        textView.setVisibility(GONE);
    }

    public CustomLabelledFloatingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLabelledFloatingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageViewResource(int imageSrc) {
        primaryButtonImageView.setVisibility(VISIBLE);
        primaryButtonImageView.setImageResource(imageSrc);
    }

    public void setTextViewResource(int textSrc) {
        if (textSrc > NO_RESOURCES) {
            textView.setText(textSrc);
            textView.setVisibility(VISIBLE);
        }
    }

}
