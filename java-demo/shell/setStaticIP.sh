#!/usr/bin/env bash
# lanwp
# 设置静态IP
# 指定的IP
IPADDR="192.168.72.166"
# 网关
GATEWAY="192.168.72.2"

# 网卡默认路径
NETWORK_PATH=/etc/sysconfig/network-scripts/
NETWORK_NAME=ifcfg-ens33
NETWORK_FILE=${NETWORK_PATH}${NETWORK_NAME}

# 设置nameserver
function setNameserver(){
    if [ `grep -c "nameserver 8.8.8.8"  /etc/resolv.conf` -lt 1 ]
    then
       echo nameserver 8.8.8.8 >> /etc/resolv.conf
    fi
}

function setIPADDR(){
    if [ `grep -c "IPADDR=" ${NETWORK_FILE}` -gt 0 ]
    then
        echo "IPADDR 已经存在"
    else
        echo "IPADDR 不存在"
        # IP写入文件
        echo "IPADDR=${IPADDR}" >> ${NETWORK_FILE}
    fi
}

function setGateway(){
    if [ `grep -c "GATEWAY=" ${NETWORK_FILE}` -gt 0 ]
    then
        echo "GATEWAY 已经存在"
    else
        echo "GATEWAY 不存在"
        # IP写入文件
        echo "GATEWAY=${GATEWAY}" >> ${NETWORK_FILE}
    fi
}

function setNetworkInfo(){
cat > ${NETWORK_FILE} <<EOF
    TYPE="Ethernet"
    IPADDR="${IPADDR}"
    GATEWAY="${GATEWAY}"
    ONBOOT="yes"
EOF
}

:<<!
    TYPE="Ethernet"
    #BOOTPROTO="dhcp"
    #BOOTPROTO="static"
    IPADDR="${IPADDR}"
    GATEWAY="${GATEWAY}"
    #    NAME="ens33"
    #    DEVICE="ens33"
    # UUID=
    ONBOOT="yes"
!

echo start...

# 1.设置nameserver,设置静态IP
setNameserver
setNetworkInfo


# 2. 重启网卡
systemctl restart network