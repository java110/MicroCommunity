

**1\. 查询投诉建议**
###### 接口功能
> 用户通过web端或APP查询广告项接口

###### URL
> [,http://api.java110.com:8008/api/complaint.listComplaints](,http://api.java110.com:8008/api/complaint.listComplaints)

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

###### 请求参数(url部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|page|1|-|30|页数|-|
|row|1|-|30|每页显示行数|-|
|communityId|1|-|30|小区ID|-|
|complaintId|1|-|30|投诉ID|-|
|complaintId|1|-|30|投诉ID|-| 
|typeCd|1|-|30|投诉类型|-|
|complaintName|1|-|30|投诉人姓名|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，




###### 举例
> 地址：[http://api.java110.com:8008/api/complaint.listComplaints?storeTypeCd=800900000003&storeId=402019032924930007&userName=wuxw&userId=30518940136629616640&complaintId=111&typeCd=809002&complaintName=111&page=1&row=10&communityId=7020181217000001]()

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
	"complaints": [{
		"complaintId": "882019110351910002",
		"complaintName": "测试工作流",
		"context": "测试工作流",
		"roomId": "752019100965690010",
		"state": "10001",
		"stateName": "处理中",
		"storeId": "402019032924930007",
		"tel": "17797173945",
		"typeCd": "809001",
		"typeCdName": "投诉"
	}, {
		"complaintId": "882019110394400002",
		"complaintName": "测试工作流",
		"context": "测试工作流",
		"roomId": "752019100758260005",
		"state": "10001",
		"stateName": "处理中",
		"storeId": "402019032924930007",
		"tel": "18909874444",
		"typeCd": "809001",
		"typeCdName": "投诉"
	}, {
		"complaintId": "882019110312520003",
		"complaintName": "吴学文",
		"context": "测试工作流",
		"roomId": "752019102597030030",
		"state": "10001",
		"stateName": "处理中",
		"storeId": "402019032924930007",
		"tel": "18909782345",
		"typeCd": "809001",
		"typeCdName": "投诉"
	}, {
		"complaintId": "882019110301190003",
		"complaintName": "cxx",
		"context": "csdsdsds",
		"roomId": "752019100758260005",
		"state": "10001",
		"stateName": "处理中",
		"storeId": "402019032924930007",
		"tel": "15819214587",
		"typeCd": "809001",
		"typeCdName": "投诉"
	}, {
		"complaintId": "882019110399850010",
		"complaintName": "王艳艳",
		"context": "存储",
		"roomId": "752019100758260005",
		"state": "10001",
		"stateName": "处理中",
		"storeId": "402019032924930007",
		"tel": "18909783333",
		"typeCd": "809001",
		"typeCdName": "投诉"
	}, {
		"complaintId": "882019110459230001",
		"complaintName": "张三分",
		"context": "自动提交一个节点测试",
		"roomId": "752019100965690010",
		"state": "10001",
		"stateName": "处理中",
		"storeId": "402019032924930007",
		"tel": "18909786789",
		"typeCd": "809001",
		"typeCdName": "投诉"
	}, {
		"complaintId": "882019110403960002",
		"complaintName": "张三分",
		"context": "自动提交一个节点测试",
		"roomId": "752019100965690010",
		"state": "10001",
		"stateName": "处理中",
		"storeId": "402019032924930007",
		"tel": "18909786789",
		"typeCd": "809001",
		"typeCdName": "投诉"
	}, {
		"complaintId": "882019110471120001",
		"complaintName": "吴学文",
		"context": "测试回单内容",
		"roomId": "752019100758260005",
		"state": "10001",
		"stateName": "处理中",
		"storeId": "402019032924930007",
		"tel": "17797173567",
		"typeCd": "809001",
		"typeCdName": "投诉"
	}, {
		"complaintId": "882019110564340012",
		"complaintName": "七七",
		"context": "七七",
		"roomId": "752019100965690010",
		"state": "10001",
		"stateName": "处理中",
		"storeId": "402019032924930007",
		"tel": "13323654562",
		"typeCd": "809001",
		"typeCdName": "投诉"
	}, {
		"complaintId": "882019110572260013",
		"complaintName": "七七",
		"context": "七七",
		"roomId": "752019100965690010",
		"state": "10001",
		"stateName": "处理中",
		"storeId": "402019032924930007",
		"tel": "13323654562",
		"typeCd": "809001",
		"typeCdName": "投诉"
	}],
	"page": 0,
	"records": 4,
	"rows": 0,
	"total": 37
}

```
