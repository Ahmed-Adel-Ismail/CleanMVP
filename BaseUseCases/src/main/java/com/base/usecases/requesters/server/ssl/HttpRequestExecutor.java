package com.base.usecases.requesters.server.ssl;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.usecases.events.ResponseMessage;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * a class to handle an http request and generate a {@link ResponseMessage} from it's response
 * <p>
 * Created by Ahmed Adel on 10/18/2016.
 */
@SuppressWarnings("deprecation")
class HttpRequestExecutor implements Command<HttpRequestBase, HttpResponse> {

    private int connectionTimeout;
    private int socketTimeout;

    HttpRequestExecutor(int connectionTimeout, int socketTimeout) {
        this.connectionTimeout = connectionTimeout;
        this.socketTimeout = socketTimeout;
    }

    @Override
    public HttpResponse execute(HttpRequestBase request) {
        HttpResponse response = null;
        try {
            response = getResponseFromServer(request);
        } catch (IOException e) {
            logFailure(e);
        }
        return response;
    }


    private HttpResponse getResponseFromServer(HttpRequestBase request) throws IOException {
        HttpClientGenerator<DefaultHttpClient> clientGenerator;
        clientGenerator = new HttpClientGenerator<>();
        clientGenerator.connectionTimeout(connectionTimeout);
        clientGenerator.socketTimeout(socketTimeout);
        DefaultHttpClient client = clientGenerator.execute(null);
        return client.execute(request);
    }

    private void logFailure(IOException e) {
        Logger.getInstance().error(HttpsRequester.class, "failed to get response body : " +
                e.getMessage());

        Logger.getInstance().exception(e);
    }


}
