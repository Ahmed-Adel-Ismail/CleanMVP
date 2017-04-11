/**
 * the Home screen of the application
 * <p/>
 * <u>update on 11/10/2016 :</u><br>
 * The Home Feature was took from old implementation, and this caused it's implementation
 * not to apply to the over-all rules of the application design, it needs to be
 * revisited again when trying to add new stuff, where a
 * {@link com.base.presentation.base.presentation.Presenter},
 * {@link com.base.presentation.base.presentation.ViewModel},
 * and {@link com.base.presentation.models.Model} classes should be used, and the
 * {@link com.appzoneltd.lastmile.customer.abstracts.LastMileViewBinder} of this feature should be
 * cleaned up ... the "pick my location" feature in the Home screen should be moved to a
 * {@link com.base.presentation.base.presentation.Presenter}
 * and not be implemented in the {@link com.appzoneltd.lastmile.customer.features.main.home.HomeFragmentViewBinder},
 * same to all the immature components in this package
 */
package com.appzoneltd.lastmile.customer.features.main;