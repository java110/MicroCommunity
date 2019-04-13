### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2018-07-30|wuxw

### 本页内容

1. [保存评论信息](#保存评论信息)
2. [保存子评论信息](#保存子评论信息)
3. [保存子评论属性信息](#保存子评论属性信息)
4. [保存子评论照片信息](#保存子评论照片信息)
5. [保存评论分数信息](#保存评论分数信息)
6. [评论请求报文格式](#评论请求报文格式)
7. [删除评论信息](#删除评论信息)
8. [删除子评论信息](#删除子评论信息)
9. [删除评论请求报文格式](#删除评论请求报文格式)

### 评论协议

orders节点 和 business节点在中心服务（center）中已经介绍，这里不再介绍，查看请点[orders和business介绍](center)，这里我们介绍datas节点下内容

##### 保存评论信息
serviceCode 为 save.comment.info 保存评论

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 datas|comment|1|Object|-|评论节点信息|-
 comment|commentId|1|String|30|评论ID|新增时传-1
 comment|userId|1|String|30|用户ID|已有用户ID
 comment|commentTypeCd|1|String|2|评论类型|评论类型 S表示 商品 M表示 商户 T 物流
 comment|outId|1|String|30|外部ID|如商品ID 商户ID等

 详细报文[协议报文](#评论请求报文格式)

 ##### 保存子评论信息

 serviceCode 为 save.comment.info 保存评论

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|subComment|1|Object|-|评论节点信息|-
  subComment|subCommentId|1|String|30|子评论ID|新增时传-1
  subComment|commentId|1|String|30|子评论ID|新增时传-1
  subComment|parentSubCommentId|1|String|30|父 子评论ID|父 子评论ID 如果不是回复 写成-1
  subComment|subCommentTypeCd|1|String|2|评论类型|评论类型 C 评论 R 回复 A 追加
  subComment|commentContext|1|String|-|评论内容|评论内容

 详细报文[协议报文](#评论请求报文格式)

 ##### 保存子评论属性信息
  serviceCode 为 save.comment.info 保存评论

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|subCommentAttr|1|Object|-|评论节点信息|-
 subCommentAttr|subCommentId|1|String|30|子评论ID|评论新增的情况下传-1
 subCommentAttr|attrId|1|String|30|属性id|新增时传-1
 subCommentAttr|specCd|1|String|12|属性编码|评论服务提供
 subCommentAttr|value|1|String|50|属性值|-

 详细报文[协议报文](#评论请求报文格式)

 ##### 保存子评论照片信息
  serviceCode 为 save.comment.info 保存评论

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|subCommentPhoto|1|Object|-|评论照片节点信息|-
  subCommentPhoto|commentPhotoId|1|String|30|评论照片ID|评论照片ID
  subCommentPhoto|subCommentId|1|String|30|子评论ID|评论新增的情况下传-1
  subCommentPhoto|commentPhotoTypeCd|1|String|2|评论照片类型|评论照片类型,S 商品照片 M 商户ID
  subCommentPhoto|photo|1|String|100|照片|照片

  详细报文[协议报文](#评论请求报文格式)

  ##### 保存评论分数信息
   serviceCode 为 save.comment.info 保存评论分数

  父元素名称|参数名称|约束|类型|长度|描述|取值说明
   :-: | :-: | :-: | :-: | :-: | :-: | :-:
   datas|commentScore|1|Object|-|评论分数节点信息|-
   commentScore|commentScoreId|1|String|30|评论分数ID|评论分数ID
   commentScore|commentId|1|String|30|评论ID|评论新增的情况下传-1
   commentScore|scoreTypeCd|1|String|2|打分类别|S 商品相符，U 卖家打分，T 物流打分
   commentScore|value|1|int|-|分数|0-5分

   详细报文[协议报文](#评论请求报文格式)

 ##### 评论请求报文格式

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
     "serviceCode": "save.comment.info",
     "serviceName": "保存评论信息",
     "remark": "备注",
     "datas": {
       "comment": {
         "commentId": "-1",
         "userId": "123",
         "commentTypeCd":"S",
         "outId": "9898989898"
       },
       "subComment": {
         "subCommentId": "-1",
         "commentId":"-1",
         "parentSubCommentId":"-1",
         "subCommentTypeCd":"C",
         "commentContext":"非常好"
       },
       "subCommentAttr": [{
         "subCommentId": "-1",
         "attrId":"-1",
         "specCd":"1001",
         "value":"01"
       }],
       "subCommentPhoto":[{
         "commentPhotoId":"-1",
         "subCommentId":"-1",
         "commentPhotoTypeCd":"L",
         "photo":"123.jpg"
       }],
       "commentScore":[{
         "commentScoreId":"-1",
         "commentId":"-1",
         "scoreTypeCd":"S",
         "value":"5"
       }]
     },
     "attrs": [{
       "specCd": "配置的字段ID",
       "value": "具体值"
     }]
   }]
 }
 ```

 ##### 删除评论信息
 serviceCode 为 delete.comment.info 删除评论

 父元素名称|参数名称|约束|类型|长度|描述|取值说明
  :-: | :-: | :-: | :-: | :-: | :-: | :-:
  datas|comment|1|Object|-|评论节点信息|-
  comment|commentId|1|String|30|评论ID|已有评论ID

  ##### 删除子评论信息

  serviceCode 为 save.comment.info 删除子评论信息

  父元素名称|参数名称|约束|类型|长度|描述|取值说明
   :-: | :-: | :-: | :-: | :-: | :-: | :-:
   datas|subComment|1|Object|-|评论节点信息|-
   subComment|subCommentId|1|String|30|子评论ID|已有子评论ID


  详细报文[协议报文](#删除评论请求报文格式)

##### 删除评论请求报文格式
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
    "serviceCode": "delete.comment.info",
    "serviceName": "保存评论信息",
    "remark": "备注",
    "datas": {
      "comment": {
        "commentId": "123"
      },
      "subComment": { //如果有 comment 节点 这个节点不用写
        "subCommentId": "123456"
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
