#!/usr/bin/env bash

# 单行注释

function hello() {
    echo "hello"
}

hello(){
    echo 333
}

hello

cat << EOF
    文本输入
    测试cat
EOF