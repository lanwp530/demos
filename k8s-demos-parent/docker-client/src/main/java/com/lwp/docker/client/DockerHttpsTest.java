package com.lwp.docker.client;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificates;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Version;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;

/**
 * created by lawnp on 2019/4/19 16:11
 * dockerClient带证书请求
 *
 * dockerClient使用证书：dockerClient也是基于httpClient请求docker Api的
 */
public class DockerHttpsTest {
    public static void main(String[] args) throws DockerCertificateException, DockerException, InterruptedException {

        // 获得证书存放路径
//        String curClassPath = DockerHttpsTest.class.getClassLoader().getResource("").getPath();
        String localCertificatesDirPath = DockerHttpsTest.class.getClassLoader().getResource("dockertls/certificate").getPath();
        // /E:/git/lwp-demos/k8s-demos-parent/docker-client/target/classes/ 转化路径为 E:/git/lwp-demos/k8s-demos-parent/docker-client/target/classes/
        String certificatesDirPath = new File(localCertificatesDirPath).getPath();
        final DockerClient docker = DefaultDockerClient.builder()
                .uri(URI.create("https://192.168.72.110:2376"))
                // 本机保存的证书位置
                .dockerCertificates(new DockerCertificates(Paths.get(certificatesDirPath)))
//                .dockerCertificates(new DockerCertificates(Paths.get("/Users/rohan/.docker/boot2docker-vm/")))
                .build();

        System.out.println(docker.getHost());
        Version version = docker.version();
        System.out.println(String.format("docker 版本: %s", version.version()));
        System.out.println(String.format("go 版本: %s", version.goVersion()));
        System.out.println(String.format("docker API版本: %s", version.apiVersion()));
        System.out.println(String.format("内核版本: %s", version.kernelVersion()));

        docker.close();
    }

    /**
     * 第二种出啊精简客户端方法
     * @throws DockerCertificateException
     * @throws DockerException
     * @throws InterruptedException
     */
    public static void second() throws DockerCertificateException, DockerException, InterruptedException {
        // 获得证书存放路径
        String curClassPath = DockerHttpsTest.class.getClassLoader().getResource("").getPath();
        String certificatesDirPath = new File(curClassPath).getPath(); // /E:/git/lwp-demos/k8s-demos-parent/docker-client/target/classes/ 转化路径为 E:/git/lwp-demos/k8s-demos-parent/docker-client/target/classes/

        final DockerClient docker = new DefaultDockerClient(URI.create("https://192.168.72.110:2376"), new DockerCertificates(Paths.get(certificatesDirPath)));
        System.out.println(docker.getHost());
        Version version = docker.version();
        System.out.println(String.format("docker 版本: %s", version.version()));
        System.out.println(String.format("go 版本: %s", version.goVersion()));
        System.out.println(String.format("docker API版本: %s", version.apiVersion()));
        System.out.println(String.format("内核版本: %s", version.kernelVersion()));

        docker.close();
    }
}
