package com.appzoneltd.lastmile.customer.features.pickup.pakage;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Toast;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.models.Package;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.system.AppResources;

/**
 * a {@link CommandExecutor} for handeling onClicks for the {@link PackageDetailsPresenter}
 * <p/>
 * Created by Ahmed Adel on 10/4/2016.
 */
class PackageDetailsOnClickExecutor extends CommandExecutor<Long, View> {


    private PackageDetailsPresenter presenter;
    private PackageDetailsViewModel viewModel;

    PackageDetailsOnClickExecutor(PackageDetailsPresenter presenter,
                                  PackageDetailsViewModel viewModel) {
        this.presenter = presenter;
        this.viewModel = viewModel;
        Command<View, Void> command = createOnDocumentClickedCommand();
        put((long) R.id.document, command);
        command = createOnBoxClickedCommand();
        put((long) R.id.box, command);
        command = createOnFirstPhotoLayoutClickedCommand();
        put((long) R.id.package_first_photo, command);
        command = createOnSecondPhotoLayoutClickedCommand();
        put((long) R.id.package_second_photo, command);
        command = createOnPayAtPickupClickedCommand();
        put((long) R.id.rb_pickup, command);
        command = createOnPayAtDeliveryCommand();
        put((long) R.id.rb_delivery, command);
        command = createOnPackagingClickedCommand();
        put((long) R.id.packaging_box, command);
        command = createOnLabelingClickedCommand();
        put((long) R.id.wrap_label_package, command);
        command = createOnCaptureClickListener();
        put((long) R.id.capture, command);
        command = createOnChooseImageCommand();
        put((long) R.id.choose_image, command);
        command = createOnRemoveFirstImageCommand();
        put((long) R.id.remove_first_photo, command);
        command = createOnRemoveSecondImageCommand();
        put((long) R.id.remove_second_photo, command);
        command = createOnNextButtonClickedCommand();
        put((long) R.id.next_recipient_details, command);
    }


    public PickupModel getModel() {
        return presenter.getModel();
    }

    public PackageDetailsViewModel getViewModel() {
        return viewModel;
    }

