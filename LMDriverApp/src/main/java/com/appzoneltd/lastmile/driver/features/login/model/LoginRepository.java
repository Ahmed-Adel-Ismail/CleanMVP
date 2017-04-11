package com.appzoneltd.lastmile.driver.features.login.model;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.cached.Token;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.repos.base.Repository;
import com.base.presentation.annotations.interfaces.Requester;
import com.base.usecases.annotations.Mock;
import com.base.usecases.requesters.server.ssl.HttpsRequester;

/**
 * the repository for login screen
 * <p>
 * Created by Ahmed Adel on 11/21/2016.
 */
@Address(R.id.addressLoginRepository)
@Requester(HttpsRequester.class)
class LoginRepository extends Repository {

    @Mock(R.id.requestLogin)
    @JsonRequest(R.id.requestLogin)
    Token token;

}
