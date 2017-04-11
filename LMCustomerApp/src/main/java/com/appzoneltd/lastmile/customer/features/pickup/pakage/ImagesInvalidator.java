package com.appzoneltd.lastmile.customer.features.pickup.pakage;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.logs.SystemLogger;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.presentation.ValidatorViewModel;
import com.base.presentation.listeners.OnEventListener;
import com.base.presentation.views.validators.ValidatorCommand;

/**
 * A {@link CommandExecutor} to invalidate the Views in related to Camera and images
 * in {@link PackageDetailsViewModel}
 * <p/>
 * Created by Ahmed Adel on 10/3/2016.
 */
class ImagesInvalidator extends CommandExecutor<Long, View> {


    private final Drawable imageOff = AppResources.drawable(R.drawable.img_upload);
    private boolean firstPhotoLayoutClicked;
    private boolean secondPhotoLayoutClicked;
    private boolean drawOnFirstLayout;
    private boolean drawOnSecondLayout;
    private boolean showDeleteFirstImage;
    private boolean showDeleteSecondPhoto;
    private Command<?, ?> onImagePopupWindowDismissCommand;
    private Bitmap firstPhotoBitmap;
    private Bitmap secondPhotoBitmap;
    private PopupWindow popupWindow;
    private boolean popupWindowItemSelected;
    private OnEventListener onEventListener;


    ImagesInvalidator(ValidatorViewModel viewModel, OnEventListener eventListener) {
        this.onEventListener = eventListener;
        Command<View, Void> command = createOnInvalidateFirstCaptureLayoutCommand();
        put((long) R.id.first_capture_layout, command);
        command = createOnInvalidateSecondCaptureLayoutCommand();
        put((long) R.id.second_capture_layout, command);
        command = createOnInvalidatePackageFirstPhotoCommand(R.id.package_first_photo, viewModel);
        put((long) (R.id.package_first_photo), command);
        command = createOnInvalidatePackageSecondPhotoCommand();
        put((long) R.id.package_second_photo, command);
        command = createOnInvalidateRemoveFirstPhotoCommand();
        put((long) R.id.remove_first_photo, command);
        command = createOnInvalidateRemoveSecondPhotoCommand();
        put((long) R.id.remove_second_photo, command);

    }


    private Command<View, Void> createOnInvalidateFirstCaptureLayoutCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                ViewGroup viewGroup = (ViewGroup) view;
                if (firstPhotoLayoutClicked && firstPhotoBitmap == null) {
                    showPopupWindow(viewGroup);
                }
                return null;
            }
        };
    }


    private Command<View, Void> createOnInvalidateSecondCaptureLayoutCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                if (secondPhotoLayoutClicked && secondPhotoBitmap == null) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    showPopupWindow(viewGroup);
                } else if (!firstPhotoLayoutClicked) {
                    hidePopupWindow();
                }
                return null;
            }
        };
    }

    private Command<View, Void> createOnInvalidatePackageFirstPhotoCommand(int id, ValidatorViewModel validatorViewModel) {
        return new ValidatorCommand<View>(id, validatorViewModel) {
            @Override
            public Void execute(View view) {
                ImageView firstPhoto = (ImageView) view;
                firstPhoto.setOnTouchListener(onEventListener);
                if (drawOnFirstLayout) {
                    if (firstPhotoBitmap != null) {
                        firstPhoto.setImageBitmap(firstPhotoBitmap);
                    } else {
                        firstPhoto.setImageDrawable(imageOff);
                    }
                }
                setError(firstPhotoBitmap == null);
                return null;
            }
        };
    }


    private Command<View, Void> createOnInvalidatePackageSecondPhotoCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                ImageView secondPhoto = (ImageView) view;
                secondPhoto.setOnTouchListener(onEventListener);
                if (drawOnSecondLayout) {
                    if (secondPhotoBitmap != null) {
                        secondPhoto.setImageBitmap(secondPhotoBitmap);
                    } else {
                        secondPhoto.setImageDrawable(imageOff);
                    }
                }
                return null;
            }
        };
    }

    private Command<View, Void> createOnInvalidateRemoveFirstPhotoCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                ImageView imageView = (ImageView) view;
                if (showDeleteFirstImage) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }
                return null;
            }
        };
    }

    private Command<View, Void> createOnInvalidateRemoveSecondPhotoCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                ImageView imageView = (ImageView) view;
                if (showDeleteSecondPhoto) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }
                return null;
            }
        };
    }

    private void showPopupWindow(ViewGroup viewGroup) {
        try {
            Rect rect = drawPopupRectangle(viewGroup);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (popupWindow != null) {
                        if (onImagePopupWindowDismissCommand != null) {
                            onImagePopupWindowDismissCommand.execute(null);
                        }
                    }
                }
            });
            popupWindow.showAtLocation(viewGroup, Gravity.LEFT | Gravity.TOP, rect.left, rect.bottom);
        } catch (Exception e) {
            SystemLogger.getInstance().exception(e);
        }
    }

    private Rect drawPopupRectangle(ViewGroup viewGroup) {
        int[] loc_int = new int[2];
        if (viewGroup == null) return null;
        try {
            viewGroup.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            return null;
        }
        Rect rect = new Rect();
        rect.left = loc_int[0];
        rect.top = loc_int[1];
        rect.right = rect.left + viewGroup.getWidth();
        rect.bottom = (rect.top + (viewGroup.getHeight()) / 3);
        return rect;
    }

    private void hidePopupWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }

    }

    void setFirstPhotoLayoutClicked(boolean firstPhotoLayoutClicked) {
        this.firstPhotoLayoutClicked = firstPhotoLayoutClicked;
    }

    void setSecondPhotoLayoutClicked(boolean secondPhotoLayoutClicked) {
        this.secondPhotoLayoutClicked = secondPhotoLayoutClicked;
    }

    void setFirstPhotoBitmap(Bitmap firstPhotoBitmap) {
        this.firstPhotoBitmap = firstPhotoBitmap;
    }

    void setSecondPhotoBitmap(Bitmap secondPhotoBitmap) {
        this.secondPhotoBitmap = secondPhotoBitmap;
    }

    void setPopupWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }


    void setDrawOnFirstLayout(boolean drawOnFirstLayout) {
        this.drawOnFirstLayout = drawOnFirstLayout;
    }

    void setDrawOnSecondLayout(boolean drawOnSecondLayout) {
        this.drawOnSecondLayout = drawOnSecondLayout;
    }

    void setShowDeleteFirstImage(boolean showDeleteFirstImage) {
        this.showDeleteFirstImage = showDeleteFirstImage;
    }

    void setShowDeleteSecondPhoto(boolean showDeleteSecondPhoto) {
        this.showDeleteSecondPhoto = showDeleteSecondPhoto;
    }


    boolean isFirstPhotoLayoutClicked() {
        return firstPhotoLayoutClicked;
    }

    boolean isSecondPhotoLayoutClicked() {
        return secondPhotoLayoutClicked;
    }

    void setOnImagePopupWindowDismissCommand(Command<?, ?> onImagePopupWindowDismissCommand) {
        this.onImagePopupWindowDismissCommand = onImagePopupWindowDismissCommand;
    }

    boolean isPopupWindowItemSelected() {
        return popupWindowItemSelected;
    }

    void setPopupWindowItemSelected(boolean popupWindowItemSelected) {
        this.popupWindowItemSelected = popupWindowItemSelected;
    }

}
