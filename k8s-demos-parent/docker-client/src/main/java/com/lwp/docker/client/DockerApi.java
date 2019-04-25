/*
package cn.ffcs.fcsp.internal.module.k8s;

import cn.ffcs.fcsp.common.CommonException;
import cn.ffcs.fcsp.common.CommonList;
import cn.ffcs.fcsp.util.Util;
import com.spotify.docker.client.*;
import com.spotify.docker.client.exceptions.ContainerNotFoundException;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.messages.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.List;

*/
/**
 * Created by linguang on 2017/8/10.
 *//*

public class DockerApi {
    private final static Logger logger = LoggerFactory.getLogger(DockerApi.class);

    private String ip;
    private String port;
    private int statusCode = 0;
    private String result = "";
    private String defaultCharset = "UTF-8";

    public static boolean isUseHttps = true;
    private static SSLConnectionSocketFactory sSLConnectionSocketFactory;
    private static String dockerKeyStorePass = "ffcs123456";

    static {
        try {
            sSLConnectionSocketFactory = getSSLConnectionSocketFactory();
        } catch (Exception e) {
            CommonException.throwEx("DockerApi getSSLConnectionSocketFactory 异常", e);
        }
    }

    public DockerApi(String ip) {
        this.ip = ip;
        port = K8sConfig.getInstance().getDockerPort();
    }

    public void get(String path) {
        request(Method.GET, path, null);
    }

    public void delete(String path) {
        request(Method.POST, path, null);
    }

    public void post(String path, String body) {
        request(Method.POST, path, body);
    }

    public void put(String path, String body) {
        request(Method.PUT, path, body);
    }

    public void patch(String path, String body) {
        request(Method.PATCH, path, body);
    }

    public void setDefaultCharset(String defaultCharset) {
        this.defaultCharset = defaultCharset;
    }

    enum Method {
        GET, POST, PATCH, DELETE, PUT;
    }

    public String getResult() {
        return result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    private static SSLConnectionSocketFactory getSSLConnectionSocketFactory() throws Exception {
        String curClassPath = DockerApi.class.getClassLoader().getResource("").getPath();
        String keyStorePath = new File(curClassPath + "/dockertls/certificate/docker.keystore").getPath();

        // docker使用证书将对应的pem证书转为jks证书
        InputStream in = new FileInputStream(new File(keyStorePath));
        KeyStore keyStore = KeyStore.getInstance("jks");

        String password = dockerKeyStorePass; // 证书密码,生成证书时指定
        try {
            keyStore.load(in, password.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            in.close();
        }
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, (password).toCharArray())
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext, new String[] { "TLSv1" }, null,
                new NoopHostnameVerifier()); // 不验证server服务器
        return sslsf;
    }

    public CloseableHttpClient getHttpclientSsl(){

        DockerCertificatesStore dockerCertificatesStore = null;
        try {
            dockerCertificatesStore = new DockerCertificates(Paths.get(getDockerCaDir()));
        } catch (DockerCertificateException e) {
            e.printStackTrace();
        }
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLContext(dockerCertificatesStore.sslContext()).setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();

*/
/*        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sSLConnectionSocketFactory)
                .build();*//*

        return httpclient;
    }

    public void request(Method method, String path, String body) {
        logger.debug("docker api requst: method:{}, path:{}, body:{}", method, path, body);
        CloseableHttpClient httpClient = null;
        String url = getDockerUri(ip, port) + path;
        if (isUseHttps) {
            httpClient = getHttpclientSsl();
        } else {
            httpClient = HttpClients.createDefault();
        }
        HttpRequestBase requestBase = null;
        if (method == Method.GET) {
            requestBase = new HttpGet();
        } else if (method == Method.DELETE) {
            requestBase = new HttpDelete();
        } else if (method == Method.POST || method == Method.PUT || method == Method.PATCH) {
            if (method == Method.POST) {
                requestBase = new HttpPost();
            } else if (method == Method.PUT) {
                requestBase = new HttpPut();
            } else {
                requestBase = new HttpPatch();
            }

            requestBase.setHeader("Content-Type", "application/json");
            HttpEntityEnclosingRequestBase entityEnclosingRequestBase = (HttpEntityEnclosingRequestBase) requestBase;
            try {
                entityEnclosingRequestBase.setEntity(new StringEntity(body));
            } catch (UnsupportedEncodingException e) {
                CommonException.throwEx("docker api cannot call", e);
            }
        }
        // get请求异常URISyntaxException: Illegal character in query at index 52: https://192.168.14.229:2376/containers/json?filters={"label":["io.kubernetes.container.name=calico-node"]}
        requestBase.setURI(URI.create(url));
        try {
            CloseableHttpResponse response = httpClient.execute(requestBase);
            try {
                statusCode = response.getStatusLine().getStatusCode();
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, defaultCharset);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                response.close();
            }
        } catch (Exception e) {
            CommonException.throwEx("docker api cannot call", e);
        } finally {
            logger.info("docker api response status: {}: result: {}", statusCode, result);
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error("", e);
            }
        }

        if (!(statusCode >= 200 && statusCode < 400)) {
            CommonException.throwEx("docker api cannot fail:" + result);
        }
    }

    public String requestGet(String path, List<NameValuePair> params) {
        return this.requestGet(ip, port, path, params);
    }

    private String requestGet(String ip, String port, String path, List<NameValuePair> params) {
        logger.debug("docker api requst: method:{}, path:{}, body:{}", path);
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;

        String url = getDockerUri(ip, port) + path;
        if (isUseHttps) {
            httpClient = getHttpclientSsl();
        } else {
            httpClient = HttpClients.createDefault();
        }
        // get请求异常URISyntaxException: Illegal character in query at index 52: https://192.168.14.229:2376/containers/json?filters={"label":["io.kubernetes.container.name=calico-node"]}
        try {
            if (params != null && !params.isEmpty()) {
                url += "?";
                for (int i = 0, paramsSize = params.size(); i < paramsSize; i++) {
                    NameValuePair nvp = params.get(i);
                    if (i > 0) {
                        url += "&";
                    }
                    url += nvp.getName() + "=" + URLEncoder.encode(nvp.getValue(), defaultCharset);
                }
            }
            HttpGet httpGet = new HttpGet(url);
            response = httpClient.execute(httpGet);
            statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, defaultCharset);
            }
        } catch (Exception e) {
            CommonException.throwEx("docker api cannot call", e);
        } finally {
            logger.info("docker api response status: {}: result: {}", statusCode, result);
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }

        if (!(statusCode >= 200 && statusCode < 400)) {
            CommonException.throwEx("docker api cannot fail:" + result);
        }
        return result;
    }

    private DockerClient getDockerClient() {
        return getDockerClient(ip, port);
    }

    public static DockerClient getDockerClient(String ip, String port) {
        String uri = getDockerUri(ip, port);
        DockerCertificatesStore dockerCertificatesStore = null;
        try {
            dockerCertificatesStore = new DockerCertificates(Paths.get(getDockerCaDir()));
        } catch (DockerCertificateException e) {
            e.printStackTrace();
        }
        return DefaultDockerClient.builder().uri(URI.create(uri)).dockerCertificates(dockerCertificatesStore).build();
    }

    */
