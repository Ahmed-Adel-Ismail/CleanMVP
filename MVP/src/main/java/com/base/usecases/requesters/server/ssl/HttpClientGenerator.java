package com.base.usecases.requesters.server.ssl;

import com.base.abstraction.commands.Command;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.security.KeyStore;

/**
 * a Class that generates a {@link HttpClient} to be used
 * <p>
 * Created by Ahmed Adel on 10/18/2016.
 */
@SuppressWarnings("deprecation")
class HttpClientGenerator<T extends HttpClient> implements Command<Void, T> {

    private static final String SCHEME_HTTP = "http";
    private static final String SCHEME_HTTPS = "https";
    private static final int PORT_HTTP = 80;
    private static final int PORT_HTTPS = 443;


    private int connectionTimeout;
    private int socketTimeout;


    @SuppressWarnings("unchecked")
    @Override
    public T execute(Void p) {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
            HttpConnectionParams.setSoTimeout(params, socketTimeout);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme(SCHEME_HTTP, PlainSocketFactory.getSocketFactory(), PORT_HTTP));
            registry.register(new Scheme(SCHEME_HTTPS, sf, PORT_HTTPS));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return (T) new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return (T) new DefaultHttpClient();
        }
    }

    HttpClientGenerator connectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    HttpClientGenerator socketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }
}