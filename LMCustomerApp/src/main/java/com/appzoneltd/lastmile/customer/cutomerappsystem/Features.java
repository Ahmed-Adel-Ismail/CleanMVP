package com.appzoneltd.lastmile.customer.cutomerappsystem;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.loaders.LoaderViewBinder;
import com.appzoneltd.lastmile.customer.features.main.packageslist.PackageListViewBinder;
import com.appzoneltd.lastmile.customer.features.main.packageslist.host.PackageListActivityViewBinder;
import com.appzoneltd.lastmile.customer.features.main.packageslist.models.PackageListModel;
import com.appzoneltd.lastmile.customer.features.receipt.ReceiptViewBinder;
import com.appzoneltd.lastmile.customer.features.receipt.models.ReceiptModel;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.presentation.annotations.interfaces.ActivityViewBinders;
import com.base.presentation.annotations.interfaces.FragmentViewBinder;
import com.base.presentation.annotations.interfaces.Menu;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.abstracts.features.AbstractFragment;

/**
 * * an interface that represents the features of the application
 * <p>
 * Created by Wafaa on 12/14/2016.
 */

public interface Features {

    @ActivityViewBinders(initial = PackageListActivityViewBinder.class, error = LoaderViewBinder.class)
    @Menu(R.menu.packages_menu)
    class PackageListActivity extends AbstractActivity<PackageListModel> {
    }

    @Address(R.id.addressPackageListFragment)
    @FragmentViewBinder(PackageListViewBinder.class)
    class PackageListFragment extends AbstractFragment<PackageListModel> {
    }

    @ActivityViewBinders(initial = ReceiptViewBinder.class, error = LoaderViewBinder.class)
    @Address(R.id.addressReceiptActivity)
    class ReceiptActivity extends AbstractActivity<ReceiptModel>{

    }

}