/**
     * 当前系统数字证书存放的目录
     * @return
     *//*

    public static String getDockerCaDir() {
        String curClassPath = DockerApi.class.getClassLoader().getResource("").getPath();
        return new File(curClassPath + "/dockertls/certificate").getPath();
    }

    public void exec(String id, String[] cmd) {
        logger.info("docker exec :" + id + ", " + Util.toJSONString(cmd));
        DockerClient docker = getDockerClient();
        LogStream output = null;
        int exitCode = -1;
        try {
            ExecCreation execCreation = docker.execCreate(
                    id, cmd, DockerClient.ExecCreateParam.attachStdout(),
                    DockerClient.ExecCreateParam.attachStderr());
            String execId = execCreation.id();
            output = docker.execStart(execId);
            result = output.readFully();
            ExecState state = docker.execInspect(execId);
            exitCode = state.exitCode();
            if (exitCode != 0) {
                CommonException.throwEx("exitCode(" + exitCode + ")");
            }
        } catch (Exception e) {
            logger.error("", e);
            CommonException.throwEx("docker exec fail", e);
        } finally {
            logger.info("docker exec: id:" + id + ", cmd:" + Util.toJSONString(cmd) + "exitCode:" + exitCode + ", result:" + result);
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
            if (docker != null) {
                try {
                    docker.close();
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }
    }


    public void stopContainer(String id) {
        DockerClient docker = getDockerClient();
        try {
            docker.stopContainer(id, 10);
        } catch (Exception e) {
            logger.error("", e);
            if (e instanceof ContainerNotFoundException) {

            } else {
                CommonException.throwEx("docker stop fail", e);
            }
        }
    }

    public void removeContainer(String id) {
        DockerClient docker = getDockerClient();
        try {
            docker.removeContainer(id, new DockerClient.RemoveContainerParam("force", "true"));
        } catch (Exception e) {
            logger.error("", e);
            if (e instanceof ContainerNotFoundException) {
            } else {
                CommonException.throwEx("docker remove fail", e);
            }
        }
    }

    public void pull(String image) {
        logger.info("docker pull " + image);
        DockerClient docker = getDockerClient();
        try {
            docker.pull(image);
        } catch (Exception e) {
            CommonException.throwEx("docker pull fail", e);
        }
    }

    public void run(String name, ContainerConfig containerConfig) {
        logger.info("docker run " + containerConfig.image());
        try {
            runContainer(name, containerConfig);
        } catch (Exception e) {
            if (e instanceof CommonException) {
                CommonException e1 = (CommonException) e;
                if (e1.getCause() instanceof com.spotify.docker.client.exceptions.ImageNotFoundException) {
                    //由于没有镜像失败
                    pull(containerConfig.image());
                    runContainer(name, containerConfig);
                    return;
                }
            }
            CommonException.throwEx(e);
        }
    }

    public void runContainer(String name, ContainerConfig containerConfig) {
        DockerClient docker = getDockerClient();
        try {
            ContainerCreation creation = docker.createContainer(containerConfig, name);
            docker.startContainer(creation.id());
        } catch (Exception e) {
            CommonException.throwEx("docker run fail", e);
        }
    }

    private static String getDockerUri(String dockerIP, String port) {
        String uri = "http://";
        if (isUseHttps) { uri = "https://";}
//        uri += dockerIP + ":" + K8sConfig.getInstance().getDockerPort();
        uri += dockerIP + ":" + port;
        return uri;
    }

    public static void main(String[] args) {
        ContainerConfig build = ContainerConfig.builder()
                .hostConfig(HostConfig.builder()
                        .capAdd("NET_ADMIN")
                        .networkMode("host")
                        .restartPolicy(HostConfig.RestartPolicy.always())
                        .build())
                .env(new CommonList()
                        .add_("K8S_URL=192.168.14.174:8080")
                        .add_("CLUSTERS_HTTP=1123")
                        .add_("CLUSTERS_TCP=")
                        .add_("ENABLE_KEEPALIVED=false")
                        .add_("VIP=")
                        .add_("KEEPALIVED_GROUP=")
                        .add_("PRIORITY=100")
                )
                .image("192.168.14.171:5000/ffcs_containers/fcsp-tengine:1.5-dev")
                .build();

        System.out.println(ToStringBuilder.reflectionToString(build, ToStringStyle.DEFAULT_STYLE, false));

//        new DockerApi("192.168.14.176").createContainer(build, "afew");


    }
}
*/
