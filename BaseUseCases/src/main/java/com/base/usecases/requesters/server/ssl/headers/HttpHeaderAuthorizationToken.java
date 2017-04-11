package com.base.usecases.requesters.server.ssl.headers;

import android.text.TextUtils;

import com.base.abstraction.logs.Logger;
import com.base.abstraction.serializers.JsonLoader;
import com.base.cached.Token;
import com.base.usecases.R;
import com.base.usecases.requesters.server.base.HttpHeaders;

/**
 * a class that adds Authorized token to the headers of the request
 * <p>
 * Created by Ahmed Adel on 11/8/2016.
 */
public class HttpHeaderAuthorizationToken implements HttpHeaderAction {

    private static final String KEY_AUTHORIZED = "Authorization";

    @Override
    public HttpHeaders execute(HttpHeaders headers) {
        Token token = new JsonLoader<Token>(R.string.PREFS_KEY_TOKEN).execute(Token.class);
        String key = token.getToken_type();
        String value = token.getAccess_token();
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            headers.put(KEY_AUTHORIZED, key + " " + value);
        } else {
            headers.remove(KEY_AUTHORIZED);
            Logger.getInstance().exception(new IllegalArgumentException("invalid token saved"));
        }
        return headers;
    }
}
