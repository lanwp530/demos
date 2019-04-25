package com.lwp.docker.client.httpclient;

import com.spotify.docker.client.DockerCertificates;
import com.spotify.docker.client.DockerCertificatesStore;
import com.spotify.docker.client.exceptions.DockerCertificateException;
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
import java.nio.file.Paths;

/**
 * created by lawnp on 2019/4/22 14:48
 *
 * 自定义sslContext 和 HostnameVerifier 请求
 */
public class ClientCustomCaSSL {
    public final static void main(String[] args) throws Exception {
        String curClassPath = ClientCustomCaSSL.class.getClassLoader().getResource("").getPath();
        String certificatesDirPath = new File(curClassPath + "/dockertls/certificate").getPath();

        DockerCertificatesStore dockerCertificatesStore = null;
        try {
            dockerCertificatesStore = new DockerCertificates(Paths.get(certificatesDirPath));
        } catch (DockerCertificateException e) {
            e.printStackTrace();
        }
        CloseableHttpClient httpclient = HttpClients.custom()
//                .setSSLSocketFactory(sSLConnectionSocketFactory)
                .setSSLContext(dockerCertificatesStore.sslContext()).setSSLHostnameVerifier(new NoopHostnameVerifier())
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
