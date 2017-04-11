package com.appzoneltd.lastmile.customer.features.pickup.host;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.pakage.PackageDetailsFragment;
import com.appzoneltd.lastmile.customer.features.pickup.recipient.RecipientDetailsFragment;
import com.appzoneltd.lastmile.customer.features.pickup.review.PickupReviewFragment;
import com.appzoneltd.lastmile.customer.features.pickup.scheduled.PickupScheduledFragment;
import com.appzoneltd.lastmile.customer.subfeatures.Toolbar.ToolbarInvalidator;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.logs.SystemLogger;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.ViewModel;


/**
 * A Presenter to handle the Fragments attached to the Pickup Activity
 * <p/>
 * Created by Wafaa on 9/25/2016.
 */
class PickupFragmentsViewModel extends ViewModel {


    private boolean pickupScheduledVisibility;
    private boolean packageDetailsVisibility;
    private boolean recipientDetailsVisibility;
    private boolean reviewVisibility;

    private Fragment currentFragment;
    private PickupScheduledFragment pickupScheduledFragment;
    private PackageDetailsFragment packageDetailsFragment;
    private RecipientDetailsFragment recipientDetailsFragment;
    private PickupReviewFragment pickupReviewFragment;


    PickupFragmentsViewModel(ViewBinder viewBInder) {
        super(viewBInder);
        pickupScheduledFragment = new PickupScheduledFragment();
        packageDetailsFragment = new PackageDetailsFragment();
        recipientDetailsFragment = new RecipientDetailsFragment();
        pickupReviewFragment = new PickupReviewFragment();
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, View> createInvalidateCommands() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command = createOnInvalidateFragmentsVisibilityCommand();
        commandExecutor.put((long) R.id.fragment_container, command);
        command = createOnInvalidateToolbarTitle();
        commandExecutor.put((long) R.id.pickup_toolbar, command);
        return commandExecutor;
    }


    private Command<View, Void> createOnInvalidateFragmentsVisibilityCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                if (pickupScheduledVisibility) {
                    onFragmentSelected(pickupScheduledFragment);
                } else if (packageDetailsVisibility) {
                    onFragmentSelected(packageDetailsFragment);
                } else if (recipientDetailsVisibility) {
                    onFragmentSelected(recipientDetailsFragment);
                } else if (reviewVisibility) {
                    onFragmentSelected(pickupReviewFragment);
                }
                return null;
            }

            private void onFragmentSelected(Fragment fragment) {
                if (currentFragment != null) {
                    removeCurrentFragment();
                }
                attachFragmentAndSetToCurrent(fragment);
            }

            private void removeCurrentFragment() {
                getFeature().getHostActivity().removeFragment(currentFragment);
            }

            private void attachFragmentAndSetToCurrent(Fragment fragment) {
                if (currentFragment != null && currentFragment.equals(fragment)) {
                    logAttachFragmentError(fragment);
                } else {
                    doAttachFragmentAndSetCurrent(fragment);
                }

            }

            private void doAttachFragmentAndSetCurrent(Fragment fragment) {
                getFeature().addFragment(R.id.fragment_container, fragment);

                SystemLogger.getInstance().error(getClass(), "fragment attached :"
                        + fragment.getClass().getSimpleName());

                currentFragment = fragment;
            }

            private void logAttachFragmentError(Fragment fragment) {
                SystemLogger.getInstance().error(getClass(), "attaching fragment again :"
                        + fragment.getClass().getSimpleName());
            }
        };
    }


    void setPickupScheduledVisibility(boolean pickupScheduledVisibility) {
        this.pickupScheduledVisibility = pickupScheduledVisibility;
    }

    void setPackageDetailsVisibility(boolean packageDetailsVisibility) {
        this.packageDetailsVisibility = packageDetailsVisibility;
    }

    void setRecipientDetailsVisibility(boolean recipientDetailsVisibility) {
        this.recipientDetailsVisibility = recipientDetailsVisibility;
    }

    void setReviewVisibility(boolean reviewVisibility) {
        this.reviewVisibility = reviewVisibility;
    }

    private Command<View, Void> createOnInvalidateToolbarTitle() {
        return new ToolbarInvalidator(this);
    }

    @Override
    public void onDestroy() {
        currentFragment = null;
        pickupScheduledFragment = null;
        packageDetailsFragment = null;
        recipientDetailsFragment = null;
        pickupReviewFragment = null;
    }

}
