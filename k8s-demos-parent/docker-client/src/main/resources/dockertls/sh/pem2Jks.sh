#!/usr/bin/env bash

# ------------------------------------------------------------------
# ------------------------------------------------------------------
# lanwp create by 2019/04/22
#   将openssl生成的pem证书转为jks证书给java程序调用
#
# ------------------------------------------------------------------

CA_FILE_NAME="ca.pem"
CA_KEY_FILE_NAME="ca-key.pem"
CA_KEY_FILE_PASS="ffcs123456"
KEY_STORE_PASS="ffcs123456"

echo "执行脚本当前路径: $PWD"

# pem convert pkcs12
# 这个需要输入ca-key.pem密码 和输出密码
#openssl pkcs12 -export -in ${CA_FILE_NAME} -inkey ${CA_KEY_FILE_NAME} -out shkey.pk12 -name shkey
openssl pkcs12 -export -in ca.pem -inkey ca-key.pem -out sh.pk12 -name shkey -passin "pass:${CA_KEY_FILE_PASS}" -passout "pass:${KEY_STORE_PASS}"
# pkcs12 convert keystore ,生成的文件名docker.keystore可以根据自己需要的命名
keytool -importkeystore -deststorepass ${KEY_STORE_PASS} -destkeypass ${KEY_STORE_PASS} -destkeystore docker.keystore -srckeystore sh.pk12 -srcstoretype PKCS12 -srcstorepass ${KEY_STORE_PASS} -alias shkey

rm -f sh.pk12

# 直接可以生成keystore
# openssl pkcs12 -export -in ca.pem -inkey ca-key.pem -out sh.pk12 -name shkey -passin "pass:123456" -passout "pass:123456" ; keytool -importkeystore -deststorepass 123456 -destkeypass 123456 -destkeystore docker.keystore -srckeystore sh.pk12 -srcstoretype PKCS12 -srcstorepass 123456 -alias shkey