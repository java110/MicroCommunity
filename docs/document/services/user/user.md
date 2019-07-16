### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2018-07-24|wuxw


### 本页内容

1. [保存用户信息](#保存用户信息)
2. [保存用户证件信息](#保存用户证件信息)
3. [用户打标](#用户打标)
4. [用户地址信息保存](#用户地址信息保存)

### 用户协议

orders节点 和 business节点在中心服务（center）中已经介绍，这里不再介绍，查看请点[orders和business介绍](center)，这里我们介绍datas节点下内容

##### 保存用户信息

serviceCode 为 save.user.info 保存用户

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessUser|1|Object|-|用户节点信息|-
 businessUser|userId|1|String|30|用户ID|新增时，写-1，其他时写存在userId
 businessUser|name|1|String|50|用户名称|-
 businessUser|email|?|String|30|邮箱|请填写XX@YY.ZZ格式的邮箱地址
 businessUser|address|?|String|200|现居住地址|XX市XX区XX路
 businessUser|password|?|String|128|加密后的用户密码|请用MD5加密
 businessUser|locationCd|?|String|8|用户地区|详见u_location
 businessUser|age|?|int|-|用户年龄|-
 businessUser|sex|?|String|1|用户性别|0表示男孩，1表示女孩
 businessUser|tel|?|String|11|用户手机号|-
 businessUser|level_cd|1|String|2|用户级别|关联user_level
 businessUser|businessUserAttr|?|Array|-|用户属性|-
 businessUserAttr|attrId|1|String|30|属性ID|新增时，写-1，其他时写存在attrId
 businessUserAttr|specCd|1|String|12|规格编码|由用户服务提供
 businessUserAttr|value|1|String|50|属性值|

请求报文格式：
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
        "level_cd": "1",
        "businessUserAttr": [{
          "attrId":"-1",
          "specCd":"1001",
          "value":"01"
        }]
      }
    },
    "attrs": [{
      "specCd": "配置的字段ID",
      "value": "具体值"
    }]
  }]
}
```

##### 保存用户证件信息

serviceCode 为 save.user.credentials 保存用户证件信息

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
datas|businessUserCredentials|1|Object|-|用户证件节点信息|-
businessUserCredentials|userId|1|String|30|用户ID|已有userId
businessUserCredentials|credentialsId|1|String|30|证件ID|新增写-1
businessUserCredentials|credentialsCd|1|String|12|证件类型|证件字典表
businessUserCredentials|value|1|String|50|证件号码|证件号码

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
    "serviceCode": "save.user.credentials",
    "serviceName": "用户打标保存",
    "remark": "备注",
    "datas": {
      "businessUserCredentials": {
        "userId": "1019181771",
        "credentialsId": "-1",
        "credentialsCd": "1",
        "value":"632126XXXXXXXX2011"
      }
    },
    "attrs": [{
      "specCd": "配置的字段ID",
      "value": "具体值"
    }]
  }]
}
```

##### 用户打标
serviceCode 为 save.user.tag 用户打标

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
datas|businessUserTag|1|Object|-|用户证件节点信息|-
businessUserTag|userId|1|String|30|用户ID|已有userId
businessUserTag|tagId|1|String|30|tagId|新增写-1
businessUserTag|tagCd|1|String|12|标签编码|-
businessUserTag|remark|1|String|200|备注|备注

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
    "serviceCode": "save.user.tag",
    "serviceName": "用户打标保存",
    "remark": "备注",
    "datas": {
      "businessUserTag": {
        "userId": "1019181771",
        "tagId": "-1",
        "tagCd": "1019191",
        "remark": "青海省西宁市城中区129号"
      }
    },
    "attrs": [{
      "specCd": "配置的字段ID",
      "value": "具体值"
    }]
  }]
}
```

##### 用户地址信息保存

serviceCode 为 save.user.address 用户地址信息保存

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|businessUserAddress|1|Object|-|用户证件节点信息|-
 businessUserAddress|userId|1|String|30|用户ID|已有userId
 businessUserAddress|addressId|1|String|30|addressId|新增写-1
 businessUserAddress|tel|11|String|30|电话|-
 businessUserAddress|address|200|String|30|地址|-
 businessUserAddress|postalCode|1|String|10|邮政编码|-
 businessUserAddress|isDefault|1|String|1|是否为默认地址|1，表示默认地址 0 为空不是默认地址

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
    "serviceCode": "save.user.address",
    "serviceName": "用户地址信息保存",
    "remark": "备注",
    "datas": {
      "businessUserAddress": {
        "userId": "123123",
        "addressId": "-1",
        "tel": "17797173943",
        "address": "青海省西宁市城中区129号",
        "postalCode": "810504",
        "isDefault": "0"
      }
    }
  }]
}
```

[>回到首页](home)
