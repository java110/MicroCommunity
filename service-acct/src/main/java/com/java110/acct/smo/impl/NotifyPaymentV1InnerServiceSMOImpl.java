/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.acct.smo.impl;


import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.acct.payment.IPaymentFactoryAdapt;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.log.LogSystemErrorDto;
import com.java110.dto.payment.NotifyPaymentOrderDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.paymentPool.PaymentPoolDto;
import com.java110.intf.acct.INotifyPaymentV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolV1InnerServiceSMO;
import com.java110.po.log.LogSystemErrorPo;
import com.java110.service.smo.ISaveSystemErrorSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.ExceptionUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2021-12-21 13:05:25 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class NotifyPaymentV1InnerServiceSMOImpl extends BaseServiceSMO implements INotifyPaymentV1InnerServiceSMO {

    private static final Logger logger = LoggerFactory.getLogger(NotifyPaymentV1InnerServiceSMOImpl.class);

    private static final String DEFAULT_PAYMENT_NOTIFY_ADAPT = "wechatPaymentFactory";// 默认微信通用支付

    protected static final String DEFAULT_NATIVE_QRCODE_PAYMENT_ADAPT = "wechatNativeQrcodePaymentFactory";// 默认微信通用支付
    @Autowired
    private IPaymentPoolV1InnerServiceSMO paymentPoolV1InnerServiceSMOImpl;
    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;
    /**
     * 通知类
     *
     * @param notifyPaymentOrderDto 数据对象分享
     * @return
     */
    @Override
    public ResponseEntity<String> notifyPayment(@RequestBody NotifyPaymentOrderDto notifyPaymentOrderDto) {

        try {
            String payNotifyAdapt = computeNotifyAdapt(notifyPaymentOrderDto);
            //MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAYMENT_ADAPT);
            payNotifyAdapt = StringUtil.isEmpty(payNotifyAdapt) ? DEFAULT_PAYMENT_NOTIFY_ADAPT : payNotifyAdapt;
//支付适配器IPayNotifyAdapt
            logger.debug("适配器：" + payNotifyAdapt);
            IPaymentFactoryAdapt tPayNotifyAdapt = ApplicationContextFactory.getBean(payNotifyAdapt, IPaymentFactoryAdapt.class);
            PaymentOrderDto paymentOrderDto = tPayNotifyAdapt.java110NotifyPayment(notifyPaymentOrderDto);
            logger.info("【支付回调响应】 响应内容：\n" + paymentOrderDto.getResponseEntity());

            if (StringUtil.isEmpty(paymentOrderDto.getOrderId())) {
                return paymentOrderDto.getResponseEntity();
            }

            String paramIn = CommonCache.getAndRemoveValue("unifiedPayment_" + paymentOrderDto.getOrderId());

            if (StringUtil.isEmpty(paramIn)) {
                throw new IllegalArgumentException("未找到业务数据");
            }

            JSONObject reqJson = JSONObject.parseObject(paramIn);

            IPaymentBusiness paymentBusiness = ApplicationContextFactory.getBean(reqJson.getString("business"), IPaymentBusiness.class);

            if (paymentBusiness == null) {
                throw new CmdException("当前支付业务不支持");
            }

            paymentOrderDto.setAppId(notifyPaymentOrderDto.getAppId());

            //2.0 相应业务 下单 返回 单号 ，金额，
            paymentBusiness.notifyPayment(paymentOrderDto, reqJson);


            return paymentOrderDto.getResponseEntity();
        } catch (Exception e) {
            logger.error("通知是配置异常", e);
            LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
            logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
            logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_PAY_NOTICE);
            logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
            saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);

            throw e;
        }
    }

    private String computeNotifyAdapt(NotifyPaymentOrderDto notifyPaymentOrderDto) {

        PaymentPoolDto paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setPpId(notifyPaymentOrderDto.getPaymentPoolId());
        paymentPoolDto.setCommunityId(notifyPaymentOrderDto.getCommunityId());
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);
        if (paymentPoolDtos == null || paymentPoolDtos.isEmpty()) {
            throw new IllegalArgumentException(notifyPaymentOrderDto.getPaymentPoolId() + "支付信息不存在");
        }
        return paymentPoolDtos.get(0).getBeanJsapi();
    }

    @Override
    public ResponseEntity<String> nativeNotifyPayment(@RequestBody NotifyPaymentOrderDto notifyPaymentOrderDto) {

        try {
            String payNotifyAdapt = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.NATIVE_QRCODE_PAYMENT_ADAPT);
            payNotifyAdapt = StringUtil.isEmpty(payNotifyAdapt) ? DEFAULT_NATIVE_QRCODE_PAYMENT_ADAPT : payNotifyAdapt;
            //支付适配器IPayNotifyAdapt
            logger.debug("适配器：" + payNotifyAdapt);
            IPaymentFactoryAdapt tPayNotifyAdapt = ApplicationContextFactory.getBean(payNotifyAdapt, IPaymentFactoryAdapt.class);
            PaymentOrderDto paymentOrderDto = tPayNotifyAdapt.java110NotifyPayment(notifyPaymentOrderDto);
            logger.info("【支付回调响应】 响应内容：\n" + paymentOrderDto.getResponseEntity());

            if (StringUtil.isEmpty(paymentOrderDto.getOrderId())) {
                return paymentOrderDto.getResponseEntity();
            }

            String paramIn = CommonCache.getAndRemoveValue("nativeQrcodePayment_" + paymentOrderDto.getOrderId());

            JSONObject reqJson = JSONObject.parseObject(paramIn);

            IPaymentBusiness paymentBusiness = ApplicationContextFactory.getBean(reqJson.getString("business"), IPaymentBusiness.class);

            if (paymentBusiness == null) {
                throw new CmdException("当前支付业务不支持");
            }

            paymentOrderDto.setAppId(notifyPaymentOrderDto.getAppId());

            //2.0 相应业务 下单 返回 单号 ，金额，
            paymentBusiness.notifyPayment(paymentOrderDto, reqJson);


            return paymentOrderDto.getResponseEntity();
        } catch (Exception e) {
            logger.error("通知是配置异常", e);
            throw e;
        }
    }
}
