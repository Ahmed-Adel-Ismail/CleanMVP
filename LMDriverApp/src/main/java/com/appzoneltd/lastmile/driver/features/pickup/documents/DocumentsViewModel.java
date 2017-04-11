package com.appzoneltd.lastmile.driver.features.pickup.documents;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.interfaces.Initializer;
import com.base.abstraction.commands.Command;
import com.base.presentation.references.BooleanProperty;
import com.base.presentation.references.Property;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.ViewModel;
import com.base.presentation.listeners.OnEventListener;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

class DocumentsViewModel extends ViewModel {

    @Sync("customer-id-image-path")
    final Property<String> customerIdImagePath = new Property<>();

    @Sync("credit-card-image-path")
    final Property<String> creditCardImagePath = new Property<>();

    final BooleanProperty progress = new BooleanProperty();

    @Override
    public void initialize(ViewBinder viewBinder) {
        super.initialize(viewBinder);
        progress.onUpdate(invalidateViewsOnProgressToggle());
    }

    @NonNull
    private Command<Boolean, Boolean> invalidateViewsOnProgressToggle() {
        return new Command<Boolean, Boolean>() {
            @Override
            public Boolean execute(Boolean p) {
                invalidateViews();
                return p;
            }
        };
    }

    @Initializer({
            R.id.screen_pickup_process_documents_images_customer_id_image_image_view,
            R.id.screen_pickup_process_documents_images_credit_card_image_image_view})
    void initializeImageViews(ImageView imageView) {
        OnEventListener onEventListener = new OnEventListener(this);
        imageView.setOnTouchListener(onEventListener);
    }

    @Executable(R.id.screen_pickup_process_documents_images_customer_id_image_image_view)
    void customerIdImageView(ImageView imageView) {
        if (!customerIdImagePath.isEmpty()) {
            draw("file://" + customerIdImagePath.get()).into(imageView);
        } else {
            draw(R.drawable.pickup_process_id).into(imageView);
        }
    }

    @Executable(R.id.screen_pickup_process_documents_images_credit_card_image_image_view)
    void creditCardImageView(ImageView imageView) {
        if (!creditCardImagePath.isEmpty()) {
            draw("file://" + creditCardImagePath.get()).into(imageView);
        } else {
            draw(R.drawable.pickup_process_receipt).into(imageView);
        }
    }

    private DrawableRequestBuilder draw(String uri) {
        return Glide.with(getHostActivity()).load(uri).fitCenter().crossFade();
    }

    private DrawableRequestBuilder draw(int drawableRes) {
        return Glide.with(getHostActivity()).load(drawableRes).fitCenter().crossFade();
    }

    @Executable(R.id.screen_pickup_process_documents_main_layout)
    void mainLayout(View view) {
        view.setVisibility(progress.isTrue() ? View.GONE : View.VISIBLE);
    }

    @Executable(R.id.screen_pickup_process_documents_main_progress)
    void progressLayout(View view) {
        view.setVisibility(progress.isTrue() ? View.VISIBLE : View.GONE);
    }


}
