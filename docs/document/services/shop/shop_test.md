### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2018-07-24|wuxw

### 测试说明
测试人员|测试时间|测试结果|联系邮箱
:-:|:-:|:-:|:-:|
wuxw|2018-5-25|通过|928255095@qq.com

### 本页内容

1. [保存商品测试](#保存商品测试)
2. [保存商品目录](#保存商品目录)
3. [修改商品信息](#修改商品信息)
4. [删除商品信息](#删除商品信息)
5. [修改商品目录](#修改商品目录)
6. [删除商品目录](#删除商品目录)
7. [购买商品](#购买商品)

### 保存商品测试

##### 测试地址：
http://135.192.86.200:8001/httpApi/service

测试时135.192.86.200替换成自己的ip

##### 请求报文：

```
{
  "orders": {
    "appId": "8000418001",
		"transactionId": "100000000020180708224736000018",
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
    "serviceCode": "save.shop.info",
    "serviceName": "保存商品信息",
    "remark": "备注",
    "datas": {
      "businessShop": {
        "shopId": "-1",
        "storeId": "40464215326209351680",
        "catalogId":"56465664285847068672",
        "name": "西红柿鸡蛋盖浇饭",
        "hotBuy": "Y",
        "salePrice": "12.00",
        "openShopCount": "N",
        "shopCount": "1",
        "startDate": "2018-07-07 11:04:00",
        "endDate": "2019-07-07 11:04:00"
      },
      "businessShopAttr": [{
        "shopId": "-1",
        "attrId":"-1",
        "specCd":"1001",
        "value":"01"
      }],
      "businessShopPhoto":[{
        "shopPhotoId":"-1",
        "shopId":"-1",
        "shopPhotoTypeCd":"L",
        "photo":"123.jpg"
      }],
      "businessShopAttrParam":[{
        "attrParamId":"-1",
        "shopId":"-1",
        "specCd":"123",
        "param":"加米"
      }],
      "businessShopPreferential":{
        "shopPreferentialId":"-1",
        "shopId":"-1",
        "originalPrice":"12.00",
        "discountRate":"1.00",
        "showOriginalPrice":"N",
        "preferentialStartDate":"2018-07-07 12:17:00",
        "preferentialEndDate":"2018-07-08 12:17:00"
      },
      "businessShopDesc":{
        "shopDescId":"-1",
        "shopId":"-1",
        "shopDescribe":""
      }
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
		"serviceCode": "save.shop.info",
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
		"transactionId": "100000000020180409224736000001"
	}
}
```

### 保存商品目录

##### 请求报文：

```
{
	"orders": {
		"appId": "8000418001",
		"transactionId": "100000000020180708224736000011",
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
		"serviceCode": "save.shop.catalog",
		"serviceName": "保存商品目录",
		"remark": "备注",
		"datas": {
			"businessShopCatalog": {
				"catalogId": "-1",
				"storeId": "40464215326209351680",
				"name": "盖浇饭",
				"level": "1",
				"parentCatalogId": "-1"
			}
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
		"serviceCode": "save.shop.catalog",
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
		"transactionId": "100000000020180409224736000001"
	}
}
```

### 修改商品信息

##### 请求报文

```
{
  "orders": {
    "appId": "8000418001",
		"transactionId": "100000000020180708224736000020",
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
    "serviceCode": "update.shop.info",
    "serviceName": "保存商品信息",
    "remark": "备注",
    "datas": {
      "businessShop": {
        "shopId": "50465675955524870144",
        "storeId": "40464215326209351680",
        "catalogId":"56465664285847068672",
        "name": "西红柿鸡蛋盖浇饭123",
        "hotBuy": "N",
        "salePrice": "12.01",
        "openShopCount": "Y",
        "shopCount": "10",
        "startDate": "2018-07-08 11:04:00",
        "endDate": "2019-07-08 11:04:00"
      },
      "businessShopAttr": [{
        "shopId": "50465675955524870144",
        "attrId":"51465675955604561920",
        "specCd":"1001",
        "value":"01123"
      }],
      "businessShopPhoto":[{
        "shopPhotoId":"52465675955638116352",
        "shopId":"50465675955524870144",
        "shopPhotoTypeCd":"O",
        "photo":"123234.jpg"
      }],
      "businessShopAttrParam":[{
        "attrParamId":"53465675955654893568",
        "shopId":"50465675955524870144",
        "specCd":"123",
        "param":"加米123"
      }],
      "businessShopPreferential":{
        "shopPreferentialId":"51231232334",
        "shopId":"50465675955524870144",
        "originalPrice":"12.03",
        "discountRate":"0.70",
        "showOriginalPrice":"Y",
        "preferentialStartDate":"2018-08-07 12:17:00",
        "preferentialEndDate":"2018-08-08 12:17:00"
      },
      "businessShopDesc":{
        "shopDescId":"55465675955675865088",
        "shopId":"50465675955524870144",
        "shopDescribe":"你好"
      }
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
		"serviceCode": "update.shop.info",
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
		"transactionId": "100000000020180409224736000001"
	}
}
```

### 删除商品信息

##### 请求报文：
```
{
  "orders": {
    "appId": "8000418001",
		"transactionId": "100000000020180708224736000023",
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
    "serviceCode": "delete.shop.info",
    "serviceName": "删除商品信息",
    "remark": "备注",
    "datas": {
      "businessShop": {
        "shopId": "50465681433294282752"
      },
      "businessShopAttr": [{
        "shopId": "50465681433294282752",
        "attrId":"51465681433361391616"
      }],
      "businessShopPhoto":[{
        "shopPhotoId":"52465681433394946048",
        "shopId":"50465681433294282752"
      }],
      "businessShopAttrParam":[{
        "attrParamId":"53465681433420111872",
        "shopId":"50465681433294282752"
      }],
      "businessShopPreferential":{
        "shopPreferentialId":"54465681433436889088",
        "shopId":"50465681433294282752"
      },
      "businessShopDesc":{
        "shopDescId":"55465681433453666304",
        "shopId":"50465681433294282752"
      }
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
		"serviceCode": "delete.shop.info",
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
		"transactionId": "100000000020180409224736000001"
	}
}
```

### 修改商品目录

##### 请求报文

```
{
	"orders": {
		"appId": "8000418001",
		"transactionId": "100000000020180708224736000012",
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
		"serviceCode": "update.shop.catalog",
		"serviceName": "保存商品目录",
		"remark": "备注",
		"datas": {
			"businessShopCatalog": {
				"catalogId": "56465664285847068672",
				"storeId": "40464215326209351680",
				"name": "盖浇饭123",
				"level": "1",
				"parentCatalogId": "-1"
			}
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
		"serviceCode": "update.shop.info",
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
		"transactionId": "100000000020180409224736000001"
	}
}
```

### 删除商品目录

##### 请求报文

```
{
	"orders": {
		"appId": "8000418001",
		"transactionId": "100000000020180708224736000013",
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
		"serviceCode": "delete.shop.catalog",
		"serviceName": "保存商品目录",
		"remark": "备注",
		"datas": {
			"businessShopCatalog": {
				"catalogId": "56465664285847068672",
				"storeId": "40464215326209351680"
			}
		},
		"attrs": [{
			"specCd": "配置的字段ID",
			"value": "具体值"
		}]
	}]
}
```

#####  返回报文
```
{
	"business": [{
		"serviceCode": "delete.shop.catalog",
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
		"transactionId": "100000000020180708224736000013"
	}
}
```

### 购买商品

##### 请求报文：

```
{
  "orders": {
    "appId": "8000418001",
		"transactionId": "100000000020180708224736000021",
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
    "serviceCode": "buy.shop.info",
    "serviceName": "购买",
    "remark": "备注",
    "datas": {
      "businessBuyShop": {
        "shopId": "50465681433294282752",
        "buyId": "-1",
        "buyCount":"10"
      },
      "businessBuyShopAttr": [{
        "buyId": "-1",
        "attrId":"-1",
        "specCd":"1001",
        "value":"01"
      }]
    },
    "attrs": [{
      "specCd": "配置的字段ID",
      "value": "具体值"
    }]
  }]
}
```

##### 返回报文

```
{
	"business": [{
		"serviceCode": "buy.shop.info",
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
		"transactionId": "100000000020180708224736000013"
	}
}
```

[>回到首页](home)
