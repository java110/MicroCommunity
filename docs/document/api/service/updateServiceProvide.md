

**1\. 编辑服务提供**
###### 接口功能
> 编辑服务提供接口

###### URL
> [http://api.java110.com:8008/api/serviceProvide.updateServiceProvide](http://api.java110.com:8008/api/serviceProvide.updateServiceProvide)

###### 支持格式
> JSON

###### HTTP请求方式
> POST

###### 请求参数(header部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-:|
|app_id|1|String|30|应用ID|Api服务分配                      |
|transaction_id|1|String|30|请求流水号|不能重复 1000000000+YYYYMMDDhhmmss+6位序列 |
|sign|1|String|-|签名|请参考签名说明|
|req_time|1|String|-|请求时间|YYYYMMDDhhmmss|

###### 请求参数(body部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|id|1|int|11|id|-|
|name|1|String|50|名称|-|
|params|1|String|500|参数|-|
|queryModel|1|String|1|查询方式| 1、sql,2、存储过程|
|serviceCode|1|String|50|-|对应c_service表|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，


###### 举例
> 地址：[http://api.java110.com:8008/api/serviceProvide.updateServiceProvide](http://api.java110.com:8008/api/serviceProvide.updateServiceProvide)

``` javascript
请求头信息：
Content-Type:application/json
USER_ID:1234
APP_ID:8000418002
TRANSACTION_ID:10029082726
REQ_TIME:20181113225612
SIGN:aabdncdhdbd878sbdudn898
请求报文：

{
	"template": "{\"PARAM\": {\"param1\": \"$.#staffFees#Array\",\"param2\":\"$.##Object\"},\"TEMPLATE\": {}}",
	"proc": "",
	"serviceCode": "api.getStaffFee",
	"javaScript": "",
	"name": "员工收取的费用1",
	"remark": "",
	"id": "44",
	"queryModel": "1",
	"params": "1",
	"sql": "{\"param1\":\"SELECT uu.`name` userName,uu.`user_id` userId,SUM(bpfd.`receivable_amount`) receivableAmount,SUM(bpfd.`received_amount`) receivedAmount\n FROM c_orders co,c_business cb,business_pay_fee_detail bpfd,u_user uu,s_store_user su\nWHERE co.`o_id` = cb.`o_id`\nAND cb.`b_id` = bpfd.`b_id`\nAND co.`user_id` = uu.`user_id`\nAND uu.`user_id` = su.`user_id`\n<if test=\\\"storeId !=null and storeId !=''\\\">\nand su.`store_id` = #storeId#\n</if>\n<if test=\\\"userCode !=null and userCode !=''\\\">\nand uu.`user_id` = #userCode#\n</if>\n<if test=\\\"startTime!=null \\\">\nAND co.`create_time` &gt;= #startTime#\n</if>\n<if test=\\\"endTime !=null \\\">\nAND co.`create_time` &lt;= #endTime#\n</if>\ngroup by uu.`name`,uu.`user_id`\nlimit #page#,#row#\",\n\"param2\":\"select count(1) total from (SELECT uu.`name`,uu.`user_id`\n FROM c_orders co,c_business cb,business_pay_fee_detail bpfd,u_user uu,s_store_user su\nWHERE co.`o_id` = cb.`o_id`\nAND cb.`b_id` = bpfd.`b_id`\nAND co.`user_id` = uu.`user_id`\nAND uu.`user_id` = su.`user_id`\n<if test=\\\"storeId !=null and storeId !=''\\\">\nand su.`store_id` = #storeId#\n</if>\n<if test=\\\"userCode !=null and userCode !=''\\\">\nand uu.`user_id` = #userCode#\n</if>\n<if test=\\\"startTime!=null \\\">\nAND co.`create_time` &gt;= #startTime#\n</if>\n<if test=\\\"endTime !=null \\\">\nAND co.`create_time` &lt;= #endTime#\n</if>\ngroup by uu.`name`,uu.`user_id`) t\"\n}"
}


返回报文：

```
