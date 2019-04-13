

**1\. 保存商户**
###### 接口功能
> 用户通过web端或APP完善商户信息接口

###### URL
> [http://api.java110.com:8008/api/save.store.info](http://api.java110.com:8008/api/save.store.info)

###### 支持格式
> JSON

###### HTTP请求方式
> POST

###### 请求参数(header部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-:|
|app_id|1|String|30|应用ID|Api服务分配                      |
|transaction_id|1|String|30|请求流水号|不能重复 1000000000+YYYYMMDDhhmmss+6位序列 |
|sign|1|String|-|签名|请参考签名说明|
|req_time|1|String|-|请求时间|YYYYMMDDhhmmss|

###### 请求参数(body部分)
|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|businessStore|object|-|-|商户基本节点|-|
|businessStore|userId|String|-|30|用户ID|-|
|businessStore|name|1|String|50|商户名称|-|
|businessStore|address|1|String|200|商户地址|-|
|businessStore|password|?|String|200|密码|MD5加密密码|
|businessStore|tel|1|String|11|联系电话|-|
|businessStore|storeTypeCd|1|String|10|店铺种类|详见表store_type定义|
|businessStore|nearbyLandmarks|1|String|200|地标|地标，如王府井北60米|
|businessStore|mapX|1|String|20|地区 x坐标|-|
|businessStore|mapY|1|String|20|地区 y坐标|-|
|-|businessStoreAttr|?|object|-|商户属性|-|
|businessStoreAttr|specCd|1|String|12|属性编码|商户服务提供|
|businessStoreAttr|value|1|String|50|属性值|-|
|-|businessStoreCerdentials|?|object|-|商户证件|-|
|businessStoreCerdentials|credentialsCd|1|String|12|证件类型|对应于 credentials表|
|businessStoreCerdentials|value|1|String|50|证件号码|-|
|businessStoreCerdentials|validityPeriod|1|String|-|有效期|格式为YYYY-MM-DD hh:mm:ss 例如：2018-07-07 11:04:00 如果是长期有效 写成 3000-01-01 00:00:00|
|businessStoreCerdentials|positivePhoto|1|String|100|正面照片|照片存储大数据中路径|
|businessStoreCerdentials|negativePhoto|1|String|100|反面照片|照片存储大数据中路径|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，




###### 举例
> 地址：[http://api.java110.com:8008/api/save.store.info](http://api.java110.com:8008/api/save.store.info)

``` json
请求头信息：
Content-Type:application/json
USER_ID:1234
APP_ID:8000418004
TRANSACTION_ID:10029082726
REQ_TIME:20181113225612
SIGN:aabdncdhdbd878sbdudn898
请求报文：
{
      "businessStore": {
        "userId": "30518940136629616640",
        "name": "齐天超时（王府井店）",
        "address": "青海省西宁市城中区129号",
        "password": "ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK",
        "tel": "15897089471",
        "storeTypeCd": "M",
        "nearbyLandmarks": "王府井内",
        "mapX": "101.801909",
        "mapY": "36.597263"
      },
      "businessStoreAttr": [{
        "specCd":"1001",
        "value":"01"
      }],
      "businessStoreCerdentials":[{
        "credentialsCd":"300200900001",
        "value":"632126XXXXXXXX2011",
        "validityPeriod":"3000-01-01",
        "positivePhoto":"1234567.jpg",
        "negativePhoto":""
      }]

}
返回报文：
状态码为 200
成功

```
