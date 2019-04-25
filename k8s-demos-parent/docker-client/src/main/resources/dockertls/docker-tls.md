
# 生成docker数字证书

> 官方生成docker ca证书 https://docs.docker.com/engine/security/https/

## 使用已有证书
    目前是这种方式
  1. 拷贝服务端需要的证书到启动docker的~/.docker/目录

    cp ca.pem server-cert.pem server-key.pem /root/.docker/
  2. 重新启动docker服务:
     systemctl daemon-reload ; systemctl restart docker

## 重新创建新的证书

  1. 执行脚本sh/createDockerCert_ark.sh生成证书
       sh createDockerCert_ark.sh 需要重新生成证书再执行
  2. 将pem2Jks.sh拷贝到生成证书所在目录，执行

    sh pem2Jks.sh
  3. 将生成的文件拷贝到dockertls/certificate目录下
  4. 拷贝服务端需要的证书到启动docker的~/.docker/目录
      cp ca.pem server-cert.pem server-key.pem /root/.docker/
  5. 设置docker-network文件`DOCKER_NETWORK_OPTIONS=" -H unix:// --tlsverify --tlscacert=/root/.docker/ca.pem --tlscert=/root/.docker/server-cert.pem --tlskey=/root/.docker/server-key.pem -H=0.0.0.0:2376 -H=0.0.0.0:2375"`

```shell
[root@docker110 .docker]# cat /etc/sysconfig/docker-network 
DOCKER_NETWORK_OPTIONS=" -H unix:// --tlsverify --tlscacert=/root/.docker/ca.pem --tlscert=/root/.docker/server-cert.pem --tlskey=/root/.docker/server-key.pem -H=0.0.0.0:2376 -H=0.0.0.0:2375"
```

6. 重新启动docker服务:
   systemctl daemon-reload ; systemctl restart docker



# 测试

* ```
  [root@docker110 .docker]# docker ps
  CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
  [root@docker110 .docker]# docker -H 192.168.72.110 ps
  Get http://192.168.72.110:2375/v1.26/containers/json: net/http: HTTP/1.x transport connection broken: malformed HTTP response "\x15\x03\x01\x00\x02\x02".
  * Are you trying to connect to a TLS-enabled daemon without TLS?
  ```

  ## 本机测试

  如下命令 `docker ps`本机执行命令成功

  ## 远程测试

  `docker -H 192.168.72.110 ps` 要求需要证书