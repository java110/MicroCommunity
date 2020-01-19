

**1\. 查询服务提供**
###### 接口功能
> 查询服务提供接口

###### URL
> [http://api.java110.com:8008/api/serviceProvide.listServiceProvides](http://api.java110.com:8008/api/serviceProvide.listServiceProvides)

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
|page|-|-|30|页数|-|
|row|-|-|30|行数|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|id|1|int|11|id|-|
|javaScript|1|String|longtext|执行java脚本代码|-|
|name|1|String|50|名称|-|
|params|1|String|500|参数|-|
|proc|1|String|200|存储过程名称|-|
|queryModel|1|String|1|查询方式| 1、sql,2、存储过程|
|remark|1|String|200|描述|-|
|serviceCode|1|String|50|-|对应c_service表|
|sql|1|String|longtext|执行sql|-|
|template|1|String|longtext|输出模板|-|


		
###### 举例
> 地址：[http://api.java110.com:8008/api/serviceProvide.listServiceProvides?page=1&row=10](http://api.java110.com:8008/api/serviceProvide.listServiceProvides?page=1&row=10)

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
 	"page": 0,
 	"records": 5,
 	"rows": 0,
 	"serviceProvides": [{
 		"id": "44",
 		"javaScript": "",
 		"name": "员工收取的费用",
 		"params": "1",
 		"proc": "",
 		"queryModel": "1",
 		"remark": "",
 		"serviceCode": "api.getStaffFee",
 		"sql": "{\"param1\":\"SELECT uu.`name` userName,uu.`user_id` userId,SUM(bpfd.`receivable_amount`) receivableAmount,SUM(bpfd.`received_amount`) receivedAmount\n FROM c_orders co,c_business cb,business_pay_fee_detail bpfd,u_user uu,s_store_user su\nWHERE co.`o_id` = cb.`o_id`\nAND cb.`b_id` = bpfd.`b_id`\nAND co.`user_id` = uu.`user_id`\nAND uu.`user_id` = su.`user_id`\n<if test=\\\"storeId !=null and storeId !=''\\\">\nand su.`store_id` = #storeId#\n</if>\n<if test=\\\"userCode !=null and userCode !=''\\\">\nand uu.`user_id` = #userCode#\n</if>\n<if test=\\\"startTime!=null \\\">\nAND co.`create_time` &gt;= #startTime#\n</if>\n<if test=\\\"endTime !=null \\\">\nAND co.`create_time` &lt;= #endTime#\n</if>\ngroup by uu.`name`,uu.`user_id`\nlimit #page#,#row#\",\n\"param2\":\"select count(1) total from (SELECT uu.`name`,uu.`user_id`\n FROM c_orders co,c_business cb,business_pay_fee_detail bpfd,u_user uu,s_store_user su\nWHERE co.`o_id` = cb.`o_id`\nAND cb.`b_id` = bpfd.`b_id`\nAND co.`user_id` = uu.`user_id`\nAND uu.`user_id` = su.`user_id`\n<if test=\\\"storeId !=null and storeId !=''\\\">\nand su.`store_id` = #storeId#\n</if>\n<if test=\\\"userCode !=null and userCode !=''\\\">\nand uu.`user_id` = #userCode#\n</if>\n<if test=\\\"startTime!=null \\\">\nAND co.`create_time` &gt;= #startTime#\n</if>\n<if test=\\\"endTime !=null \\\">\nAND co.`create_time` &lt;= #endTime#\n</if>\ngroup by uu.`name`,uu.`user_id`) t\"\n}",
 		"template": "{\"PARAM\": {\"param1\": \"$.#staffFees#Array\",\"param2\":\"$.##Object\"},\"TEMPLATE\": {}}"
 	}, {
 		"id": "43",
 		"javaScript": "",
 		"name": "停车费缴费清单",
 		"params": "1",
 		"proc": "",
 		"queryModel": "1",
 		"remark": "",
 		"serviceCode": "api.getParkingSpacePayFee",
 		"sql": "{\"param1\":\"SELECT td.`name` feeTypeCdName,oo.`name` payObjName, pfd.`detail_id` detailId,pfd.`cycles`,\n pfd.`receivable_amount` receivableAmount,pfd.`received_amount` receivedAmount,uu.`name` userName,DATE_FORMAT(pfd.create_time,'%Y-%m-%d %H:%i:%s') createTime FROM pay_fee pf,pay_fee_detail pfd,t_dict td,s_store ss,\n  c_business cb,c_orders co,u_user uu,\n  building_owner oo,owner_car oc\nWHERE pf.`fee_id` = pfd.`fee_id`\nAND pf.`status_cd` = '0'\nAND pf.`fee_type_cd` = td.`status_cd`\nAND td.`table_name` = 'pay_fee'\nAND td.`table_columns` = 'fee_type_cd'\nAND pfd.`status_cd` = '0'\nAND pfd.`b_id` = cb.`b_id`\nAND cb.`o_id` = co.`o_id`\nAND co.`user_id` = uu.`user_id`\nAND pf.`income_obj_id` = ss.`store_id`\nAND ss.`status_cd` = '0'\nAND oo.`owner_id` = oc.`owner_id`\nAND oo.`status_cd` = '0'\nAND oc.`ps_id` = pf.`payer_obj_id`\nAND oc.`status_cd` = '0'\n<if test=\\\"storeId !=null and storeId !=''\\\">\nand ss.`store_id` = #storeId#\n</if>\n<if test=\\\"startTime!=null \\\">\nAND pfd.`create_time` &gt;= #startTime#\n</if>\n<if test=\\\"endTime !=null \\\">\nAND pfd.`create_time` &lt;= #endTime#\n</if>\n<if test=\\\"feeTypeCd !=null and feeTypeCd !=''\\\">\nAND pf.`fee_type_cd` = #feeTypeCd#\n</if>\norder by pfd.create_time desc\nlimit #page#,#row#\",\n\"param2\":\"SELECT count(1) total\n  FROM pay_fee pf,pay_fee_detail pfd,t_dict td,s_store ss,\n  c_business cb,c_orders co,u_user uu,\n  building_owner oo,owner_car oc\nWHERE pf.`fee_id` = pfd.`fee_id`\nAND pf.`status_cd` = '0'\nAND pf.`fee_type_cd` = td.`status_cd`\nAND td.`table_name` = 'pay_fee'\nAND td.`table_columns` = 'fee_type_cd'\nAND pfd.`status_cd` = '0'\nAND pfd.`b_id` = cb.`b_id`\nAND cb.`o_id` = co.`o_id`\nAND co.`user_id` = uu.`user_id`\nAND pf.`income_obj_id` = ss.`store_id`\nAND ss.`status_cd` = '0'\nAND oo.`owner_id` = oc.`owner_id`\nAND oo.`status_cd` = '0'\nAND oc.`ps_id` = pf.`payer_obj_id`\nAND oc.`status_cd` = '0'\n<if test=\\\"storeId !=null and storeId !=''\\\">\nand ss.`store_id` = #storeId#\n</if>\n<if test=\\\"startTime!=null \\\">\nAND pfd.`create_time` &gt;= #startTime#\n</if>\n<if test=\\\"endTime !=null \\\">\nAND pfd.`create_time` &lt;= #endTime#\n</if>\n<if test=\\\"feeTypeCd !=null and feeTypeCd !=''\\\">\nAND pf.`fee_type_cd` = #feeTypeCd#\n</if>\"\n}",
 		"template": "{\"PARAM\": {\"param1\": \"$.#payFees#Array\",\"param2\":\"$.##Object\"},\"TEMPLATE\": {}}"
 	}],
 	"total": 42
 }

```
