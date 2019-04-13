### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2018-07-24|wuxw



### 本页内容

1. [保存商品信息](#保存商品信息)
2. [保存商品属性信息](#保存商品属性信息)
3. [保存商品照片](#保存商品照片)
4. [保存商品属性离散值](#保存商品属性离散值)
5. [保存商品优惠](#保存商品优惠)
6. [保存商品描述](#保存商品描述)
7. [保存商品信息请求报文格式](#保存商品信息请求报文格式)
8. [保存商品目录](#保存商品目录)
9. [保存商品目录请求报文格式](#保存商品目录请求报文格式)
10. [修改商品目录](#修改商品目录)
11. [修改商品目录协议](#修改商品目录协议)
12. [删除商品目录](#删除商品目录)
13. [删除商品目录协议](#删除商品目录协议)
14. [购买商品](#购买商品)
15. [购买商品请求报文格式](#购买商品请求报文格式)
16. [修改商品信息](#修改商品信息)
17. [修改商品属性信息](#修改商品属性信息)
18. [修改商品照片](#修改商品照片)
19. [修改商品属性离散值](#修改商品属性离散值)
20. [修改商品优惠](#修改商品优惠)
21. [修改商品描述](#修改商品描述)
22. [修改商品信息请求报文格式](#修改商品信息请求报文格式)
23. [删除商品信息](#删除商品信息)
24. [删除商品属性信息](#删除商品属性信息)
25. [删除商品照片](#删除商品照片)
26. [删除商品属性离散值](#删除商品属性离散值)
27. [删除商品优惠](#删除商品优惠)
28. [删除商品描述](#删除商品描述)
29. [删除商品信息请求报文格式](#删除商品信息请求报文格式)

### 操作说明

新建客户 ——————>新建商户———————>新建商品目录————————>新建商品———————>购买商品


### 商品协议

orders节点 和 business节点在中心服务（center）中已经介绍，这里不再介绍，查看请点[orders和business介绍](center)，这里我们介绍datas节点下内容

##### 保存商品信息
serviceCode 为 save.shop.info 保存商品

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessShop|1|Object|-|用户节点信息|-
businessShop|shopId|1|String|30|商品ID|新增时，传-1
businessShop|storeId|1|String|30|商户ID|填写商户ID
businessShop|catalogId|1|String|30|商品目录ID|-
businessShop|name|1|String|100|商品名称|-
businessShop|hotBuy|1|String|2|是否热卖商品|是否热卖 Y是 N否
businessShop|salePrice|1|String|10.2|商品售价|商品销售价,再没有打折情况下显示，例如12.00
businessShop|openShopCount|1|String|2|是否显示库存|是否开启库存管理，默认N Y开启，N关闭，开启后界面显示数量，如果为0 则下架
businessShop|shopCount|1|int|-|商品库存数量|-
businessShop|startDate|1|String|-|开始时间|格式为YYYY-MM-DD hh:mm:ss 例如：2018-07-07 11:04:00
businessShop|endDate|1|String|-|结束时间|格式为YYYY-MM-DD hh:mm:ss 例如：2018-07-07 11:04:00

详细报文[协议报文](#保存商品信息请求报文格式)

##### 保存商品属性信息
serviceCode 为 save.shop.info 保存商品属性

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessShopAttr|1|Object|-|用户节点信息|-
businessShopAttr|shopId|1|String|30|商品ID|商品也新增的情况下传-1
businessShopAttr|attrId|1|String|30|属性id|新增时传-1
businessShopAttr|specCd|1|String|12|属性编码|商品服务提供
businessShopAttr|value|1|String|50|属性值|-

详细报文[协议报文](#保存商品信息请求报文格式)

##### 保存商品照片
serviceCode 为 save.shop.info 保存商品照片

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessShopPhoto|1|Object|-|商品照片节点信息|-
businessShopPhoto|shopId|1|String|30|商品ID|商品也新增的情况下传-1
businessShopPhoto|shopPhotoId|1|String|30|商品照片ID|新增时传-1
businessShopPhoto|shopPhotoTypeCd|1|String|商品照片类型|商品照片类型,L logo O 其他照片
businessShopPhoto|photo|1|String|照片|照片路径，或照片名称

详细报文[协议报文](#保存商品信息请求报文格式)

##### 保存商品属性离散值
serviceCode 为 save.shop.info 保存商品属性离散值

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessShopAttrParam|1|Object|-|商品属性离散值|-
businessShopAttrParam|shopId|1|String|30|商品ID|商品也新增的情况下传-1
businessShopAttrParam|attrParamId|1|String|30|离散值ID|新增时传-1
businessShopAttrParam|specCd|1|String|12|编码|-
businessShopAttrParam|param|1|String|50|参数值|-

详细报文[协议报文](#保存商品信息请求报文格式)

##### 保存商品优惠
serviceCode 为 save.shop.info 保存商品优惠

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessShopPreferential|1|Object|-|商品优惠|-
businessShopPreferential|shopId|1|String|30|商品ID|商品也新增的情况下传-1
businessShopPreferential|shopPreferentialId|1|String|30|优惠ID|新增时传-1
businessShopPreferential|originalPrice|1|String|10,2|商品销售价|商品销售价，再没有优惠的情况下和售价是一致的,例如：12.00
businessShopPreferential|discountRate|1|String|3,2|商品打折率|例如 0.90 打9折
businessShopPreferential|showOriginalPrice|1|String|2|是否显示原价|是否显示原价，Y显示，N 不显示
businessShopPreferential|preferentialStartDate|1|String|-|优惠开始时间|格式为YYYY-MM-DD hh:mm:ss 例如：2018-07-07 11:04:00
businessShopPreferential|preferentialEndDate|1|String|-|优惠结束时间|格式为YYYY-MM-DD hh:mm:ss 例如：2018-07-07 11:04:00

详细报文[协议报文](#保存商品信息请求报文格式)

##### 保存商品描述
serviceCode 为 save.shop.info 保存商品描述

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessShopDesc|1|Object|-|商品描述|-
businessShopDesc|shopId|1|String|30|商品ID|商品也新增的情况下传-1
businessShopDesc|shopDescId|1|String|30|商品描述ID|新增时传-1
businessShopDesc|shopDescribe|1|String|-|商品描述|-

###### 保存商品信息请求报文格式：
 ```
 {
   "orders": {
     "appId": "外系统ID，分配得到",
     "transactionId": "100000000020180409224736000001",
     "userId": "用户ID",
     "orderTypeCd": "订单类型,查询,受理",
     "requestTime": "20180409224736",
     "remark": "备注",
     "sign": "这个服务是否要求MD5签名",
     "attrs": [{
       "specCd": "配置的字段ID",
       "value": "具体值"
     }]
   },
   "business": [{
     "serviceCode": "save.shop.info",
     "serviceName": "保存商品信息",
     "remark": "备注",
     "datas": {
       "businessShop": {
         "shopId": "-1",
         "storeId": "123",
         "catalogId":"-1",
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

 ##### 保存商品目录
 serviceCode 为 save.shop.catalog 保存商品目录

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|businessShopCatalog|1|Object|-|商品目录|-
businessShopCatalog|catalogId|1|String|30|目录ID|新增时传-1
businessShopCatalog|storeId|1|String|30|商户ID|对应商户ID
businessShopCatalog|name|1|String|100|目录名称|-
businessShopCatalog|level|1|String|2|目录级别|-
businessShopCatalog|parentCatalogId|1|String|30|父目录ID|如果一级目录传-1

###### 保存商品目录请求报文格式：
 ```
 {
   "orders": {
     "appId": "外系统ID，分配得到",
     "transactionId": "100000000020180409224736000001",
     "userId": "用户ID",
     "orderTypeCd": "订单类型,查询,受理",
     "requestTime": "20180409224736",
     "remark": "备注",
     "sign": "这个服务是否要求MD5签名",
     "attrs": [{
       "specCd": "配置的字段ID",
       "value": "具体值"
     }]
   },
   "business": [{
     "serviceCode": "save.shop.catalog",
     "serviceName": "保存商品目录",
     "remark": "备注",
     "datas": {
       "businessShopCatalog":{
         "catalogId":"-1",
         "storeId":"123",
         "name":"盖浇饭",
         "level":"1",
         "parentCatalogId":"-1"
       }
     },
     "attrs": [{
       "specCd": "配置的字段ID",
       "value": "具体值"
     }]
   }]
 }
 ```

 ##### 修改商品目录

 serviceCode 为 update.shop.catalog 修改商品目录

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|businessShopCatalog|1|Object|-|商品目录|-
businessShopCatalog|catalogId|1|String|30|目录ID|已有目录ID
businessShopCatalog|storeId|1|String|30|商户ID|对应商户ID
businessShopCatalog|name|1|String|100|目录名称|-
businessShopCatalog|level|1|String|2|目录级别|-
businessShopCatalog|parentCatalogId|1|String|30|父目录ID|如果一级目录传-1

##### 修改商品目录协议
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

##### 删除商品目录

serviceCode 为 delete.shop.catalog 删除商品目录

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessShopCatalog|1|Object|-|商品目录|-
businessShopCatalog|catalogId|1|String|30|目录ID|已有目录ID
businessShopCatalog|storeId|1|String|30|商户ID|对应商户ID

##### 删除商品目录协议
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

 ##### 购买商品

 serviceCode 为 buy.shop.info 购买商品信息

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|businessBuyShop|1|Object|-|购买商品|-
businessBuyShop|shopId|1|String|30|商品ID|填写已有商品ID
businessBuyShop|buyId|1|String|30|购买ID|传-1
businessBuyShop|buyCount|1|int|-|商品数量|-
datas|businessBuyShopAttr|?|Object|-|购买商品属性|-
businessBuyShopAttr|buyId|1|String|30|购买ID|传-1
businessBuyShopAttr|attrId|1|String|30|属性id|新增时传-1
businessBuyShopAttr|specCd|1|String|12|属性编码|商品服务提供
businessBuyShopAttr|value|1|String|50|属性值|-

###### 购买商品请求报文格式：
 ```
 {
   "orders": {
     "appId": "外系统ID，分配得到",
     "transactionId": "100000000020180409224736000001",
     "userId": "用户ID",
     "orderTypeCd": "订单类型,查询,受理",
     "requestTime": "20180409224736",
     "remark": "备注",
     "sign": "这个服务是否要求MD5签名",
     "attrs": [{
       "specCd": "配置的字段ID",
       "value": "具体值"
     }]
   },
   "business": [{
     "serviceCode": "buy.shop.info",
     "serviceName": "购买",
     "remark": "备注",
     "datas": {
       "businessBuyShop": {
         "shopId": "123",
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

 ##### 修改商品信息
 serviceCode 为 update.shop.info 修改商品

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|businessShop|1|Object|-|用户节点信息|-
 businessShop|shopId|1|String|30|商品ID|传已有商品ID
 businessShop|storeId|1|String|30|商户ID|填写商户ID
 businessShop|catalogId|1|String|30|商品目录ID|-
 businessShop|name|1|String|100|商品名称|-
 businessShop|hotBuy|1|String|2|是否热卖商品|是否热卖 Y是 N否
 businessShop|salePrice|1|String|10.2|商品售价|商品销售价,再没有打折情况下显示，例如12.00
 businessShop|openShopCount|1|String|2|是否显示库存|是否开启库存管理，默认N Y开启，N关闭，开启后界面显示数量，如果为0 则下架
 businessShop|shopCount|1|int|-|商品库存数量|-
 businessShop|startDate|1|String|-|开始时间|格式为YYYY-MM-DD hh:mm:ss 例如：2018-07-07 11:04:00
 businessShop|endDate|1|String|-|结束时间|格式为YYYY-MM-DD hh:mm:ss 例如：2018-07-07 11:04:00

 详细报文[协议报文](#修改商品信息请求报文格式)

 ##### 修改商品属性信息
 serviceCode 为 update.shop.info 修改商品属性

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|businessShopAttr|1|Object|-|用户节点信息|-
 businessShopAttr|shopId|1|String|30|商品ID|传已有商品ID
 businessShopAttr|attrId|1|String|30|属性id|已有属性ID
 businessShopAttr|specCd|1|String|12|属性编码|商品服务提供
 businessShopAttr|value|1|String|50|属性值|-

 详细报文[协议报文](#修改商品信息请求报文格式)

 ##### 修改商品照片
 serviceCode 为 update.shop.info 修改商品照片

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|businessShopPhoto|1|Object|-|商品照片节点信息|-
 businessShopPhoto|shopId|1|String|30|商品ID|已有商品ID
 businessShopPhoto|shopPhotoId|1|String|30|商品照片ID|已有商品照片ID
 businessShopPhoto|shopPhotoTypeCd|1|String|商品照片类型|商品照片类型,L logo O 其他照片
 businessShopPhoto|photo|1|String|照片|照片路径，或照片名称

 详细报文[协议报文](#修改商品信息请求报文格式)

 ##### 修改商品属性离散值
 serviceCode 为 update.shop.info 修改商品属性离散值

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|businessShopAttrParam|1|Object|-|商品属性离散值|-
 businessShopAttrParam|shopId|1|String|30|商品ID|已有商品ID
 businessShopAttrParam|attrParamId|1|String|30|离散值ID|已有离散值ID
 businessShopAttrParam|specCd|1|String|12|编码|-
 businessShopAttrParam|param|1|String|50|参数值|-

 详细报文[协议报文](#修改商品信息请求报文格式)

 ##### 修改商品优惠
 serviceCode 为 update.shop.info 修改商品优惠

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|businessShopPreferential|1|Object|-|商品优惠|-
 businessShopPreferential|shopId|1|String|30|商品ID|已有商品ID
 businessShopPreferential|shopPreferentialId|1|String|30|优惠ID|已有商品优惠ID
 businessShopPreferential|originalPrice|1|String|10,2|商品销售价|商品销售价，再没有优惠的情况下和售价是一致的,例如：12.00
 businessShopPreferential|discountRate|1|String|3,2|商品打折率|例如 0.90 打9折
 businessShopPreferential|showOriginalPrice|1|String|2|是否显示原价|是否显示原价，Y显示，N 不显示
 businessShopPreferential|preferentialStartDate|1|String|-|优惠开始时间|格式为YYYY-MM-DD hh:mm:ss 例如：2018-07-07 11:04:00
 businessShopPreferential|preferentialEndDate|1|String|-|优惠结束时间|格式为YYYY-MM-DD hh:mm:ss 例如：2018-07-07 11:04:00

 详细报文[协议报文](#修改商品信息请求报文格式)

 ##### 修改商品描述
 serviceCode 为 update.shop.info 修改商品描述

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|businessShopDesc|1|Object|-|商品描述|-
 businessShopDesc|shopId|1|String|30|商品ID|已有商品ID
 businessShopDesc|shopDescId|1|String|30|商品描述ID|已有商品描述ID
 businessShopDesc|shopDescribe|1|String|-|商品描述|-


##### 修改商品信息请求报文格式
```
 {
  "orders": {
    "appId": "外系统ID，分配得到",
    "transactionId": "100000000020180409224736000001",
    "userId": "用户ID",
    "orderTypeCd": "订单类型,查询,受理",
    "requestTime": "20180409224736",
    "remark": "备注",
    "sign": "这个服务是否要求MD5签名",
    "attrs": [{
      "specCd": "配置的字段ID",
      "value": "具体值"
    }]
  },
  "business": [{
    "serviceCode": "update.shop.info",
    "serviceName": "修改商品信息",
    "remark": "备注",
    "datas": {
      "businessShop": {
        "shopId": "123456",
        "storeId": "123",
        "catalogId":"-1",
        "name": "西红柿鸡蛋盖浇饭",
        "hotBuy": "Y",
        "salePrice": "12.00",
        "openShopCount": "N",
        "shopCount": "1",
        "startDate": "2018-07-07 11:04:00",
        "endDate": "2019-07-07 11:04:00"
      },
      "businessShopAttr": [{
        "shopId": "123456",
        "attrId":"123123",
        "specCd":"1001",
        "value":"01"
      }],
      "businessShopPhoto":[{
        "shopPhotoId":"123123",
        "shopId":"123456",
        "shopPhotoTypeCd":"L",
        "photo":"123.jpg"
      }],
      "businessShopAttrParam":[{
        "attrParamId":"123123",
        "shopId":"123456",
        "specCd":"123",
        "param":"加米"
      }],
      "businessShopPreferential":{
        "shopPreferentialId":"23213",
        "shopId":"123456",
        "originalPrice":"12.00",
        "discountRate":"1.00",
        "showOriginalPrice":"N",
        "preferentialStartDate":"2018-07-07 12:17:00",
        "preferentialEndDate":"2018-07-08 12:17:00"
      },
      "businessShopDesc":{
        "shopDescId":"343434",
        "shopId":"123456",
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

##### 删除商品信息

serviceCode 为 delete.shop.info 删除商品

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessShop|1|Object|-|用户节点信息|-
businessShop|shopId|1|String|30|商品ID|传已有商品ID

详细报文[协议报文](#删除商品信息请求报文格式)

##### 删除商品属性信息
serviceCode 为 delete.shop.info 删除商品属性

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessShopAttr|1|Object|-|用户节点信息|-
businessShopAttr|shopId|1|String|30|商品ID|传已有商品ID
businessShopAttr|attrId|1|String|30|属性id|已有属性ID

详细报文[协议报文](#删除商品信息请求报文格式)

##### 删除商品照片
serviceCode 为 delete.shop.info 删除商品照片

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessShopPhoto|1|Object|-|商品照片节点信息|-
businessShopPhoto|shopId|1|String|30|商品ID|已有商品ID
businessShopPhoto|shopPhotoId|1|String|30|商品照片ID|已有商品照片ID

详细报文[协议报文](#删除商品信息请求报文格式)

##### 删除商品属性离散值
serviceCode 为 delete.shop.info 删除商品属性离散值

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessShopAttrParam|1|Object|-|商品属性离散值|-
businessShopAttrParam|shopId|1|String|30|商品ID|已有商品ID
businessShopAttrParam|attrParamId|1|String|30|离散值ID|已有离散值ID

详细报文[协议报文](#删除商品信息请求报文格式)

##### 删除商品优惠
serviceCode 为 delete.shop.info 删除商品优惠

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessShopPreferential|1|Object|-|商品优惠|-
businessShopPreferential|shopId|1|String|30|商品ID|已有商品ID
businessShopPreferential|shopPreferentialId|1|String|30|优惠ID|已有商品优惠ID

详细报文[协议报文](#删除商品信息请求报文格式)

##### 删除商品描述
serviceCode 为 delete.shop.info 删除商品描述

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessShopDesc|1|Object|-|商品描述|-
businessShopDesc|shopId|1|String|30|商品ID|已有商品ID
businessShopDesc|shopDescId|1|String|30|商品描述ID|已有商品描述ID

##### 删除商品信息请求报文格式
```
{
  "orders": {
    "appId": "外系统ID，分配得到",
    "transactionId": "100000000020180409224736000001",
    "userId": "用户ID",
    "orderTypeCd": "订单类型,查询,受理",
    "requestTime": "20180409224736",
    "remark": "备注",
    "sign": "这个服务是否要求MD5签名",
    "attrs": [{
      "specCd": "配置的字段ID",
      "value": "具体值"
    }]
  },
  "business": [{
    "serviceCode": "delete.shop.info",
    "serviceName": "删除商品信息",
    "remark": "备注",
    "datas": {
      "businessShop": {
        "shopId": "123456"
      },
      "businessShopAttr": [{
        "shopId": "123456",
        "attrId":"123123"
      }],
      "businessShopPhoto":[{
        "shopPhotoId":"123123",
        "shopId":"123456"
      }],
      "businessShopAttrParam":[{
        "attrParamId":"123123",
        "shopId":"123456"
      }],
      "businessShopPreferential":{
        "shopPreferentialId":"23213",
        "shopId":"123456"
      },
      "businessShopDesc":{
        "shopDescId":"343434",
        "shopId":"123456"
      }
    },
    "attrs": [{
      "specCd": "配置的字段ID",
      "value": "具体值"
    }]
  }]
}
```

[>回到首页](home)
