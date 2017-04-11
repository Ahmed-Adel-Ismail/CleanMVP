package com.appzoneltd.lastmile.customer.system;

import com.appzoneltd.lastmile.customer.failures.SessionExpiredHandler;
import com.appzoneltd.lastmile.customer.requesters.SecureUrlLocator;
import com.appzoneltd.lastmile.customer.requesters.WebSocketsUrlLocator;
import com.base.abstraction.annotations.interfaces.Actor;
import com.base.abstraction.annotations.interfaces.ApplicationLoader;
import com.base.abstraction.annotations.interfaces.Behavior;
import com.base.abstraction.annotations.interfaces.Integration;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppLoader;
import com.base.abstraction.system.Behaviors;
import com.base.presentation.failures.OAuth2Handler;
import com.base.usecases.annotations.HttpRequestUrlLocator;
import com.base.usecases.annotations.RefreshTokenEntity;
import com.base.usecases.annotations.WebSocketUrlLocator;
import com.base.usecases.api.IntegrationHandler;
import com.entities.auth.RefreshToken;

@Behavior(Behaviors.TESTING)
@ApplicationLoader(AppLoader.class)
@Integration(IntegrationHandler.class)
@RefreshTokenEntity(RefreshToken.class)
@HttpRequestUrlLocator(SecureUrlLocator.class)
@WebSocketUrlLocator(WebSocketsUrlLocator.class)
public class LastMileApp extends App {

    @Actor
    OAuth2Handler oAuth2Handler;

    @Actor
    SessionExpiredHandler sessionExpiredHandler;

}
