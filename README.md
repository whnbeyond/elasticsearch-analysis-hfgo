HFGO Analysis for Elasticsearch
=============================

Analyzer:  `hfgo_max_word`

Versions
--------

IK version | ES version
-----------|-----------
master | 6.x -> master

Install
-------

1.download or compile

* optional 1 - download pre-build package from here: https://github.com/whnbeyond/elasticsearch-analysis-hfgo/tree/master/releases

    create plugin folder `cd your-es-root/plugins/ && mkdir ik`

    unzip plugin to folder `your-es-root/plugins/ik`

2.restart elasticsearch



#### Quick Example

1.create a index

```bash
curl -XPUT http://localhost:9200/index
```

2.create a mapping

```bash
curl -XPOST http://localhost:9200/index/_mapping -H 'Content-Type:application/json' -d'
{
        "properties": {
            "content": {
                "type": "text",
                "analyzer": "hfgo_max_word",
                "search_analyzer": "hfgo_max_word"
            }
        }

}'
```

3.index some docs

```bash
curl -XPOST http://localhost:9200/index/_create/1 -H 'Content-Type:application/json' -d'
{"content":"中国联通话费购"}
'
```


4.query

```bash
curl -XPOST http://localhost:9200/index/_search  -H 'Content-Type:application/json' -d'
{
    "query" : { "match" : { "content" : "国联" }}
}
'
```

Result

```json
{
    "took": 1,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "failed": 0
    },
    "hits": {
        "total": 1,
        "max_score": 2,
        "hits": [
            {
                "_index": "index",
                "_type": "fulltext",
                "_id": "1",
                "_score": 2,
                "_source": {
                    "content": "中国联通话费购"
                }
            }
        ]
    }
}
```


### 说明

该插件对文本进行最大顺序排列组合，使任意、顺序组合的文字都作为一个分词。（如果文本中包含英文，会被统一转换为小写）<br>
例如，对“中国联通话费购”进行分词，则获取的分词为：

```
中国联通话费购
中国联通话费
中国联通话
中国联通
中国联
中国
中
国联通话费购
国联通话费
国联通话
国联通
国联
国
联通话费购
联通话费
联通话
联通
联
通话费购
通话费
通话
通
话费购
话费
话
费购
费
购
```
从而达到输入任意词或词组，都可搜索的目的。

常见问题
-------

1.哪种场景会导致报错异常？

在插入数据时，目前版本不支持一个请求中带有多个文档的情况，测试中发现，如果文档数量过多，处理分词的数组会发生越界异常，以及lucene的一些游标异常。因为这个分词器处理过程中，会对游标左右移动，而不是顺序读取，elasticsearch在处理批量文档时，并不是按单个文档单个处理方式，会导致如上异常。
建议每个文档单独插入。


