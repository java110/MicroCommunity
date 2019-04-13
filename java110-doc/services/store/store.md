### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2018-07-29|wuxw

### 本页内容

1. [保存商户信息](#保存商户信息)
2. [保存商户属性信息](#保存商户属性信息)
3. [保存商户照片信息](#保存商户照片信息)
4. [保存商户证件信息](#保存商户证件信息)
5. [保存商户信息请求报文格式](#保存商户信息请求报文格式)
6. [修改商户信息](#修改商户信息)
7. [修改商户属性信息](#修改商户属性信息)
8. [修改商户照片信息](#修改商户照片信息)
9. [修改商户证件信息](#修改商户证件信息)
10. [修改商户信息请求报文格式](#修改商户信息请求报文格式)
11. [删除商户信息](#删除商户信息)
12. [删除商户属性信息](#删除商户属性信息)
13. [删除商户照片信息](#删除商户照片信息)
14. [删除商户证件信息](#删除商户证件信息)
15. [删除商户信息请求报文格式](#删除商户信息请求报文格式)
16. [商户成员加入](#商户成员加入)
17. [商户成员加入请求报文格式](#商户成员加入请求报文格式)
18. [商户成员退出](#商户成员退出)
19. [商户成员退出请求报文格式](#商户成员退出请求报文格式)

### 商户协议

orders节点 和 business节点在中心服务（center）中已经介绍，这里不再介绍，查看请点[orders和business介绍](center)，这里我们介绍datas节点下内容

##### 保存商户信息

serviceCode 为 save.store.info 保存商户属性

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessStore|1|Object|-|商户节点信息|-
 businessStore|storeId|1|String|30|商户ID|新增时传-1
 businessStore|userId|1|String|30|用户ID|传已有用户ID
 businessStore|name|1|String|100|商户名称|商户或店铺名称
 businessStore|address|1|String|200|商户地址|-
 businessStore|tel|1|String|11|联系电话|-
 businessStore|storeTypeCd|1|String|10|店铺种类|详见表store_type定义
 businessStore|nearbyLandmarks|1|String|200|地标|地标，如王府井北60米
 businessStore|mapX|1|String|20|地区 x坐标|-
 businessStore|mapY|1|String|20|地区 y坐标|-

详细报文[协议报文](#保存商户信息请求报文格式)

##### 保存商户属性信息
serviceCode 为 save.store.info 保存商户属性

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessStoreAttr|1|Object|-|商户节点信息|-
businessStoreAttr|storeId|1|String|30|商户ID|商户新增的情况下传-1
businessStoreAttr|attrId|1|String|30|属性id|新增时传-1
businessStoreAttr|specCd|1|String|12|属性编码|商户服务提供
businessStoreAttr|value|1|String|50|属性值|-

详细报文[协议报文](#保存商户信息请求报文格式)

##### 保存商户照片信息
serviceCode 为 save.store.info 保存商户照片

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessStorePhoto|1|Object|-|商户照片节点信息|-
 businessStorePhoto|storePhotoId|1|String|30|商户照片ID|新增时传-1
 businessStorePhoto|storeId|1|String|30|商户ID|商户新增时传-1
 businessStorePhoto|storePhotoTypeCd|1|String|12|商户照片类型|商户照片类型,T 门头照 I 内景照
 businessStorePhoto|photo|1|String|100|照片|照片

详细报文[协议报文](#保存商户信息请求报文格式)

##### 保存商户证件信息
serviceCode 为 save.store.info 保存商户证件

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessStoreCerdentials|1|Object|-|商户证件节点信息|-
 businessStoreCerdentials|storeCerdentialsId|1|String|30|商户证件ID|新增时传-1
businessStoreCerdentials|storeId|1|String|30|商户ID|商户新增时传-1
businessStoreCerdentials|credentialsCd|1|String|12|证件类型|对应于 credentials表
businessStoreCerdentials|value|1|String|50|证件号码|-
businessStoreCerdentials|validityPeriod|1|String|-|有效期|格式为YYYY-MM-DD hh:mm:ss 例如：2018-07-07 11:04:00 如果是长期有效 写成 3000-01-01 00:00:00
businessStoreCerdentials|positivePhoto|1|String|100|正面照片|照片存储大数据中路径
businessStoreCerdentials|negativePhoto|1|String|100|反面照片|照片存储大数据中路径

详细报文[协议报文](#保存商户信息请求报文格式)

##### 保存商户信息请求报文格式

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
    "serviceCode": "save.store.info",
    "serviceName": "保存商户信息",
    "remark": "备注",
    "datas": {
      "businessStore": {
        "storeId": "-1",
        "userId": "用户ID",
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
        "storeId": "-1",
        "attrId":"-1",
        "specCd":"1001",
        "value":"01"
      }],
      "businessStorePhoto":[{
        "storePhotoId":"-1",
        "storeId":"-1",
        "storePhotoTypeCd":"T",
        "photo":"12345678.jpg"
      }],
      "businessStoreCerdentials":[{
        "storeCerdentialsId":"-1",
        "storeId":"-1",
        "credentialsCd":"1",
        "value":"632126XXXXXXXX2011",
        "validityPeriod":"有效期，长期有效请写3000/01/01",
        "positivePhoto":"正面照片地址，1234567.jpg",
        "negativePhoto":"反面照片地址，没有不填写"
      }]
    },
    "attrs": [{
      "specCd": "配置的字段ID",
      "value": "具体值"
    }]
  }]
}
```

##### 修改商户信息

serviceCode 为 update.store.info 修改商户信息

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessStore|1|Object|-|商户节点信息|-
 businessStore|storeId|1|String|30|商户ID|传需要修改的商户ID
 businessStore|userId|1|String|30|用户ID|传已有用户ID
 businessStore|name|1|String|100|商户名称|商户或店铺名称
 businessStore|address|1|String|200|商户地址|-
 businessStore|tel|1|String|11|联系电话|-
 businessStore|storeTypeCd|1|String|10|店铺种类|详见表store_type定义
 businessStore|nearbyLandmarks|1|String|200|地标|地标，如王府井北60米
 businessStore|mapX|1|String|20|地区 x坐标|-
 businessStore|mapY|1|String|20|地区 y坐标|-

 详细报文[协议报文](#修改商户信息请求报文格式)


 ##### 修改商户属性信息
 serviceCode 为 update.store.info 修改商户属性

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|businessStoreAttr|1|Object|-|商户节点信息|-
 businessStoreAttr|storeId|1|String|30|商户ID|已有商户ID
 businessStoreAttr|attrId|1|String|30|属性id|已有属性ID
 businessStoreAttr|specCd|1|String|12|属性编码|商户服务提供
 businessStoreAttr|value|1|String|50|属性值|-

 详细报文[协议报文](#修改商户信息请求报文格式)

 ##### 修改商户照片信息
 serviceCode 为 update.store.info 修改商户照片

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|businessStorePhoto|1|Object|-|商户照片节点信息|-
  businessStorePhoto|storePhotoId|1|String|30|商户照片ID|已有商户照片ID
  businessStorePhoto|storeId|1|String|30|商户ID|已有商户ID
  businessStorePhoto|storePhotoTypeCd|1|String|12|商户照片类型|商户照片类型,T 门头照 I 内景照
  businessStorePhoto|photo|1|String|100|照片|照片

 详细报文[协议报文](#修改商户信息请求报文格式)

 ##### 修改商户证件信息
 serviceCode 为 update.store.info 修改商户证件

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|businessStoreCerdentials|1|Object|-|商户证件节点信息|-
  businessStoreCerdentials|storeCerdentialsId|1|String|30|商户证件ID|已有证件ID
 businessStoreCerdentials|storeId|1|String|30|商户ID|已有商户ID
 businessStoreCerdentials|credentialsCd|1|String|12|证件类型|对应于 credentials表
 businessStoreCerdentials|value|1|String|50|证件号码|-
 businessStoreCerdentials|validityPeriod|1|String|-|有效期|格式为YYYY-MM-DD hh:mm:ss 例如：2018-07-07 11:04:00 如果是长期有效 写成 3000-01-01 00:00:00
 businessStoreCerdentials|positivePhoto|1|String|100|正面照片|照片存储大数据中路径
 businessStoreCerdentials|negativePhoto|1|String|100|反面照片|照片存储大数据中路径

 详细报文[协议报文](#修改商户信息请求报文格式)

##### 修改商户信息请求报文格式

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
    "serviceCode": "update.store.info",
    "serviceName": "修改商户信息",
    "remark": "备注",
    "datas": {
      "businessStore": {
        "storeId": "123456",
        "userId": "用户ID",
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
        "storeId": "123456",
        "attrId":"1234546",
        "specCd":"1001",
        "value":"01"
      }],
      "businessStorePhoto":[{
        "storePhotoId":"12321",
        "storeId": "123456",
        "storePhotoTypeCd":"T",
        "photo":"12345678.jpg"
      }],
      "businessStoreCerdentials":[{
        "storeCerdentialsId":"123123",
        "storeId": "123456",
        "credentialsCd":"1",
        "value":"632126XXXXXXXX2011",
        "validityPeriod":"有效期，长期有效请写3000/01/01",
        "positivePhoto":"正面照片地址，1234567.jpg",
        "negativePhoto":"反面照片地址，没有不填写"
      }]
    },
    "attrs": [{
      "specCd": "配置的字段ID",
      "value": "具体值"
    }]
  }]
}
```

##### 删除商户信息

serviceCode 为 delete.store.info 删除商户信息

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessStore|1|Object|-|商户节点信息|-
 businessStore|storeId|1|String|30|商户ID|传需要删除的商户ID

 详细报文[协议报文](#删除商户信息请求报文格式)

 ##### 删除商户属性信息
 serviceCode 为 delete.store.info 删除商户属性

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|businessStoreAttr|1|Object|-|商户节点信息|-
 businessStoreAttr|storeId|1|String|30|商户ID|已有商户ID
 businessStoreAttr|attrId|1|String|30|属性id|已有属性ID

 详细报文[协议报文](#删除商户信息请求报文格式)

 ##### 删除商户照片信息
 serviceCode 为 delete.store.info 修改商户照片

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|businessStorePhoto|1|Object|-|商户照片节点信息|-
  businessStorePhoto|storePhotoId|1|String|30|商户照片ID|已有商户照片ID
  businessStorePhoto|storeId|1|String|30|商户ID|已有商户ID

 详细报文[协议报文](#删除商户信息请求报文格式)

 ##### 删除商户证件信息
 serviceCode 为 delete.store.info 删除商户证件

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|businessStoreCerdentials|1|Object|-|商户证件节点信息|-
  businessStoreCerdentials|storeCerdentialsId|1|String|30|商户证件ID|已有证件ID
 businessStoreCerdentials|storeId|1|String|30|商户ID|已有商户ID

 详细报文[协议报文](#删除商户信息请求报文格式)

##### 删除商户信息请求报文格式

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
    "serviceCode": "delete.store.info",
    "serviceName": "修改商户信息",
    "remark": "备注",
    "datas": {
      "businessStore": {
        "storeId": "123456"
      },
      "businessStoreAttr": [{
        "storeId": "123456",
        "attrId":"1234546"
      }],
      "businessStorePhoto":[{
        "storePhotoId":"12321",
        "storeId": "123456"
      }],
      "businessStoreCerdentials":[{
        "storeCerdentialsId":"123123",
        "storeId": "123456"
      }]
    },
    "attrs": [{
      "specCd": "配置的字段ID",
      "value": "具体值"
    }]
  }]
}
```

#### 商户成员加入
serviceCode 为 member.joined.store 成员加入

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessMemberStore|1|Object|-|成员加入节点信息|-
 businessMemberStore|memberStoreId|1|String|30|id|传-1
businessMemberStore|storeId|1|String|30|商户ID|已有商户ID
businessMemberStore|memberId|1|String|30|成员商户ID|已有商户ID


#####  商户成员加入请求报文格式

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
    "serviceCode": "member.joined.store",
    "serviceName": "商户成员加入",
    "remark": "备注",
    "datas": {
      "businessMemberStore": {
        "memberStoreId": "-1",
        "storeId": "1234",
        "memberId": "45677",
      }
    },
    "attrs": [{
      "specCd": "配置的字段ID",
      "value": "具体值"
    }]
  }]
}
```

#### 商户成员退出
serviceCode 为 member.quit.store 成员加入

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessMemberStore|1|Object|-|成员加入节点信息|-
 businessMemberStore|memberStoreId|1|String|30|id|已有成员关系ID


#####  商户成员退出请求报文格式

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
    "serviceCode": "member.quit.store",
    "serviceName": "商户成员加入",
    "remark": "备注",
    "datas": {
      "businessMemberStore": {
        "memberStoreId": "123"
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
