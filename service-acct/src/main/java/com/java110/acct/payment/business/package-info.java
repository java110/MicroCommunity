/**
 * 支付模块
 *
 * 这个package 写 业务下单实现模块 代码
 * 主要实现
 * IPaymentBusiness 类
 *
 * PaymentOrderDto unified(JSONObject reqJson); 下单处理类 reqjson 前台传送的
 * paymentOrderDto 需要 下单后返回的 对象 主要 包含订单号orderId money 名称
 *
 *  void notify(PaymentOrderDto paymentOrderDto); 支付成功 支付厂家 回调后会通知改方法，接下来业务测可以确认订单
 *
 *
 * add by 吴学文 2022-10-04
 */
package com.java110.acct.payment.business;