    private Command<View, Void> createOnDocumentClickedCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                updateModelAndPackageTypeViews(false);
                return null;
            }

        };
    }

    private Command<View, Void> createOnBoxClickedCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                updateModelAndPackageTypeViews(true);
                return null;
            }
        };
    }

    private void updateModelAndPackageTypeViews(boolean seekBarVisible) {
        getModel().getPackage().updatePackageTypeTextAndWeight(seekBarVisible);
        getModel().getPackage().setBoxSelected(seekBarVisible);
        getViewModel().setSeekBarEnabled(seekBarVisible);
        getViewModel().invalidateViews();
    }


    private Command<View, Void> createOnFirstPhotoLayoutClickedCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                updateModelAndPhotoLayoutClickedViews(true, false);
                if (presenter.getManifestPermissions().isTakeAndCaptureImagePermissionAllowed()) {
                    createAndInvalidatePhotoLayout();
                } else {
                    presenter.getManifestPermissions().openCameraPermissionDialog(R.integer.requestCodeCameraPermission);
                }
                return null;
            }
        };
    }

    private Command<View, Void> createOnSecondPhotoLayoutClickedCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                updateModelAndPhotoLayoutClickedViews(false, true);
                if (presenter.getManifestPermissions().isTakeAndCaptureImagePermissionAllowed()) {
                    createAndInvalidatePhotoLayout();
                } else {
                    presenter.getManifestPermissions().openCameraPermissionDialog(R.integer.requestCodeCameraPermission);
                }
                return null;
            }
        };
    }

    private void updateModelAndPhotoLayoutClickedViews(boolean firstPhotoLayoutClicked,
                                                       boolean secondPhotoLayoutClicked) {
        getModel().getPackage().setFirstPhotoLayoutClicked(firstPhotoLayoutClicked);
        getViewModel().setFirstPhotoLayoutClicked(firstPhotoLayoutClicked);

        getModel().getPackage().setSecondPhotoLayoutClicked(secondPhotoLayoutClicked);
        getViewModel().setSecondPhotoLayoutClicked(secondPhotoLayoutClicked);
    }

    void createAndInvalidatePhotoLayout() {
        View popupMenuLayout = createPopupMenuLayout();
        getViewModel().setPopupWindow(createPopupWindow(popupMenuLayout));
        getViewModel().invalidateViews();
    }

    private Command<View, Void> createOnPayAtPickupClickedCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                RadioButton radioButton = (RadioButton) view;
                if (radioButton.isChecked()) {
                    getViewModel().setPaymentTypes(Package.PaymentTypes.PICKUP);
                    getModel().getPackage().setPaymentType(Package.PaymentTypes.PICKUP);
                }
                return null;
            }
        };
    }

    private Command<View, Void> createOnPayAtDeliveryCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                RadioButton radioButton = (RadioButton) view;
                if (radioButton.isChecked()) {
                    getViewModel().setPaymentTypes(Package.PaymentTypes.DELIVERY);
                    getModel().getPackage().setPaymentType(Package.PaymentTypes.DELIVERY);
                }
                return null;
            }
        };
    }

    private Command<View, Void> createOnPackagingClickedCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                CheckBox checkBox = (CheckBox) view;
                boolean checked = checkBox.isChecked();

                getViewModel().setPackagingBoxChecked(checked);
                getModel().getPackage().setPackagingBox(checked);

                String service = (checked) ? AppResources.string(R.string.packaging_box) : null;
                getModel().getPackage().setAdditionalServices(service);

                return null;
            }
        };
    }

    private Command<View, Void> createOnLabelingClickedCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {

                CheckBox checkBox = (CheckBox) view;
                boolean wrapAndLabel = checkBox.isChecked();

                getModel().getPackage().setWrapAndLabel(wrapAndLabel);
                getModel().getPackage().setShowCustomLabelErrorMsg(false);

                getViewModel().setWrapAndLabelChecked(wrapAndLabel);
                getViewModel().setLabelingBox(wrapAndLabel);

                getViewModel().invalidateViews();

                return null;
            }
        };
    }


    @SuppressLint("InflateParams")
    private View createPopupMenuLayout() {
        View popupMenuLayout;
        LayoutInflater mInflater = (LayoutInflater) presenter.getHostActivity().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupMenuLayout = mInflater.inflate(R.layout.popup_menu, null);
        popupMenuLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return popupMenuLayout;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private PopupWindow createPopupWindow(View popupMenuLayout) {
        PopupWindow popupWindow;
        popupWindow = new PopupWindow(popupMenuLayout, FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(AppResources.drawable(R.color.white));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setElevation(4);
        return popupWindow;
    }

    private Command<View, Void> createOnCaptureClickListener() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                hidePhotoLayoutClicked();
                captureImage(AppResources.integer(R.integer.requestCodeCapturePhoto));
                return null;
            }
        };
    }

    private void captureImage(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(presenter.getHostActivity().getPackageManager()) != null) {
            presenter.getHostActivity().startActivityForResult(takePictureIntent, requestCode);
        }
    }

    private Command<View, Void> createOnChooseImageCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                hidePhotoLayoutClicked();
                openGallery(AppResources.integer(R.integer.requestCodeChooseImage));
                return null;
            }
        };
    }

    private void hidePhotoLayoutClicked() {
        getViewModel().setPopupWindowItemSelected(true);
        getViewModel().setFirstPhotoLayoutClicked(false);
        getViewModel().setSecondPhotoLayoutClicked(false);
        getViewModel().invalidateViews();

    }

    private void openGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        presenter.getHostActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }


    private Command<View, Void> createOnRemoveFirstImageCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                getModel().getPackage().setFirstRemoveImageClicked(true);
                getModel().getPackage().setSecondRemoveImageClicked(false);
                removeImage();
                getViewModel().invalidateViews();
                return null;
            }
        };
    }

    private Command<View, Void> createOnRemoveSecondImageCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                getModel().getPackage().setFirstRemoveImageClicked(false);
                getModel().getPackage().setSecondRemoveImageClicked(true);
                removeImage();
                getViewModel().invalidateViews();
                return null;
            }
        };
    }


    private void removeImage() {
        if (getModel().getPackage().isFirstRemoveImageClicked()) {
            removeAndAdjustImages();
        } else if (getModel().getPackage().isSecondRemoveImageClicked()) {
            removeSecondImage();
        }
    }

    private void removeAndAdjustImages() {
        if (getModel().getPackage().getSecondPhotoBitmap() == null) {
            removeFirstImage();
        } else {
            removeFirstImageAndReplaceWithSecond();
        }
    }

    private void removeFirstImage() {
        getModel().getPackage().setFirstPhotoBitmap(null);
        getViewModel().setFirstPhotoBitmap(null);

        getModel().getPackage().setShowDeleteFirstPhoto(false);
        getViewModel().setShowDeleteFirstImage(false);
    }


    private void removeFirstImageAndReplaceWithSecond() {
        Bitmap bmp = getModel().getPackage().getSecondPhotoBitmap();

        getModel().getPackage().setFirstPhotoBitmap(bmp);
        getViewModel().setFirstPhotoBitmap(bmp);

        removeSecondImage();
    }

    private void removeSecondImage() {
        getModel().getPackage().setSecondPhotoBitmap(null);
        getViewModel().setSecondPhotoBitmap(null);

        getModel().getPackage().setShowDeleteSecondPhoto(false);
        getViewModel().setShowDeleteSecondPhoto(false);
    }


    private Command<View, Void> createOnNextButtonClickedCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                checkErrorsAndUpdateModel();
                validateOnPackageTypes();
                getViewModel().setValidationActive(true);
                getViewModel().invalidateViews();
                return null;
            }

            private void checkErrorsAndUpdateModel() {
                boolean valid = getModel().getPackage().isImageValid();
                getModel().getPackage().setShowImageErrorMsg(!valid);

                valid = getModel().getPackage().isCustomLabelValid();
                getModel().getPackage().setShowCustomLabelErrorMsg(!valid);
            }

            private void validateOnPackageTypes() {
                if (getModel().getPackage().getPackageTypes() == null) {
                    Toast.makeText(viewModel.getFeature().getHostActivity()
                            , AppResources.string(R.string.package_types_response_error)
                            , Toast.LENGTH_SHORT).show();
                }
            }
        };
    }


    @Override
    public void clear() {
        super.clear();
        presenter = null;
        viewModel = null;
    }
}
