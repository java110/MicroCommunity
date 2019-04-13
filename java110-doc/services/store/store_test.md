### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2018-07-29|wuxw

### 测试说明
测试人员|测试时间|测试结果|联系邮箱
:-:|:-:|:-:|:-:|
wuxw|2018-5-25|通过|928255095@qq.com

### 本页内容

1. [保存商户测试](#保存商户测试)
2. [修改商户测试](#修改商户测试)
3. [删除商户测试](#删除商户测试)

### 保存商户测试

##### 测试地址：
http://135.192.86.200:8001/httpApi/service

测试时135.192.86.200替换成自己的ip

##### 请求报文：

```
{
	"orders": {
		"appId": "8000418001",
		"transactionId": "100000000020180704224736000014",
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
		"serviceCode": "save.store.info",
		"serviceName": "保存商户信息",
		"remark": "备注",
		"datas": {
			"businessStore": {
				"storeId": "-1",
				"userId": "用户ID",
				"name": "齐天超时（王府井店）",
				"address": "青海省西宁市城中区129号",
				"tel": "15897089471",
				"storeTypeCd": "M",
				"nearbyLandmarks": "王府井内",
				"mapX": "101.801909",
				"mapY": "36.597263"
			},
			"businessStoreAttr": [{
				"storeId": "-1",
				"attrId": "-1",
				"specCd": "1001",
				"value": "01"
			}],
			"businessStorePhoto": [{
				"storePhotoId": "-1",
				"storeId": "-1",
				"storePhotoTypeCd": "T",
				"photo": "12345678.jpg"
			}],
			"businessStoreCerdentials": [{
				"storeCerdentialsId": "-1",
				"storeId": "-1",
				"credentialsCd": "1",
				"value": "632126XXXXXXXX2011",
				"validityPeriod": "",
				"positivePhoto": "1234567.jpg",
				"negativePhoto": ""
			}]
		},
		"attrs": [{
			"specCd": "配置的字段ID",
			"value": "具体值"
		}]
	}]
}
```

##### 返回报文：

```
{
	"business": [{
		"serviceCode": "save.store.info",
		"response": {
			"code": "0000",
			"message": "成功"
		}
	}],
	"orders": {
		"response": {
			"code": "0000",
			"message": "成功"
		},
		"responseTime": "20180524011054",
		"sign": "",
		"transactionId": "100000000020180704224736000014"
	}
}
```

### 修改商户测试

##### 测试地址：
http://135.192.86.200:8001/httpApi/service

测试时135.192.86.200替换成自己的ip

##### 请求报文：

```
{
	"orders": {
		"appId": "8000418001",
		"transactionId": "100000000020180704224736000022",
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
		"serviceCode": "update.store.info",
		"serviceName": "修改商户信息",
		"remark": "备注",
		"datas": {
			"businessStore": {
				"storeId": "40464215326209351680",
				"userId": "用户ID123",
				"name": "齐天超时（王府井店）123",
				"address": "青海省西宁市城中区129号123",
				"tel": "15897089471",
				"storeTypeCd": "M",
				"nearbyLandmarks": "王府井内123",
				"mapX": "101.801909",
				"mapY": "36.597263"
			},
			"businessStoreAttr": [{
				"storeId": "40464215326209351680",
				"attrId": "11464215326758805504",
				"specCd": "1001",
				"value": "01123"
			}],
			"businessStorePhoto": [{
				"storePhotoId": "41464215326792359936",
				"storeId": "40464215326209351680",
				"storePhotoTypeCd": "T",
				"photo": "12345678123.jpg"
			}],
			"businessStoreCerdentials": [{
				"storeCerdentialsId": "42464215326817525760",
				"storeId": "40464215326209351680",
				"credentialsCd": "1",
				"value": "632126XXXXXXXX2012",
				"validityPeriod": "3000-01-01",
				"positivePhoto": "1234567123.jpg",
				"negativePhoto": ""
			}]
		},
		"attrs": [{
			"specCd": "配置的字段ID",
			"value": "具体值"
		}]
	}]
}
```

##### 返回报文：

```
{
	"business": [{
		"serviceCode": "update.store.info",
		"response": {
			"code": "0000",
			"message": "成功"
		}
	}],
	"orders": {
		"response": {
			"code": "0000",
			"message": "成功"
		},
		"responseTime": "20180524011054",
		"sign": "",
		"transactionId": "100000000020180704224736000022"
	}
}
```

### 删除商户测试

##### 测试地址：
http://135.192.86.200:8001/httpApi/service

测试时135.192.86.200替换成自己的ip

##### 请求报文：

```
{
	"orders": {
		"appId": "8000418001",
		"transactionId": "100000000020180704224736000025",
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
		"serviceCode": "delete.store.info",
		"serviceName": "删除商户信息",
		"remark": "备注",
		"datas": {
			"businessStore": {
				"storeId": "40464215326209351680"
			},
			"businessStoreAttr": [{
				"storeId": "40464215326209351680",
				"attrId": "11464215326758805504"
			}],
			"businessStorePhoto": [{
				"storePhotoId": "41464215326792359936",
				"storeId": "40464215326209351680"
			}],
			"businessStoreCerdentials": [{
				"storeCerdentialsId": "42464215326817525760",
				"storeId": "40464215326209351680"
			}]
		},
		"attrs": [{
			"specCd": "配置的字段ID",
			"value": "具体值"
		}]
	}]
}
```

##### 返回报文：

```
{
	"business": [{
		"serviceCode": "delete.store.info",
		"response": {
			"code": "0000",
			"message": "成功"
		}
	}],
	"orders": {
		"response": {
			"code": "0000",
			"message": "成功"
		},
		"responseTime": "20180524011054",
		"sign": "",
		"transactionId": "100000000020180704224736000025"
	}
}
```

[>回到首页](home)
