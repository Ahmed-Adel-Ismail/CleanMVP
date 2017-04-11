package com.entities.auth;

import com.base.annotations.Authorization;
import com.base.auth.AbstractRefreshToken;
import com.base.auth.AuthGrantTypeRefreshToken;
import com.base.cached.Token;

/**
 * the refresh token Object that requests the new {@link Token}
 * <p>
 * Created by Ahmed Adel on 12/13/2016.
 */
@Authorization(client = AuthClientAndroid.class, grantType = AuthGrantTypeRefreshToken.class)
public class RefreshToken extends AbstractRefreshToken {
}
