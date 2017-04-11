package com.appzoneltd.lastmile.driver.features.pickup.verification;

import android.view.View;

import com.appzoneltd.lastmile.driver.R;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.annotations.interfaces.Presenter;
import com.base.presentation.base.abstracts.features.ViewBinder;

import butterknife.BindView;

/**
 * the {@link ViewBinder} for the verification screen in the pickup-process feature
 * <p>
 * Created by Ahmed Adel on 12/27/2016.
 */
@BindLayout(R.layout.screen_pickup_process_verify_package)
public class VerificationViewBinder extends ViewBinder {

    @Presenter
    VerificationPresenter verificationPresenter;
    @BindView(R.id.screen_pickup_process_verify_package_nick_name_details_text_view)
    View screen_pickup_process_verify_package_nick_name_details_text_view;
    @BindView(R.id.screen_pickup_process_verify_package_type_details_spinner)
    View screen_pickup_process_verify_package_type_details_spinner;
    @BindView(R.id.screen_pickup_process_verify_package_wight_text_input_layout)
    View screen_pickup_process_verify_package_wight_text_input_layout;
    @BindView(R.id.screen_pickup_process_verify_package_wight_text_input_edit_text)
    View screen_pickup_process_verify_package_wight_text_input_edit_text;
    @BindView(R.id.screen_pickup_process_verify_package_whats_inside_text_input_layout)
    View screen_pickup_process_verify_package_whats_inside_text_input_layout;
    @BindView(R.id.screen_pickup_process_verify_package_whats_inside_text_input_edit_text)
    View screen_pickup_process_verify_package_whats_inside_text_input_edit_text;
    @BindView(R.id.screen_pickup_process_verify_package_additional_services_details_check_box_one)
    View screen_pickup_process_verify_package_additional_services_details_check_box_one;
    @BindView(R.id.screen_pickup_process_verify_package_additional_services_details_check_box_two)
    View screen_pickup_process_verify_package_additional_services_details_check_box_two;
    @BindView(R.id.screen_pickup_process_verify_package_labels_relative_layout)
    View screen_pickup_process_verify_package_labels_relative_layout;
    @BindView(R.id.screen_pickup_process_verify_package_labels_details_linear_layout)
    View screen_pickup_process_verify_package_labels_details_linear_layout;
    @BindView(R.id.screen_pickup_process_verify_package_labels_details_check_box_one)
    View screen_pickup_process_verify_package_labels_details_check_box_one;
    @BindView(R.id.screen_pickup_process_verify_package_labels_details_check_box_two)
    View screen_pickup_process_verify_package_labels_details_check_box_two;
    @BindView(R.id.screen_pickup_process_verify_package_labels_details_check_box_three)
    View screen_pickup_process_verify_package_labels_details_check_box_three;
    @BindView(R.id.screen_pickup_process_verify_package_labels_details_check_box_four)
    View screen_pickup_process_verify_package_labels_details_check_box_four;
    @BindView(R.id.screen_pickup_process_verify_package_labels_details_check_box_five)
    View screen_pickup_process_verify_package_labels_details_check_box_five;
    @BindView(R.id.screen_pickup_process_verify_package_labels_details_check_box_six)
    View screen_pickup_process_verify_package_labels_details_check_box_six;
    @BindView(R.id.screen_pickup_process_verify_package_labels_details_check_box_seven)
    View screen_pickup_process_verify_package_labels_details_check_box_seven;
    @BindView(R.id.screen_pickup_process_verify_package_labels_details_check_box_eight)
    View screen_pickup_process_verify_package_labels_details_check_box_eight;
    @BindView(R.id.screen_pickup_process_verify_package_payment_at_details_check_box_one)
    View screen_pickup_process_verify_package_payment_at_details_check_box_one;
    @BindView(R.id.screen_pickup_process_verify_package_payment_at_details_check_box_two)
    View screen_pickup_process_verify_package_payment_at_details_check_box_two;
    @BindView(R.id.screen_pickup_process_verify_package_buy_with_details_check_box_one)
    View screen_pickup_process_verify_package_buy_with_details_check_box_one;
    @BindView(R.id.screen_pickup_process_verify_package_buy_with_details_check_box_two)
    View screen_pickup_process_verify_package_buy_with_details_check_box_two;
    @BindView(R.id.screen_pickup_process_verify_package_main_layout)
    View screen_pickup_process_verify_package_main_layout;
    @BindView(R.id.screen_pickup_process_verify_package_main_progress)
    View screen_pickup_process_verify_package_main_progress;
    @BindView(R.id.screen_pickup_process_verify_package_images_left_image_main_image)
    View screen_pickup_process_verify_package_images_left_image_main_image;
    @BindView(R.id.screen_pickup_process_verify_package_images_right_image_main_image)
    View screen_pickup_process_verify_package_images_right_image_main_image;


}
