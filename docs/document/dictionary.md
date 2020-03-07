### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2018-10-21|wuxw

### 本页内容
1、字典表spec概述

2、商户字典

## 1、字典表spec概述

模块名|domain|编码开头|举例
:-: | :-: | :-: | :-:
order|ORDERS|10|100001
business|BUSINESS|20|200001
user|USER|30|300001
user属性|USER_ATTR|31|310001
store|STORE|40|400001
store属性|STORE_ATTR|41|410001
shop|SHOP|50|500001
shop属性|SHOP_ATTR|51|510001
comment|COMMENT|60|600001
comment属性|COMMENT_ATTR|61|610001

## 商户字典（spec domain 为 STORE_ATTR）
domain|spec_cd|name|description
:-: | :-: | :-: | :-:
STORE_ATTR|410102100001|品牌名称|品牌名称
STORE_ATTR|410102100002|收款账户|收款账户
STORE_ATTR|410102100003|营业起始日|营业起始日
STORE_ATTR|410102100004|营业结束日|营业结束日
STORE_ATTR|410102100005|营业起始时段|营业起始时段
STORE_ATTR|410102100006|营业结束时段|营业结束时段
