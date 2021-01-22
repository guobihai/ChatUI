package com.smart.chatui.https.ssl;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Created by gbh on 17/12/18  20:27.
 *
 * @describe
 */

public class TrustAllCerts implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {}

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {}

    @Override
    public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
}