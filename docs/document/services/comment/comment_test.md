### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2018-07-30|wuxw

### 测试说明
测试人员|测试时间|测试结果|联系邮箱
:-:|:-:|:-:|:-:|
wuxw|2018-7-17|通过|928255095@qq.com

### 本页内容

1. [保存评论测试](#保存评论测试)
2. [删除评论测试](#删除评论测试)

### 保存评论测试

##### 测试地址：
http://135.192.86.200:8001/httpApi/service

测试时135.192.86.200替换成自己的ip

##### 请求报文：

```
{
	"orders": {
		"appId": "8000418001",
		"transactionId": "100000000020180717224736000013",
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
		"serviceCode": "save.comment.info",
		"serviceName": "保存评论信息",
		"remark": "备注",
		"datas": {
			"comment": {
				"commentId": "-1",
				"userId": "123",
				"commentTypeCd": "S",
				"outId": "9898989898"
			},
			"subComment": {
				"subCommentId": "-1",
				"commentId": "-1",
				"parentSubCommentId": "-1",
				"subCommentTypeCd": "C",
				"commentContext": "非常好"
			},
			"subCommentAttr": [{
				"subCommentId": "-1",
				"attrId": "-1",
				"specCd": "1001",
				"value": "01"
			}],
			"subCommentPhoto": [{
				"commentPhotoId": "-1",
				"subCommentId": "-1",
				"commentPhotoTypeCd": "L",
				"photo": "123.jpg"
			}],
			"commentScore": [{
				"commentScoreId": "-1",
				"commentId": "-1",
				"scoreTypeCd": "S",
				"value": "5"
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
		"serviceCode": "save.comment.info",
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
		"transactionId": "100000000020180717224736000013"
	}
}
```

### 删除评论测试

##### 测试地址：
http://135.192.86.200:8001/httpApi/service

测试时135.192.86.200替换成自己的ip

##### 请求报文：

```
{
	"orders": {
		"appId": "8000418001",
		"transactionId": "100000000020180708224736000022",
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
		"serviceCode": "delete.comment.info",
		"serviceName": "保存评论信息",
		"remark": "备注",
		"datas": {
			"comment": {
				"commentId": "60468918071306756096"
			},
			"subComment": {
				"subCommentId": "61468918071457751040"
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
		"serviceCode": "delete.comment.info",
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
		"transactionId": "100000000020180708224736000022"
	}
}
```

[>回到首页](home)
