#!/usr/bin/env bash
# -------------------------------------------------------------
# ark容器平台 自动创建 Docker TLS 证书
# -------------------------------------------------------------
:<<!
author: lanwp
date: 2019/4/17
des:    docker tls数字证书创建。
            subjectAltName 不设置服务器端subjectAltName不设置(不校验 serverIP，所有服务器均可用)
            subjectAltName 设置多个 subjectAltName = DNS:docker166,IP:192.168.72.166,IP:127.0.0.1
        自动创建 Docker TLS 证书

        服务器证书
            ca.pem
            server-cert.pem
            server-key.pem
        客户端使用
            cert.pem
            ca.pem
            key.pem

     \cp -f ./ssl/* /root/.docker/ && scp ./ssl/* root@192.168.72.166:/root/.docker
     systemctl restart docker
!

PASSWORD="ffcs123456" #私钥密码
DAYS=36500

COUNTRY="CN"
STATE="省" # 省 可选
CITY="市" # 市 可选
ORGANIZATION="公司名称" # 组织 可选
ORGANIZATIONAL_UNIT="ffcs" # 组织-单位可选
COMMON_NAME="ffcs.ark"  # 域名或者IP,必须填写
EMAIL="test@ffcs.cn" # 可选 test@163.com

function createCa() {
    #---
    # 创建ca-key.pem 和 ca.pem
    #openssl genrsa -out ca-key.pem 4096
    openssl genrsa -aes256 -passout "pass:${PASSWORD}" -out ca-key.pem 4096  # -passout "pass:$PASSWORD" 不用输入私钥privateKey
    openssl req -new -x509 -days ${DAYS} -key "ca-key.pem" -sha256 -out "ca.pem" -passin "pass:${PASSWORD}" -subj "/C=${COUNTRY}/ST=${STATE}/L=${CITY}/O=${ORGANIZATION}/OU=${ORGANIZATIONAL_UNIT}/CN=${COMMON_NAME}/emailAddress=${EMAIL}"

    #---
    # Generate Server key
    openssl genrsa -out "server-key.pem" 4096
    # Generate Server Certs.
    openssl req -subj "/CN=$COMMON_NAME" -sha256 -new -key "server-key.pem" -out server.csr

    # Generate server-cert.pem
#    echo "extendedKeyUsage = serverAuth" >> extfile.cnf # echo subjectAltName = DNS:docker166,IP:192.168.72.166,IP:127.0.0.1 >> extfile.cnf
#    openssl x509 -req -days ${DAYS} -sha256 -in server.csr -CA ca.pem -CAkey ca-key.pem -CAcreateserial -out server-cert.pem  -passin "pass:${PASSWORD}" -extfile extfile.cnf
    openssl x509 -req -days ${DAYS} -sha256 -in server.csr -CA ca.pem -CAkey ca-key.pem -CAcreateserial -out server-cert.pem -passin "pass:${PASSWORD}"

    rm -f extfile.cnf

    #---
    # Generate Cient
    openssl genrsa -out "key.pem" 4096

    openssl req -subj '/CN=client' -new -key "key.pem" -out client.csr
    echo extendedKeyUsage = clientAuth >> extfile.cnf
    openssl x509 -req -days ${DAYS} -sha256 -in client.csr -passin "pass:${PASSWORD}" -CA "ca.pem" -CAkey "ca-key.pem" -CAcreateserial -out "cert.pem" -extfile extfile.cnf

    rm -vf client.csr server.csr extfile.cnf

    chmod -v 0400 ca-key.pem key.pem server-key.pem
    chmod -v 0444 ca.pem server-cert.pem cert.pem
}

# 保存目录
function setSaveCaCurrentDir() {
    # 运行脚本的路径   当前运行脚本文件名basename和目录dirname
    local BASE_PATH=$PWD
    if [ -d "$BASE_PATH/ssl" ];then
        echo "文件夹存在"
    else
        echo "文件夹不存在"
        mkdir $PWD/ssl
    fi

    cd $PWD/ssl
}
setSaveCaCurrentDir
createCa