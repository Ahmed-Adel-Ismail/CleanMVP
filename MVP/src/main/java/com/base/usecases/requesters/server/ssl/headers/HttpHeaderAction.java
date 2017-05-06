package com.base.usecases.requesters.server.ssl.headers;

import com.base.abstraction.commands.Command;
import com.base.usecases.requesters.server.base.HttpHeaders;

/**
 * {@link Command} that is done on the {@link HttpHeaders} to change there values or data
 * <p>
 * Created by Ahmed Adel on 11/8/2016.
 */
public interface HttpHeaderAction extends Command<HttpHeaders, HttpHeaders> {

    @Override
    HttpHeaders execute(HttpHeaders headers);
}
