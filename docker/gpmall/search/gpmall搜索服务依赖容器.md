#gpmall搜索模块依赖容器

搜索服务需要的容器 
elasticsearch
kibana
logstash(logstash-input-jdbc)

# 开发环境

## 基础组件

gpmall 需要运行起来的最小依赖组件
```
docker run --name some-zookeeper -p 2181:2181 --restart always -d zookeeper:3.5.5
docker run --name some-redis -p 6379:6379 --restart always -d redis:5.0.5 
```
mysql8部署 密码默认为root 明文
```
docker run --name mysql8 -v /opt/mysql/conf:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 --restart always -d mysql:8.0.17 --default-authentication-plugin=mysql_native_password
```
/opt/mysql/conf/ 下 my.cnf 文件内容，兼容mysql5.7
```
[mysqld]
sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
default-time-zone='+08:00'
```


## 搜索模块
部署的容器都在一台服务器上

### docker run

```
# 部署 elasticsearch kibana logstash
docker run --name elasticsearch  -d -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" --restart always elasticsearch:6.8.2
docker run -d --name kibana -p 5601:5601 --link elasticsearch:elasticsearch --restart always kibana:6.8.2
docker run --name logstash6-mysql -p 9600:9600 -p 5044:5044 -v /root/logstash-input-jdbc.conf:/usr/share/logstash/pipeline/logstash.conf --restart always -d dimmaryanto93/logstash-input-jdbc-mysql:6.6.0

```

logstash 需要的配置文件 /root/logstash-input-jdbc.conf，同步mysql数据
```
input {
        jdbc {
                jdbc_driver_library => "/usr/share/logstash/mysql-connector-java.jar"
                jdbc_driver_class => "com.mysql.jdbc.Driver"
                jdbc_connection_string => "jdbc:mysql://172.26.43.37:3306/gpmall?useUnicode=true&characterEncoding=utf8&useSSL=false"
                jdbc_user => "root"
                jdbc_password => "root"
                schedule => "* * * * *"
                jdbc_paging_enabled => true
                statement => "select * from tb_item"
        }
        jdbc {
                jdbc_driver_library => "/usr/share/logstash/mysql-connector-java.jar"
                jdbc_driver_class => "com.mysql.jdbc.Driver"
                jdbc_connection_string => "jdbc:mysql://172.26.43.37:3306/gpmall?useUnicode=true&characterEncoding=utf8&useSSL=false"
                jdbc_user => "root"
                jdbc_password => "root"
                schedule => "* * * * *"
                jdbc_paging_enabled => true
                statement => "select * from tb_item where updated > :sql_last_value"
                use_column_value => true
                tracking_column => "updated"
                tracking_column_type => "timestamp"
                #这是存放上一次执行之后id的值
                last_run_metadata_path => "lastUpdated.txt"
        }
}

output {
        elasticsearch {
            hosts => ["172.26.43.37:9200"]
            index => "index_item"
            document_id => "%{id}"
        }
        stdout { codec => json_lines }
}
```
#### elasticsearch 安装中文IK分词器


```
# root_elasticsearch_1 为启动的容器名
docker exec -it root_elasticsearch_1 bash bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.8.2/elasticsearch-analysis-ik-6.8.2.zip 
```