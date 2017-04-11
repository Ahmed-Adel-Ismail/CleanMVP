package com.appzoneltd.lastmile.customer.features.pickup.review;

import android.view.View;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.presentation.base.presentation.ViewModel;

/**
 * a {@link ViewModel} to handle the UI of the optional fields in the Pickup Review screen
 * <p>
 * Created by Wafaa on 10/4/2016.
 */
class PickupReviewOptionalLayoutsInvalidator extends PickupReviewInvalidator {


    PickupReviewOptionalLayoutsInvalidator(PickupReviewViewModel viewModel) {
        super(viewModel);
        Command<View, Void> command = createOnNicknameLayoutInvalidateCommand();
        put((long) R.id.nickname_layout, command);
        command = createOnBriefLayoutCommand();
        put((long) R.id.package_brief_layout, command);
        command = createOnAddServicesInvalidateCommand();
        put((long) R.id.additional_services_layout, command);
        command = createOnNotesInvalidateCommand();
        put((long) R.id.notes_layout, command);
    }


    private Command<View, Void> createOnNicknameLayoutInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                invalidateLayoutVisibility(view, getViewModel().isNicknameVisibility());
                return null;
            }
        };
    }

    private Command<View, Void> createOnBriefLayoutCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                invalidateLayoutVisibility(view, getViewModel().isBriefVisibility());
                return null;
            }
        };
    }

    private Command<View, Void> createOnAddServicesInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                invalidateLayoutVisibility(view, getViewModel().isAdditionalServicesVisibility());
                return null;
            }
        };
    }

    private Command<View, Void> createOnNotesInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                invalidateLayoutVisibility(view, getViewModel().isNotesVisibility());
                return null;
            }
        };
    }


    private void invalidateLayoutVisibility(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }


}
