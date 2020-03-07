

**1\. 保存小区单元**
###### 接口功能
> API服务做保存小区单元时调用该接口

###### URL
> [http://community-service/unitApi/service](http://community-service/unitApi/service)

###### 支持格式
> JSON

###### HTTP请求方式
> POST

###### 协议接口
|父元素名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-:|
|-|orders|1|Object|-|订单节点|-|
|-|business|1|Array|-|业务节点|-|

###### orders
|父元素名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|orders|1|Object|-|订单节点|-|
|orders|appId|1|String|10|系统ID|由中心服务提供|
|orders|transactionId|1|String|30|交互流水|appId+'00'+YYYYMMDD+10位序列|
|orders|userId|1|String|30|用户ID|已有用户ID|
|orders|orderTypeCd|1|String|4|订单类型|查看订单类型说明|
|orders|requestTime|1|String|14|请求时间|YYYYMMDDhhmmss|
|orders|remark|1|String|200|备注|备注|
|orders|sign|?|String|64|签名|查看加密说明|
|orders|attrs|?|Array|-|订单属性|-|
|attrs|specCd|1|String|12|规格编码|由中心服务提供|
|attrs|value|1|String|50|属性值|-|
|orders|response|1|Object|-|返回结果节点|-|
|response|code|1|String|4|返回状态|查看状态说明|
|response|message|1|String|200|返回状态描述|-|

###### business
|父元素名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|business|?|Array|-|业务节点|-|
|business|businessTypeCd|1|String|12|业务类型编码|500100030002|
|business|datas|1|Object|-|数据节点|不同的服务下的节点不一样|
|datas|businessUnitInfo|1|Object|-|小区成员|小区成员|
|businessUnitInfo|floorId|1|String|30|-|-|
|businessUnitInfo|layerCount|1|String|30|-|-|
|businessUnitInfo|unitId|1|String|30|-|-|
|businessUnitInfo|unitNum|1|String|30|-|-|
|businessUnitInfo|lift|1|String|30|-|-|
|businessUnitInfo|remark|1|String|30|-|-|
|businessUnitInfo|userId|1|String|30|-|-|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，





###### 举例
> 地址：[http://community-service/unitApi/service](http://community-service/unitApi/service)

``` javascript
请求头信息：
Content-Type:application/json

请求报文：

{
  "orders": {
    "appId": "外系统ID，分配得到",
    "transactionId": "100000000020180409224736000001",
    "userId": "用户ID",
    "orderTypeCd": "订单类型,查询,受理",
    "requestTime": "20180409224736",
    "remark": "备注",
    "sign": "这个服务是否要求MD5签名",
    "businessType":"I",
    "attrs": [{
      "specCd": "配置的字段ID",
      "value": "具体值"
    }]
  },
  "business": {
    "businessTypeCd": "520100030001",
    "bId":"1234567892",
    "remark": "备注",
    "datas": {
      "businessUnitInfo": {
                "floorId":"填写具体值",
        "layerCount":"填写具体值",
        "unitId":"填写具体值",
        "unitNum":"填写具体值",
        "lift":"填写具体值",
        "remark":"填写具体值",
        "userId":"填写具体值"
      }
    },
    "attrs": [{
      "specCd": "配置的字段ID",
      "value": "具体值"
    }]
  }
}

返回报文：
 {
	"orderTypeCd": "D",
	"response": {
		"code": "0000",
		"message": "成功"
	},
	"responseTime": "20190418102004",
	"bId": "202019041810750003",
	"businessType": "B",
	"transactionId": "3a5a411ec65a4c3f895935638aa1d2bc",
	"dataFlowId": "44fde86d39ce46f4b4aab5f6b14f3947"
}

```
