package com.appzoneltd.lastmile.customer.features.pickup.pakage;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.speech.tts.Voice;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.system.AppResources;
import com.base.presentation.listeners.OnTouchParams;

/**
 * Created by Wafaa on 1/5/2017.
 */

class OnTouchExecutor extends CommandExecutor<Long, OnTouchParams> {

    private PackageDetailsViewModel viewModel;
    private PackageDetailsPresenter presenter;

    OnTouchExecutor(PackageDetailsViewModel viewModel, PackageDetailsPresenter presenter) {
        this.viewModel = viewModel;
        this.presenter = presenter;
        Command<OnTouchParams, Void> command;
        command = createOnFirstPhotoLayoutClickedCommand();
        put((long) R.id.package_first_photo, command);
        command = createOnSecondPhotoLayoutClickedCommand();
        put((long) R.id.package_second_photo, command);
        command = createOnTouchNextButton();
        put((long) R.id.next_recipient_details, command);
    }

    private Command<OnTouchParams, Void> createOnFirstPhotoLayoutClickedCommand() {
        return new Command<OnTouchParams, Void>() {
            @Override
            public Void execute(OnTouchParams params) {
                params.getView().performClick();
                return null;
            }
        };
    }


    private Command<OnTouchParams, Void> createOnSecondPhotoLayoutClickedCommand() {
        return new Command<OnTouchParams, Void>() {
            @Override
            public Void execute(OnTouchParams params) {
                params.getView().performClick();
                return null;
            }
        };
    }

    private Command<OnTouchParams, Void> createOnTouchNextButton() {
        return new Command<OnTouchParams, Void>() {
            @Override
            public Void execute(OnTouchParams p) {
                p.getView().performClick();
                return null;
            }
        };
    }
}
