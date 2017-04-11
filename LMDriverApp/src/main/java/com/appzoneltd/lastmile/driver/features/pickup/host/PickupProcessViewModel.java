package com.appzoneltd.lastmile.driver.features.pickup.host;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.pickup.states.PickupProcessState;
import com.appzoneltd.lastmile.driver.features.pickup.states.StateLoading;
import com.appzoneltd.lastmile.driver.subfeatures.SnackbarGenerator;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.presentation.references.Consumable;
import com.base.presentation.references.Property;
import com.base.presentation.references.State;
import com.base.presentation.references.Updatable;
import com.base.presentation.base.presentation.ViewModel;

import static com.appzoneltd.lastmile.driver.features.pickup.host.PickupProcessLoadingState.IN_PROCESS;
import static com.appzoneltd.lastmile.driver.features.pickup.host.PickupProcessLoadingState.LOADING;
import static com.appzoneltd.lastmile.driver.features.pickup.host.PickupProcessLoadingState.LOADING_ERROR;
import static com.appzoneltd.lastmile.driver.features.pickup.host.ProgressBars.BOTTOM_LEF;
import static com.appzoneltd.lastmile.driver.features.pickup.host.ProgressBars.BOTTOM_RIGHT;
import static com.appzoneltd.lastmile.driver.features.pickup.host.ProgressBars.CENTER;
import static com.appzoneltd.lastmile.driver.features.pickup.host.ProgressBars.TOP_LEFT;
import static com.appzoneltd.lastmile.driver.features.pickup.host.ProgressBars.TOP_RIGHT;

/**
 * the {@link ViewModel} of the pickup-process screen
 * <p>
 * Created by Ahmed Adel on 12/24/2016.
 */
class PickupProcessViewModel extends ViewModel {

    final Updatable<ProgressBars> progress;
    final Property<PickupProcessLoadingState> loadingState = new Property<>(LOADING);
    final State<PickupProcessState> pickupState = new State<PickupProcessState>(new StateLoading());
    final Consumable<String> snackbarText = new Consumable<>();

    public PickupProcessViewModel() {
        progress = new Updatable<ProgressBars>(TOP_RIGHT) {
            @Override
            protected ProgressBars update(@NonNull ProgressBars object) {
                return object.next();
            }
        };
    }

    // layout :

    @Executable(R.id.screen_host_container_id)
    void onInvalidate(View view) {
        try {
            new SnackbarGenerator(view).execute(snackbarText.consume()).show();
        } catch (CheckedReferenceClearedException e) {
            // do nothing
        }
    }

    @Executable(R.id.screen_pickup_process_main_scroll_view)
    void mainLayout(View view) {
        boolean visible = IN_PROCESS.equals(loadingState.get());
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Executable(R.id.screen_pickup_process_progress_bars_frame_layout)
    void loadingLayout(View view) {
        boolean visible = !IN_PROCESS.equals(loadingState.get());
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Executable(R.id.screen_pickup_process_progress_bar_top_right)
    void topRightProgressBar(ProgressBar progressBar) {
        boolean visible = LOADING.equals(loadingState.get()) && TOP_RIGHT.equals(progress.get());
        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Executable(R.id.screen_pickup_process_progress_bar_top_left)
    void topLeftProgressBar(ProgressBar progressBar) {
        boolean visible = LOADING.equals(loadingState.get()) && TOP_LEFT.equals(progress.get());
        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Executable(R.id.screen_pickup_process_progress_bar_bottom_right)
    void bottomRightProgressBar(ProgressBar progressBar) {
        boolean visible = LOADING.equals(loadingState.get()) && BOTTOM_RIGHT.equals(progress.get());
        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Executable(R.id.screen_pickup_process_progress_bar_bottom_left)
    void bottomLeftProgressBar(ProgressBar progressBar) {
        boolean visible = LOADING.equals(loadingState.get()) && BOTTOM_LEF.equals(progress.get());
        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Executable(R.id.screen_pickup_process_progress_bar_center)
    void centerProgressBar(ProgressBar progressBar) {
        boolean visible = LOADING.equals(loadingState.get()) && CENTER.equals(progress.get());
        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Executable(R.id.screen_pickup_process_progress_failed_relative_layout)
    void loadingErrorLayout(View view) {
        boolean visible = LOADING_ERROR.equals(loadingState.get());
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Executable(R.id.screen_pickup_process_verify_package_fragment_layout)
    void verifyingFragment(View view) {
        boolean visible = pickupState.get().verifyingState.isTrue();
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Executable(R.id.screen_pickup_process_payment_fragment_layout)
    void paymentFragment(View view) {
        boolean visible = pickupState.get().paymentState.isTrue();
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Executable(R.id.screen_pickup_process_documents_fragment_layout)
    void documentsFragment(View view) {
        boolean visible = pickupState.get().documentsState.isTrue();
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


}
