package com.appzoneltd.lastmile.customer.features.pickup.review;

import android.view.View;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;

/**
 * a {@link PickupReviewInvalidator} that invalidates Views after the submit button
 * (Submit button is NOT invalidated in this class)
 * <p/>
 * Created by Ahmed Adel on 9/27/2016.
 *
 * @see PickupReviewInvalidator
 * @see PickupReviewPreSubmitInvalidator
 */
class PickupReviewPostSubmitInvalidator extends PickupReviewInvalidator {

    public PickupReviewPostSubmitInvalidator(PickupReviewViewModel viewModel) {
        super(viewModel);
        Command<View, Void> command = createOnNicknameInvalidateCommand();
        put((long) R.id.pickup_review_nickname_txt, command);
        command = createOnPackageTypeInvalidateCommand();
        put((long) R.id.pickup_review_package_type_txt, command);
        command = createOnPackageWeightInvalidateCommand();
        put((long) R.id.pickup_review_package_weight_txt, command);
        command = createOnWhatsInsideInvalidateCommand();
        put((long) R.id.pickup_review_whats_inside_txt, command);
        command = createOnAdditionalServicesInvalidateCommand();
        put((long) R.id.pickup_review_additional_service_txt, command);
        command = createOnRecipientNameInvalidateCommand();
        put((long) R.id.pickup_review_recipient_name_txt, command);
        command = createOnRecipientPhoneInvalidateCommand();
        put((long) R.id.pickup_review_recioient_phone_no_txt, command);
        command = createOnRecipientAddressInvalidateCommand();
        put((long) R.id.pickup_review_recipient_address_txt, command);
        command = createOnRecipientNotesInvalidateCommand();
        put((long) R.id.pickup_review_recipient_notes_txt, command);
    }

    private Command<View, Void> createOnNicknameInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                updateTextView(p, getViewModel().getNickname());
                return null;
            }
        };
    }

    private Command<View, Void> createOnPackageTypeInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                updateTextView(p, getViewModel().getPackageType());
                return null;
            }
        };
    }

    private Command<View, Void> createOnPackageWeightInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                updateTextView(p, getViewModel().getPackageWeight());
                return null;
            }
        };
    }

    private Command<View, Void> createOnWhatsInsideInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                updateTextView(p, getViewModel().getWhatsInside());
                return null;
            }
        };
    }

    private Command<View, Void> createOnAdditionalServicesInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                updateTextView(p, getViewModel().getAdditionalService());
                return null;
            }
        };
    }

    private Command<View, Void> createOnRecipientNameInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                updateTextView(p, getViewModel().getRecipientName());
                return null;
            }
        };
    }

    private Command<View, Void> createOnRecipientPhoneInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                updateTextView(p, getViewModel().getRecipientPhoneNumber());
                return null;
            }
        };
    }

    private Command<View, Void> createOnRecipientAddressInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                updateTextView(p, getViewModel().getRecipientAddress());
                return null;
            }
        };
    }

    private Command<View, Void> createOnRecipientNotesInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                updateTextView(p, getViewModel().getRecipientNotes());
                return null;
            }
        };
    }
}
