package com.java110.acct.payment.adapt.easypay.utils;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.cert.X509Certificate;

/**
 *  Http ssl 处理类
 *
 * @date: 2021/10/14 22:03
 * @author: pandans
 */
public class BaseHttpSslSocketFactory extends SSLSocketFactory {
    public BaseHttpSslSocketFactory() {
    }

    private SSLContext getSSLContext() {
        return this.createEasySSLContext();
    }

    @Override
    public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2, int arg3) throws IOException {
        return this.getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
    }

    @Override
    public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3) throws IOException {
        return this.getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
    }

    @Override
    public Socket createSocket(InetAddress arg0, int arg1) throws IOException {
        return this.getSSLContext().getSocketFactory().createSocket(arg0, arg1);
    }

    @Override
    public Socket createSocket(String arg0, int arg1) throws IOException {
        return this.getSSLContext().getSocketFactory().createSocket(arg0, arg1);
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return null;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return null;
    }

    @Override
    public Socket createSocket(Socket arg0, String arg1, int arg2, boolean arg3) throws IOException {
        return this.getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
    }

    private SSLContext createEasySSLContext() {
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[]{MyX509TrustManager.manger}, null);
            return context;
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public TrustAnyHostnameVerifier() {
        }

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static class MyX509TrustManager implements X509TrustManager {
        static MyX509TrustManager manger = new MyX509TrustManager();

        public MyX509TrustManager() {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }
    }
}
