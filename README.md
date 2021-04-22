HFGO Analysis for Elasticsearch
=============================

Analyzer:  `hfgo_max_word`

Versions
--------

HFGO version | ES version
-----------|-----------
master | 6.x -> master

Install
-------

1.download or compile

* optional 1 - download pre-build package from here: https://github.com/whnbeyond/elasticsearch-analysis-hfgo/releases

    create plugin folder `cd your-es-root/plugins/ && mkdir hfgo`

    unzip plugin to folder `your-es-root/plugins/hfgo`

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
{"content":"中国联"}
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
                    "content": "中国联"
                }
            }
        ]
    }
}
```


# 背景：
### 不同的业务场景，对搜索结果的要求也是不一样的。<br>
场景一，商品列表搜索，用户需要的是更精准的搜索结果，比如我在京东搜【go 教程】，如果搜索结果里出现了【玩转google搜索排名教程】，我就会怀疑京东是不是出bug了。<br>
场景二，订单列表搜索，用户需要的是更全的搜索结果，比如我还是在京东的订单列表搜【go】，我需要的是所有包含了go关键字的订单，不单要把go语言的订单搜出来，google、good smile等等所有包含g、o字符的订单，都需要展示出来。<br>

# 分析规律
我们发现，两个场景虽然都是基于商品名称进行搜索，但是期望的结果是完全不同的。<br>
这里就有两个概念，一个是查准率，一个查全率。<br>
查准率，关注的是结果是否足够的精确。<br>
查全率，关注的是结果是否完全。<br>
 <img src="https://pic1.zhimg.com/80/v2-daf544189929e98d7644522864e3b784_1440w.jpeg" width = "400" height = "400"   />
<br/>


# 解决思路
所以，对于商品列表搜索，高的查准率和高的查全率，都是需要不断追求的，即便不能全部都是100%，也要不断去靠近这个目标。<br>
而对于订单列表搜索，需要的是100%的查全率，这样，我们就可以通过使用枚举的方式，生成原始文本的所有顺序组合，来达到100%的查全率。<br>
例如，对“中国联”进行分词，则获取的分词为：

```
中国联
中国
中
国联
国
联
```
从而达到输入任意词或词组都可搜索的目的。

常见问题
-------

1.哪种场景会导致报错异常？

在插入数据时，目前版本不支持一个请求中带有多个文档的情况，测试中发现，如果文档数量过多，处理分词的数组会发生越界异常以及lucene的一些游标异常。因为这个分词器在分词处理过程中，会对游标左右移动，而不是顺序读取，elasticsearch在处理批量文档时，并不是按单个文档单个处理方式，会导致如上异常。
建议每个文档单独插入。


