package com.appzoneltd.lastmile.driver;

import com.appzoneltd.lastmile.driver.failures.SessionExpiredHandler;
import com.appzoneltd.lastmile.driver.features.loaders.LoaderViewBinder;
import com.appzoneltd.lastmile.driver.features.login.host.LoginViewBinder;
import com.appzoneltd.lastmile.driver.features.login.model.LoginModel;
import com.appzoneltd.lastmile.driver.features.login.signin.SignInViewBinder;
import com.appzoneltd.lastmile.driver.features.main.home.HomeViewBinder;
import com.appzoneltd.lastmile.driver.features.main.host.MainViewBinder;
import com.appzoneltd.lastmile.driver.features.main.model.MainModel;
import com.appzoneltd.lastmile.driver.features.pickup.documents.DocumentsViewBinder;
import com.appzoneltd.lastmile.driver.features.pickup.host.PickupProcessViewBinder;
import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.appzoneltd.lastmile.driver.features.pickup.payments.PaymentViewBinder;
import com.appzoneltd.lastmile.driver.features.pickup.verification.VerificationViewBinder;
import com.appzoneltd.lastmile.driver.requesters.servers.urls.SecureUrlLocator;
import com.appzoneltd.lastmile.driver.requesters.servers.urls.WebSocketsUrlLocator;
import com.appzoneltd.lastmile.driver.subfeatures.pickups.OnDemandPickupActionsHandler;
import com.base.abstraction.annotations.interfaces.Actor;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.abstraction.annotations.interfaces.ApplicationLoader;
import com.base.abstraction.annotations.interfaces.Behavior;
import com.base.abstraction.annotations.interfaces.BroadcastsHandler;
import com.base.abstraction.annotations.interfaces.Integration;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppLoader;
import com.base.abstraction.system.Behaviors;
import com.base.presentation.annotations.interfaces.ActivityViewBinders;
import com.base.presentation.annotations.interfaces.Drawer;
import com.base.presentation.annotations.interfaces.FragmentViewBinder;
import com.base.presentation.annotations.interfaces.Menu;
import com.base.presentation.annotations.interfaces.Splash;
import com.base.presentation.annotations.interfaces.Toolbar;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.abstracts.features.AbstractFragment;
import com.base.presentation.failures.OAuth2Handler;
import com.base.presentation.models.NullModel;
import com.base.usecases.annotations.HttpRequestUrlLocator;
import com.base.usecases.annotations.RefreshTokenEntity;
import com.base.usecases.annotations.WebSocketUrlLocator;
import com.base.usecases.api.IntegrationHandler;
import com.entities.auth.RefreshToken;

/**
 * an interface that represents the flow of the application
 * <p>
 * Created by Ahmed Adel on 11/30/2016.
 */
public interface Flow {

    @Behavior(Behaviors.MOCKING)
    @ApplicationLoader(AppLoader.class)
    @Integration(IntegrationHandler.class)
    @RefreshTokenEntity(RefreshToken.class)
    @HttpRequestUrlLocator(SecureUrlLocator.class)
    @WebSocketUrlLocator(WebSocketsUrlLocator.class)
    class LastMileApp extends App {
        @BroadcastsHandler
        OnDemandPickupActionsHandler onDemandPickupActionsHandler;
        @Actor
        OAuth2Handler oAuth2Handler;
        @Actor
        SessionExpiredHandler sessionExpiredHandler;
    }


    @Splash
    @ActivityViewBinders(initial = LoaderViewBinder.class, error = LoaderViewBinder.class)
    class SplashActivity extends AbstractActivity<NullModel> {
    }

    @ActivityViewBinders(initial = LoginViewBinder.class, error = LoaderViewBinder.class)
    class LoginActivity extends AbstractActivity<LoginModel> {

        @Address(R.id.addressLoginFragment)
        @FragmentViewBinder(SignInViewBinder.class)
        public static class SignInFragment extends AbstractFragment<LoginModel> {
        }

    }

    @Menu(R.menu.main)
    @Toolbar(R.id.screen_main_toolbar)
    @Drawer(R.id.screen_host_container_id)
    @ActivityViewBinders(initial = MainViewBinder.class, error = LoaderViewBinder.class)
    class MainActivity extends AbstractActivity<MainModel> {

        @Address(R.id.addressHomeFragment)
        @FragmentViewBinder(HomeViewBinder.class)
        public static class HomeFragment extends AbstractFragment<MainModel> {
        }
    }

    @Toolbar(R.id.screen_pickup_process_toolbar)
    @ActivityViewBinders(initial = PickupProcessViewBinder.class, error = LoaderViewBinder.class)
    class PickupProcessActivity extends AbstractActivity<PickupProcessModel> {

        @Address(R.id.addressPickupPackageVerificationFragment)
        @FragmentViewBinder(VerificationViewBinder.class)
        public static class Verification extends AbstractFragment<PickupProcessModel> {

        }

        @Address(R.id.addressPickupPaymentFragment)
        @FragmentViewBinder(PaymentViewBinder.class)
        public static class Payment extends AbstractFragment<PickupProcessModel> {

        }

        @Address(R.id.addressPickupDocumentsFragment)
        @FragmentViewBinder(DocumentsViewBinder.class)
        public static class Documents extends AbstractFragment<PickupProcessModel> {

        }
    }

//    @ActivityViewBinders(initial = ScalaTestViewBinder.class, error = LoaderViewBinder.class)
//    class ScalaTestActivity extends AbstractActivity<ScalaTestModel> {
//
//    }

}
