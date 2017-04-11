package com.appzoneltd.lastmile.customer.features.pickup.pakage;

import android.widget.SeekBar;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.models.Package;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.logs.SystemLogger;
import com.base.abstraction.system.App;
import com.base.presentation.base.presentation.PresenterUpdater;
import com.base.presentation.commands.RequestBasedCommand;
import com.base.presentation.commands.RequesterCommand;

/**
 * a {@link PresenterUpdater} for {@link PackageDetailsPresenter}
 * <p>
 * Created by Ahmed Adel on 10/17/2016.
 */
class PackageDetailsUpdater
        extends PresenterUpdater<PackageDetailsPresenter, PackageDetailsViewModel, PickupModel> {

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener;

    PackageDetailsUpdater() {
        this.onSeekBarChangeListener = createOnSeekBarChangeListener();
    }

    private SeekBar.OnSeekBarChangeListener createOnSeekBarChangeListener() {
        return new SeekBar.OnSeekBarChangeListener() {

            private final int SEEK_BAR_MINIMUM_VALUE = 1;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    progress = SEEK_BAR_MINIMUM_VALUE;
                }
                getModel().getPackage().setBoxWeight(progress);
                getViewModel().setSeekBarValue(progress);
                getViewModel().invalidateViews();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                getViewModel().invalidateViews();
            }


        };
    }

    @Override
    public void onUpdateViewModel() {
        Package pack = getModel().getPackage();
        PackageDetailsViewModel viewModel = getViewModel();
        viewModel.setOnSeekBarChanged(onSeekBarChangeListener);
        viewModel.setDrawOnFirstLayout(pack.hasDrawOnFirstLayout());
        viewModel.setDrawOnSecondLayout(pack.hasDrawOnSecondLayout());
        viewModel.setShowDeleteFirstImage(pack.hasShowDeleteFirstPhoto());
        viewModel.setShowDeleteSecondPhoto(pack.hasShowDeleteSecondPhoto());
        viewModel.setFirstPhotoLayoutClicked(pack.isFirstPhotoLayoutClicked());
        viewModel.setSecondPhotoLayoutClicked(pack.isSecondPhotoLayoutClicked());
        viewModel.setFirstPhotoBitmap(pack.getFirstPhotoBitmap());
        viewModel.setSecondPhotoBitmap(pack.getSecondPhotoBitmap());
        viewModel.setSeekBarValue(pack.getBoxWeight());
        viewModel.setSeekBarEnabled(pack.isBoxSelected());
        viewModel.setLabelingBox(pack.isWrapAndLabel());
        viewModel.setPackageDescriptionValue(pack.getDescription());
        viewModel.setPackageNicknameValue(pack.getNickname());
        viewModel.setCustomLabelValue(pack.getLabelMessage());
        viewModel.setPackagingBoxChecked(pack.hasPackagingBox());
        viewModel.setWrapAndLabelChecked(pack.isWrapAndLabel());
        viewModel.setPaymentTypes(pack.getPaymentType());
        createPackageTypesBasedCommand(getPresenter().getPackageTypesRequesterCommand()).execute(null);
        viewModel.invalidateViews();
    }


    @Override
    public void onUpdateModel() {
        Package pack = getModel().getPackage();
        PackageDetailsViewModel viewModel = getViewModel();
        pack.setDescription(viewModel.getPackageDescriptionValue());
        pack.setNickname(viewModel.getPackageNicknameValue());
        pack.setLabelMessage(viewModel.getCustomLabelValue());
        pack.updatePackageTypeTextAndWeight(viewModel.isSeekBarEnabled());

    }

    private RequestBasedCommand<Void> createPackageTypesBasedCommand(RequesterCommand requesterCmd) {
        return new RequestBasedCommand<Void>(requesterCmd) {
            @Override
            protected Void onExecute(Message message) {
                SystemLogger.getInstance().error(PackageDetailsPresenter.class,
                        "onResume : onSuccess()");
                return null;
            }

            @Override
            protected Void onRequesting(Message p) {
                SystemLogger.getInstance().error(PackageDetailsPresenter.class,
                        "onResume : onRequesting()");
                return null;
            }

            @Override
            protected Void onFailure(Message message) {
                App.getInstance().getActorSystem()
                        .get((long) R.id.addressActivity).execute(createToastEvent());
                return null;
            }

            private Event createToastEvent() {
                Message message = new Message.Builder()
                        .content(R.string.package_types_response_error).build();
                return new Event.Builder(R.id.showToast)
                        .message(message)
                        .build();

            }

        };
    }

    @Override
    public void onClear() {
        onSeekBarChangeListener = null;
    }
}
