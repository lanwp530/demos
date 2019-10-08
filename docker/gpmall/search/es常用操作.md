# 索引操作
``` 
# 创建索引，指定IK分词器为默认分词器
PUT /tb_item1
{
  "settings": {
    "analysis": {
      "analyzer": {"default":{"type":"ik_max_word"}}
    }
  }

}

# 创建index 中的 type , es6.x以后 一个索引只能创建一个type
PUT /tb_item1/_mapping/test
{
  "properties": {
    "cid": {
      "type": "long"
    },
    "created": {
      "type": "date"
    },
    "image": {
      "type": "text"
    },
    "limit_num": {
      "type": "long"
    },
    "num": {
      "type": "long"
    },
    "price": {
      "type": "float"
    },
    "sell_point": {
      "type": "text"
    },
    "status": {
      "type": "long"
    },
    "title": {
      "type": "text"
    },
    "updated": {
      "type": "date"
    }
  }
}
```