package com.base.usecases.requesters.server.ssl;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * a class that generates a {@link SSLContext} that accepts any certificates
 * <p>
 * Created by Ahmed Adel on 1/24/2017.
 */
public class SSLContextFactory implements Command<Void, SSLContext> {

    @Override
    public SSLContext execute(Void p) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{createTrustManager()}, null);
            return sslContext;
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    private X509TrustManager createTrustManager() {
        return new X509TrustManager() {

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                    throws java.security.cert.CertificateException {

            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                    throws java.security.cert.CertificateException {

            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }


        };
    }
}
