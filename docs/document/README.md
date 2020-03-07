
### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2018-07-24|wuxw


### 本页内容

1. [中心服务sql](#中心服务sql)
2. [用户服务sql](#用户服务sql)
3. [商品服务sql](#商品服务sql)
4. [商户服务sql](#商户服务sql)
5. [评论服务sql](#评论服务sql)
6. [日志相关sql](#日志相关sql)
7. [配置说明](#配置说明)
8. [二次开发](#二次开发)

### 简介
  微小区服务文档由java110团队编写，主要包括：安装教程，接口协议，配置说明和二次开发等

### 安装文档

  包括中间件部署，服务打包，数据配置，服务部署

  &nbsp;&nbsp;[安装部署](install)

### 维护sql

  ##### 中心服务sql

  1. 订单相关sql：
  ```
    -- 订单表
    select a.* from c_orders a ;
    -- 订单属性表
    select a.* from c_orders_attrs a;
    -- 业务表
    select a.* from c_business a;
    -- 业务属性表
    select a.* from c_business_attrs a;
    -- 订单类型表
    select a.* from c_order_type a;
    -- 业务类型表
    select a.* from c_business_type a;
  ```
  2. 服务配置相关sql：
  ```
  -- 外部应用表
  select a.* from c_app a ;
  -- 服务提供表
  select a.* from c_service a;
  -- 外部应用 和 服务关联表
  select a.* from c_route a;
  -- 查询类 服务实现表
  select a.* from c_service_sql a;
  ```

  3. 公共配置sql：
  ```
    -- 数据映射表
    select a.* from c_mapping a;
    -- 规格表
    select a.* from spec a;
    -- 状态表
    select a.* from c_status a;
  ```


  ##### 用户服务sql

  ```
    -- 用户Business表
    select a.* from business_user a;
    -- 用户Business 属性表
    select a.* from business_user_attr a;
    -- 用户Business物流地址
    select a.* from business_user_address a;
    -- 用户Business标签
    select a.* from business_user_tag a;
    -- 用户Business证件 表
    select a.* from business_user_credentials a;
    -- 用户表
    select a.* from u_user a;
    -- 用户属性表
    select a.* from u_user_attr a;
    -- 用户物流地址
    select a.* from u_user_address a;
    -- 用户标签
    select a.* from u_user_tag a;
    -- 用户证件表
    select a.* from u_user_credentials a;
    -- 用户等级
    select a.* from user_level a;
    -- 用户位置
    select a.* from u_location a;
    -- 用户tag字典表
    select a.* from tag a;
    -- 用户标签字典表
    select a.* from credentials a;
  ```

  #####  商品服务sql

  ```
    -- 商品 Business 表
    select a.* from business_shop a;
    -- 商品属性 Business 表
    select a.* from business_shop_attr a;
    -- 商品目录 Business 表
    select a.* from business_shop_catalog a;
    -- 商店照片 Business表
    select a.* from business_shop_photo a ;
    -- 商品属性 离散值 Business表
    select a.* from business_shop_attr_param a;
    -- 商品优惠 Business表
    select a.* from business_shop_preferential a ;
    -- 商品描述 Business表
    select a.* from business_shop_desc a ;
    -- 商品表
    select a.* from s_shop a;
    -- 商品属性表
    select a.* from s_shop_attr a;
    -- 商品属性 离散值表，例如 手机颜色 黑 白 红
    select a.* from s_shop_attr_param a;
    -- 商品优惠表
    select a.* from s_shop_preferential a;
    -- 商品描述
    select a.* from s_shop_desc a;
    -- 商品照片
    select a.* from s_shop_photo a;
    -- 商品目录
    select a.* from s_shop_catalog a ;
    -- 商品购买记录
    select a.* from s_buy_shop a ;
    -- 商品购买属性
    select a.* from s_buy_shop_attr a;
  ```

  ##### 商户服务sql
  ```
    -- 商户 Business 表
    select a.* from business_store a;
    -- 商户 属性 Business 表
    select a.* from business_store_attr a;
    -- 商户照片 Business表
    select a.* from business_store_photo a ;
    -- 商户证件 Business表
    select a.* from business_store_cerdentials a ;
    -- 商户表
    select a.* from s_store a ;
    -- 商户属性表
    select a.* from s_store_attr a ;
    -- 商户照片表
    select a.* from s_store_photo a ;
    -- 商户证件表
    select a.* from s_store_cerdentials a ;
    -- 商户种类
    select a.* from store_type a ;
  ```

  ##### 评论服务sql

  ```
    -- 评论表
    select  a.* from c_comment a;
    -- 评论子表
    select a.* from c_sub_comment a;
    -- 评论属性表
    select a.* from c_sub_comment_attr a ;
    -- 评论照片表
    select  a.* from c_sub_comment_photo a;
    -- 评论分数表
    select a.* from c_comment_score a ;
  ```

  ##### 日志相关sql

  ```
  -- 交互日志记录表
  SELECT * FROM l_transaction_log a ;
  -- 交互日志报文记录表
  SELECT * FROM l_transaction_log_message a ;
  ```


### 配置说明

   主要说明 中心服务配置

   &nbsp;&nbsp;&nbsp;&nbsp;[配置说明](center_config)

### 二次开发

  包括新建服务，新增配置，测试方法

  &nbsp;&nbsp;&nbsp;&nbsp;[二次开发](develop)
