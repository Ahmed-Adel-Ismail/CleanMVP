package com.appzoneltd.lastmile.driver.features.pickup.payments;

import android.view.View;

import com.appzoneltd.lastmile.driver.R;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.annotations.interfaces.Presenter;
import com.base.presentation.base.abstracts.features.ViewBinder;

import butterknife.BindView;

/**
 * the {@link ViewBinder} for the payment and invoice screen in the pickup-process feature
 * <p>
 * Created by Ahmed Adel on 12/27/2016.
 */
@BindLayout(R.layout.screen_pickup_process_payment)
public class PaymentViewBinder extends ViewBinder {

    @Presenter
    PaymentPresenter paymentPresenter;
    @BindView(R.id.screen_pickup_process_payment_header_invoice_text_view)
    View screen_pickup_process_payment_header_invoice_text_view;
    @BindView(R.id.screen_pickup_process_payment_header_date_text_view)
    View screen_pickup_process_payment_header_date_text_view;
    @BindView(R.id.screen_pickup_process_payment_header_time_text_view)
    View screen_pickup_process_payment_header_time_text_view;
    @BindView(R.id.screen_pickup_process_payment_service_types_outer_relative_layout)
    View screen_pickup_process_payment_service_types_outer_relative_layout;
    @BindView(R.id.screen_pickup_process_payment_service_types_linear_layout_line_zero)
    View screen_pickup_process_payment_service_types_linear_layout_line_zero;
    @BindView(R.id.screen_pickup_process_payment_service_types_linear_layout_line_one)
    View screen_pickup_process_payment_service_types_linear_layout_line_one;
    @BindView(R.id.screen_pickup_process_payment_service_types_linear_layout_line_two)
    View screen_pickup_process_payment_service_types_linear_layout_line_two;
    @BindView(R.id.screen_pickup_process_payment_service_types_linear_layout_line_three)
    View screen_pickup_process_payment_service_types_linear_layout_line_three;
    @BindView(R.id.screen_pickup_process_payment_service_types_linear_layout_line_four)
    View screen_pickup_process_payment_service_types_linear_layout_line_four;
    @BindView(R.id.screen_pickup_process_payment_service_types_linear_layout_line_five)
    View screen_pickup_process_payment_service_types_linear_layout_line_five;
    @BindView(R.id.screen_pickup_process_payment_service_types_linear_layout_line_six)
    View screen_pickup_process_payment_service_types_linear_layout_line_six;
    @BindView(R.id.screen_pickup_process_payment_service_types_bottom_border_view)
    View screen_pickup_process_payment_service_types_bottom_border_view;
    @BindView(R.id.screen_pickup_process_payment_total_services_relative_layout)
    View screen_pickup_process_payment_total_services_relative_layout;
    @BindView(R.id.screen_pickup_process_payment_total_services_left_text_view)
    View screen_pickup_process_payment_total_services_left_text_view;
    @BindView(R.id.screen_pickup_process_payment_total_services_right_text_view)
    View screen_pickup_process_payment_total_services_right_text_view;
    @BindView(R.id.screen_pickup_process_payment_total_without_tax_relative_layout)
    View screen_pickup_process_payment_total_without_tax_relative_layout;
    @BindView(R.id.screen_pickup_process_payment_total_without_tax_left_text_view)
    View screen_pickup_process_payment_total_without_tax_left_text_view;
    @BindView(R.id.screen_pickup_process_payment_total_without_tax_right_text_view)
    View screen_pickup_process_payment_total_without_tax_right_text_view;
    @BindView(R.id.screen_pickup_process_payment_value_added_tax_relative_layout)
    View screen_pickup_process_payment_value_added_tax_relative_layout;
    @BindView(R.id.screen_pickup_process_payment_value_added_tax_left_text_view)
    View screen_pickup_process_payment_value_added_tax_left_text_view;
    @BindView(R.id.screen_pickup_process_payment_value_added_tax_right_text_view)
    View screen_pickup_process_payment_value_added_tax_right_text_view;
    @BindView(R.id.screen_pickup_process_payment_total_amount_relative_layout)
    View screen_pickup_process_payment_total_amount_relative_layout;
    @BindView(R.id.screen_pickup_process_payment_total_amount_left_text_view)
    View screen_pickup_process_payment_total_amount_left_text_view;
    @BindView(R.id.screen_pickup_process_payment_total_amount_right_text_view)
    View screen_pickup_process_payment_total_amount_right_text_view;
    @BindView(R.id.screen_pickup_process_payment_total_services_bottom_border_view)
    View screen_pickup_process_payment_total_services_bottom_border_view;
    @BindView(R.id.screen_pickup_process_payment_method_relative_layout)
    View screen_pickup_process_payment_method_relative_layout;
    @BindView(R.id.screen_pickup_process_payment_method_left_text_view)
    View screen_pickup_process_payment_method_left_text_view;
    @BindView(R.id.screen_pickup_process_payment_method_right_text_view)
    View screen_pickup_process_payment_method_right_text_view;
    @BindView(R.id.screen_pickup_process_payment_method_bottom_border_view)
    View screen_pickup_process_payment_method_bottom_border_view;
    @BindView(R.id.screen_pickup_process_payment_terms_and_conditions_label)
    View screen_pickup_process_payment_terms_and_conditions_label;
    @BindView(R.id.screen_pickup_process_payment_terms_and_conditions_details)
    View screen_pickup_process_payment_terms_and_conditions_details;
    @BindView(R.id.screen_pickup_process_payment_main_layout)
    View screen_pickup_process_payment_main_layout;
    @BindView(R.id.screen_pickup_process_payment_main_progress)
    View screen_pickup_process_payment_main_progress;

}
