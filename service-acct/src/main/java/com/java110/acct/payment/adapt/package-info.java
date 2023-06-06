/**
 * 支付厂家实现 适配器
 *
 *
 * 实现 IPaymentFactoryAdapt 接口
 *
 * 下单接口
 *
 * public Map java110Payment(PaymentOrderDto paymentOrderDto, JSONObject reqJson, ICmdDataFlowContext context) throws Exception
 * PaymentOrderDto 包含订单ID 支付金额 费用名称
 *
 * 返回内容 为 微信手机端 唤起支付框认识的 内容
 *
 * 支付完成通知接口
 *   public PaymentOrderDto java110NotifyPayment(String param)
 *   微信平台支付完成通知接口
 *
 *
 *   需要开发者账户 修改 PAYMENT_ADAPT 的值为适配器 beanName
 *
 */
package com.java110.acct.payment.adapt;