### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2018-07-24|wuxw

### 测试说明
测试人员|测试时间|测试结果|联系邮箱
:-:|:-:|:-:|:-:|
wuxw|2018-5-25|通过|928255095@qq.com

### 本页内容

1. [保存用户测试](#保存用户测试)

### 保存用户测试

##### 测试地址：
http://135.192.86.200:8001/httpApi/service

测试时135.192.86.200替换成自己的ip

##### 请求报文：

```
{
"orders": {
	"appId": "8000418001",
	"transactionId": "100000000020180409224736000001",
	"userId": "1000123",
	"orderTypeCd": "D",
	"requestTime": "20180409224736",
	"remark": "备注",
	"sign": "",
	"attrs": [{
		"specCd": "100001",
		"value": "测试单"
	}]
},
"business": [{
	"serviceCode": "save.user.info",
	"serviceName": "保存用户信息",
	"remark": "备注",
	"datas": {
		"businessUser": {
			"userId": "-1",
			"name": "张三",
			"email": "928255095@qq.com",
			"address": "青海省西宁市城中区129号",
			"password": "ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK",
			"locationCd": "001",
			"age": 19,
			"sex": "0",
			"tel": "17797173943",
			"levelCd": "1",
			"businessUserAttr": [{
				"attrId": "-1",
				"specCd": "1001",
				"value": "01"
			}]
		}
	},
	"attrs": [{
		"specCd": "200001",
		"value": "1000123"
	}]
}]
}
```

##### 返回报文：

```
{
	"business": [{
		"serviceCode": "save.user.info",
		"response": {
			"code": "0000",
			"message": "成功"
		},
		"responseTime": "20180524011054",
		"bId": "202018052400000046",
		"businessType": "B",
		"userId": "300000000001",
		"transactionId": "100000120180524000048",
		"dataFlowId": "20002018052400000043"
	}],
	"orders": {
		"response": {
			"code": "0000",
			"message": "成功"
		},
		"responseTime": "20180524011054",
		"sign": "",
		"transactionId": "100000000020180409224736000001"
	}
}
```

[>回到首页](home)
