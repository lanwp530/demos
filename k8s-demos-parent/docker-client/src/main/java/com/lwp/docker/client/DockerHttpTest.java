package com.lwp.docker.client;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Version;

import java.net.URI;

/**
 * created by lawnp on 2019/4/19 16:11
 *
 * 没有使用证书请求httpclient
 * 官方默认 docker http使用2375
 */
public class DockerHttpTest {
    public static void main(String[] args) throws DockerCertificateException, DockerException, InterruptedException {
        final DockerClient docker = DefaultDockerClient.builder()
                .uri(URI.create("http://192.168.72.112:2375"))
//                .dockerCertificates(new DockerCertificates(Paths.get("/Users/rohan/.docker/boot2docker-vm/")))
//                .dockerCertificates(new DockerCertificates(Paths.get("/git/lwp-demos/kversion8s-demos-parent/docker-client/target/classes/")))
//                .dockerCertificates(new DockerCertificates(Paths.get(DockerHttpsTest.class.getResource("/").getPath())))
                .build();

        System.out.println(docker.getHost());
        Version version = docker.version();
        System.out.println(docker.listContainers().size()); // 容器
        System.out.println(docker.listImages().size()); // 镜像
        System.out.println(String.format("docker 版本: %s", version.version()));
        System.out.println(String.format("go 版本: %s", version.goVersion()));
        System.out.println(String.format("docker API版本: %s", version.apiVersion()));
        System.out.println(String.format("内核版本: %s", version.kernelVersion()));

        docker.close();
    }
}
