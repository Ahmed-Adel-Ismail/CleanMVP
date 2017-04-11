package com.appzoneltd.lastmile.customer.features.pickup.pakage;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.deprecated.utilities.BitmapUtils;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.logs.SystemLogger;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.commands.CashedRequesterCommand;
import com.base.presentation.listeners.OnTouchParams;
import com.base.presentation.requests.ActivityResult;
import com.base.presentation.requests.PermissionResult;
import com.base.usecases.events.ResponseMessage;

import java.io.IOException;

/**
 * A Presenter for Package details Screen
 * <p/>
 * Created by Wafaa on 9/22/2016.
 */
class PackageDetailsPresenter
        extends Presenter<PackageDetailsPresenter, PackageDetailsViewModel, PickupModel> {

    private PackageDetailsOnClickExecutor packageDetailsOnClickExecutor;
    private CashedRequesterCommand packageTypesRequesterCommand;
    private OnTouchExecutor onTouchExecutor;


    @Override
    public void preInitialize() {
        this.packageDetailsOnClickExecutor = new PackageDetailsOnClickExecutor(this, getViewModel());
        this.packageTypesRequesterCommand = createPackageTypesRequesterCommand();
        this.onTouchExecutor = new OnTouchExecutor(getViewModel(), this);
    }

    PackageDetailsPresenter(PackageDetailsViewModel viewModel) {
        super(viewModel);
        setUpdater(new PackageDetailsUpdater());
        getViewModel().setOnImagePopupWindowDismissCommand(createOnImagePopupWindowDismissCommand());
        updateViews();
    }

    @NonNull
    private Command<Void, Void> createOnImagePopupWindowDismissCommand() {
        return new Command<Void, Void>() {
            @Override
            public Void execute(Void p) {
                if (!getViewModel().isPopupWindowItemSelected()) {
                    getViewModel().setFirstPhotoLayoutClicked(false);
                    getViewModel().setSecondPhotoLayoutClicked(false);
                    getModel().getPackage().setFirstPhotoLayoutClicked(false);
                    getModel().getPackage().setSecondPhotoLayoutClicked(false);
                }
                return null;
            }
        };
    }


    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createResponseCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.requestPackageTypes, packageTypesRequesterCommand);
        return commandExecutor;
    }

    private CashedRequesterCommand createPackageTypesRequesterCommand() {
        return new CashedRequesterCommand(this, R.id.requestPackageTypes) {
            @Override
            public boolean isDataCashed() {
                if (getModel().getPackage().getPackageTypes() != null
                        && getModel().getPackage().getPackageTypes().size() > 0) {
                    return true;
                }
                return false;
            }

            @Override
            protected boolean request() {
                Event event = new Event.Builder(R.id.requestPackageTypes).build();
                getModel().execute(event);
                return true;
            }
        };
    }

    private void updateViews() {
        onUpdateViewModel();
    }

    @NonNull
    protected CommandExecutor<Long, View> createOnClickCommandExecutor() {
        return packageDetailsOnClickExecutor;
    }


    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnRequestPermissionCommand();
        commandExecutor.put((long) R.id.onRequestPermissionsResult, command);
        command = createOnActivityResultCommand();
        commandExecutor.put((long) R.id.onActivityResult, command);
        command = createOnTouchCommand();
        commandExecutor.put((long) R.id.onTouch, command);
        return commandExecutor;
    }

    private Command<Message, Void> createOnRequestPermissionCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                PermissionResult result = message.getContent();
                int cameraPermission = AppResources.integer(R.integer.requestCodeCameraPermission);
                if (result.requestCode == cameraPermission) {
                    if (result.grantResults.length > 0
                            && result.grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        packageDetailsOnClickExecutor.createAndInvalidatePhotoLayout();
                    }
                }
                return null;
            }
        };
    }


    private Command<Message, Void> createOnActivityResultCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                ActivityResult result = message.getContent();
                if (result.isResultCodeOk()) {
                    drawReceivedImage(result);
                }
                getModel().getPackage().setFirstPhotoLayoutClicked(false);
                getModel().getPackage().setSecondPhotoLayoutClicked(false);
                getViewModel().setFirstPhotoLayoutClicked(false);
                getViewModel().setSecondPhotoLayoutClicked(false);
                getViewModel().setPopupWindowItemSelected(false);
                getViewModel().invalidateViews();
                return null;
            }

            private void drawReceivedImage(ActivityResult result) {
                if (result.hasRequestCode(R.integer.requestCodeCapturePhoto)) {
                    drawCapturedImage(result);
                } else if (result.hasRequestCode(R.integer.requestCodeChooseImage)) {
                    drawImageFromGallery(result);
                }
            }

            private void drawCapturedImage(ActivityResult result) {
                Bitmap bitmap;
                bitmap = (Bitmap) result.data.getExtras().get("data");
                if (bitmap != null) {
                    bitmap = BitmapUtils.resizeImageForImageView(bitmap);
                    if (bitmap != null) {
                        drawOnPhotoLayout(bitmap);
                        getModel().getPackage().setShowImageErrorMsg(false);
                    }
                }
            }

            private void drawImageFromGallery(ActivityResult result) {
                Bitmap bitmap;
                Uri uri = result.data.getData();
                if (uri != null) {
                    bitmap = getBitmapFromGallery(uri);
                    bitmap = BitmapUtils.resizeImageForImageView(bitmap);
                    if (bitmap != null) {
                        drawOnPhotoLayout(bitmap);
                        getModel().getPackage().setShowImageErrorMsg(false);
                    }
                }
            }

            private Bitmap getBitmapFromGallery(Uri uri) {
                Bitmap bitmap;
                bitmap = BitmapUtils.getImageFromGallery(getHostActivity(), uri);
                try {
                    bitmap = BitmapUtils.modifyOrientation(getHostActivity(), bitmap, uri);
                } catch (IOException e) {
                    SystemLogger.getInstance().exception(e);
                }
                return bitmap;
            }

        };
    }


    private void drawOnPhotoLayout(Bitmap bitmap) {
        if (getModel().getPackage().isFirstPhotoLayoutClicked()) {
            drawOnFirstPhotoLayout(bitmap);
        } else if (getModel().getPackage().isSecondPhotoLayoutClicked()) {
            drawOnFirstPhotoLayoutIfPossible(bitmap);
        }
    }

    private void drawOnFirstPhotoLayoutIfPossible(Bitmap bitmap) {
        if (getModel().getPackage().getFirstPhotoBitmap() == null) {
            drawOnFirstPhotoLayout(bitmap);
        } else {
            drawOnSecondPhotoLayout(bitmap);
        }
    }

    private void drawOnFirstPhotoLayout(Bitmap bitmap) {

        getModel().getPackage().setFirstPhotoBitmap(bitmap);
        getViewModel().setFirstPhotoBitmap(bitmap);

        getModel().getPackage().setDrawOnFirstLayout(true);
        getViewModel().setDrawOnFirstLayout(true);

        getModel().getPackage().setShowDeleteFirstPhoto(true);
        getViewModel().setShowDeleteFirstImage(true);

        getViewModel().setFirstPhotoLayoutClicked(false);
    }

    private void drawOnSecondPhotoLayout(Bitmap bitmap) {

        getModel().getPackage().setSecondPhotoBitmap(bitmap);
        getViewModel().setSecondPhotoBitmap(bitmap);

        getModel().getPackage().setDrawOnSecondLayout(true);
        getViewModel().setDrawOnSecondLayout(true);

        getModel().getPackage().setShowDeleteSecondPhoto(true);
        getViewModel().setShowDeleteSecondPhoto(true);

        getViewModel().setSecondPhotoLayoutClicked(false);
    }

    CashedRequesterCommand getPackageTypesRequesterCommand() {
        return packageTypesRequesterCommand;
    }

    private Command<Message, Void> createOnTouchCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                OnTouchParams params = p.getContent();
                onTouchExecutor.execute(p.getId(), params);
                return null;
            }
        };
    }

    @Override
    public void onDestroy() {

        if (packageTypesRequesterCommand != null) {
            packageTypesRequesterCommand.clear();
            packageTypesRequesterCommand = null;
        }

        if (packageDetailsOnClickExecutor != null) {
            packageDetailsOnClickExecutor.clear();
            packageDetailsOnClickExecutor = null;
        }
    }
}
