

**1\. 查询采购/出库申请订单**
###### 接口功能
> 查询采购/出库申请订单接口

###### URL
> [http://api.java110.com:8008/api/purchaseApply.listPurchaseApplys](http://api.java110.com:8008/api/purchaseApply.listPurchaseApplys)

###### 支持格式
> JSON

###### HTTP请求方式
> GET

###### 请求参数(header部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-:|
|app_id|1|String|30|应用ID|Api服务分配                      |
|transaction_id|1|String|30|请求流水号|不能重复 1000000000+YYYYMMDDhhmmss+6位序列 |
|sign|1|String|-|签名|请参考签名说明|
|req_time|1|String|-|请求时间|YYYYMMDDhhmmss|

###### 请求参数
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|page|1|int|-|分页页数|-|
|row|1|int|-|每页展示数量|-|
|resOrderType|1|String|8|订单类型|出库类型 10000 入库 20000 出库 |
|---------------上面三个参数必填，以下查询参数选填-----------------------|
|userName|1|String|50|申请人姓名|模糊查询|
|applyOrderId|1|String|30|订单号|-|
|state|1|String|12|订单状态|1000：未审核，1001：审核中，1002：已审核详细参考t_dict表|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/purchaseApply.listPurchaseApplys?storeTypeCd=800900000003&resOrderType=10000&page=1&row=10&storeId=402019032924930007](http://api.java110.com:8008/api/purchaseApply.listPurchaseApplys?storeTypeCd=800900000003&resOrderType=10000&page=1&row=10&storeId=402019032924930007)

``` javascript
请求头信息：
Content-Type:application/json
USER_ID:1234
APP_ID:8000418002
TRANSACTION_ID:10029082726
REQ_TIME:20181113225612
SIGN:aabdncdhdbd878sbdudn898

请求报文：

无

返回报文：
{
	"page": 0,
	"purchaseApplys": [{
		"applyOrderId": "152020030967680002",
		"createTime": "2020-03-09 09:45:25",
		"description": "非法访问",
		"purchaseApplyDetailVo": [{
			"applyOrderId": "152020030967680002",
			"price": "3.00",
			"quantity": "12",
			"remark": "士大夫VS",
			"resCode": "2",
			"resId": "852020021392580008",
			"resName": "12",
			"stock": "0"
		}, {
			"applyOrderId": "152020030967680002",
			"price": "32.00",
			"quantity": "14",
			"remark": "顺风顺水",
			"resCode": "00011",
			"resId": "852020010916260001",
			"resName": "摄像头",
			"stock": "0"
		}],
		"resourceNames": "12;摄像头;",
		"state": "1000",
		"stateName": "未审核",
		"totalPrice": "484.00",
		"userName": "wuxw"
	}, {
		"applyOrderId": "152020030923790001",
		"createTime": "2020-03-09 09:35:34",
		"description": "需要购买物资",
		"purchaseApplyDetailVo": [{
			"applyOrderId": "152020030923790001",
			"price": "32.00",
			"quantity": "32",
			"resCode": "",
			"resId": "852020022343610002",
			"resName": "摄像头",
			"stock": "0"
		}, {
			"applyOrderId": "152020030923790001",
			"price": "13.00",
			"quantity": "12",
			"resCode": "rrwrfsf",
			"resId": "852020022393460003",
			"resName": "dasd",
			"stock": "0"
		}],
		"resourceNames": "摄像头;dasd;",
		"state": "1000",
		"stateName": "未审核",
		"totalPrice": "1180.00",
		"userName": "wuxw"
	}, {
		"applyOrderId": "152020030782470001",
		"createTime": "2020-03-07 15:25:08",
		"description": "bkhguy",
		"purchaseApplyDetailVo": [{
			"applyOrderId": "152020030782470001",
			"price": "32.00",
			"quantity": "99",
			"resCode": "00011",
			"resId": "852020010916260001",
			"resName": "摄像头",
			"stock": "0"
		}, {
			"applyOrderId": "152020030782470001",
			"price": "3.00",
			"quantity": "66",
			"resCode": "2",
			"resId": "852020021392580008",
			"resName": "12",
			"stock": "0"
		}],
		"resourceNames": "摄像头;12;",
		"state": "1000",
		"stateName": "未审核",
		"totalPrice": "3366.00",
		"userName": "wuxw"
	}, {
		"applyOrderId": "152020030669990003",
		"createTime": "2020-03-06 17:16:59",
		"description": "电脑一台",
		"purchaseApplyDetailVo": [{
			"applyOrderId": "152020030669990003",
			"price": "2007.00",
			"quantity": "1",
			"remark": "电脑一台",
			"resCode": "SGC0025001",
			"resId": "852020021455220010",
			"resName": "电脑测试一",
			"stock": "0"
		}],
		"resourceNames": "电脑测试一;",
		"state": "1000",
		"stateName": "未审核",
		"totalPrice": "2007.00",
		"userName": "wuxw"
	}, {
		"applyOrderId": "152020030674420002",
		"createTime": "2020-03-06 17:16:36",
		"description": "需要摄像头",
		"purchaseApplyDetailVo": [{
			"applyOrderId": "152020030674420002",
			"price": "32.00",
			"quantity": "10",
			"remark": "购买10个摄像头",
			"resCode": "00011",
			"resId": "852020010916260001",
			"resName": "摄像头",
			"stock": "0"
		}],
		"resourceNames": "摄像头;",
		"state": "1000",
		"stateName": "未审核",
		"totalPrice": "320.00",
		"userName": "wuxw"
	}],
	"records": 1,
	"rows": 0,
	"total": 5
}
```
