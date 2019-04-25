package com.lwp.docker.client.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * created by lawnp on 2019/4/22 14:48
 *
 * This example demonstrates how to create secure connections with a custom SSL context.
 * 1.使用docker相关证书生成docker.keystore
 * 2. 使用docker.keystore创建SSLConnectionSocketFactory
 *      com.lwp.docker.client.httpclient.ClientCustomCaSSL 推荐
 *      直接使用docker的证书
 */
public class ClientCustomSSL {
    public final static void main(String[] args) throws Exception {
        String curClassPath = ClientCustomSSL.class.getClassLoader().getResource("").getPath();
        String certificatesDirPath = new File(curClassPath).getPath();

        // docker使用证书将对应的pem证书转为jks证书
//        InputStream in = new FileInputStream(new File(certificatesDirPath + "/docker.keystore"));
        InputStream in = new FileInputStream(new File(certificatesDirPath + "/dockertls/certificate/docker.keystore"));
        KeyStore keyStore = KeyStore.getInstance("jks");

        String password = "ffcs123456"; // 证书密码
        try {
            keyStore.load(in, password.toCharArray());
        } finally {
            in.close();
        }


        // Trust own CA and all self-signed certs
        // Exception in thread "main" javax.net.ssl.SSLHandshakeException: Received fatal alert: bad_certificate
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, (password).toCharArray())
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                new NoopHostnameVerifier()); // 不验证server服务器
//                SSLConnectionSocketFactory.getDefaultHostnameVerifier()); // 验证server服务器
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {

            String uri = "https://192.168.14.227:2376/version";
            HttpGet httpget = new HttpGet(uri);

            System.out.println("Executing request " + httpget.getRequestLine());

            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(entity));
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    /**
     * 官方例子
     * @throws Exception
     */
    public static void officialExample() throws Exception {
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(new File("my.keystore"), "nopassword".toCharArray(),
                        new TrustSelfSignedStrategy())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {

            HttpGet httpget = new HttpGet("https://httpbin.org/");

            System.out.println("Executing request " + httpget.getRequestLine());

            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

}
