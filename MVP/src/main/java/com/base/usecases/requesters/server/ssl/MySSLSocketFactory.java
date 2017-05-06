package com.base.usecases.requesters.server.ssl;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * a fake implementation for {@link SSLSocketFactory} to make SSL connection with the server
 * <p>
 * Created by Ahmed Adel on 10/18/2016.
 */
@SuppressWarnings("deprecation")
public class MySSLSocketFactory extends SSLSocketFactory {


    private SSLContext sslContext;

    public MySSLSocketFactory(KeyStore trustStore) throws NoSuchAlgorithmException, KeyManagementException,
            UnrecoverableKeyException, KeyStoreException {
        super(trustStore);
        this.sslContext = SSLContext.getInstance("TLS");
        TrustManager tm = new X509TrustManager() {

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }


        };

        sslContext.init(null, new TrustManager[]{tm}, null);

    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,
                               boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port,
                autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }


}
