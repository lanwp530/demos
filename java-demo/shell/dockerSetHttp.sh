#!/bin/bash
# docker-ce version
dockerCeExecStartStr='ExecStart=/usr/bin/dockerd'
# old docker version
dockerExecStartStr='ExecStart=/usr/bin/dockerd-current'
#docker exec config file path
dockerServiceFilePath=/usr/lib/systemd/system/docker.service

function hello(){
	echo "hello"
}

hello(){
    echo "function hello1"
}

HELLO() {
    echo "function HELLO"
}

hello

HELLO

echo "$JAVA_HOME"

:<<EOF

EOF
:<<!

!

for var in 1 2 3 4 5
do
    echo "value is ${var}"
done

