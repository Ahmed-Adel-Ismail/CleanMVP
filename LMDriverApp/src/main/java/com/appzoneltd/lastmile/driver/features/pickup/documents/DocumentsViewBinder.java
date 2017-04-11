package com.appzoneltd.lastmile.driver.features.pickup.documents;

import android.view.View;

import com.appzoneltd.lastmile.driver.R;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.annotations.interfaces.Presenter;
import com.base.presentation.base.abstracts.features.ViewBinder;

import butterknife.BindView;

/**
 * the {@link ViewBinder} for the Documents screen in the pickup-process feature
 * <p>
 * Created by Ahmed Adel on 12/27/2016.
 */
@BindLayout(R.layout.screen_pickup_process_documents)
public class DocumentsViewBinder extends ViewBinder {

    @Presenter
    DocumentsPresenter documentsPresenter;
    @BindView(R.id.screen_pickup_process_documents_title_text_view)
    View screen_pickup_process_documents_title_text_view;
    @BindView(R.id.screen_pickup_process_documents_images_main_relative_layout)
    View screen_pickup_process_documents_images_main_relative_layout;
    @BindView(R.id.screen_pickup_process_documents_images_titles_linear_layout)
    View screen_pickup_process_documents_images_titles_linear_layout;
    @BindView(R.id.screen_pickup_process_documents_images_customer_id_title_text_view)
    View screen_pickup_process_documents_images_customer_id_title_text_view;
    @BindView(R.id.screen_pickup_process_documents_images_credit_card_title_text_view)
    View screen_pickup_process_documents_images_credit_card_title_text_view;
    @BindView(R.id.screen_pickup_process_documents_images_images_linear_layout)
    View screen_pickup_process_documents_images_images_linear_layout;
    @BindView(R.id.screen_pickup_process_documents_images_customer_id_image_image_view)
    View screen_pickup_process_documents_images_customer_id_image_image_view;
    @BindView(R.id.screen_pickup_process_documents_images_credit_card_image_image_view)
    View screen_pickup_process_documents_images_credit_card_image_image_view;
    @BindView(R.id.screen_pickup_process_documents_submit_button)
    View screen_pickup_process_documents_submit_button;
    @BindView(R.id.screen_pickup_process_documents_main_layout)
    View screen_pickup_process_documents_main_layout;
    @BindView(R.id.screen_pickup_process_documents_main_progress)
    View screen_pickup_process_documents_main_progress;
}
