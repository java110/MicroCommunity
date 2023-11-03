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
import com.java110.acct.payment.IRefundMoneyAdapt;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.payment.NotifyPaymentOrderDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.paymentPool.PaymentPoolDto;
import com.java110.dto.wechat.OnlinePayDto;
import com.java110.intf.acct.INotifyPaymentV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolV1InnerServiceSMO;
import com.java110.intf.acct.IReturnMoneyV1InnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
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
public class ReturnMoneyV1InnerServiceSMOImpl extends BaseServiceSMO implements IReturnMoneyV1InnerServiceSMO {

    private static final Logger logger = LoggerFactory.getLogger(ReturnMoneyV1InnerServiceSMOImpl.class);

    private static final String DEFAULT_PAYMENT_NOTIFY_ADAPT = "wechatPaymentFactory";// 默认微信通用支付

    protected static final String DEFAULT_NATIVE_QRCODE_PAYMENT_ADAPT = "wechatNativeQrcodePaymentFactory";// 默认微信通用支付
    @Autowired
    private IPaymentPoolV1InnerServiceSMO paymentPoolV1InnerServiceSMOImpl;


    @Override
    public ResultVo returnMoney(@RequestBody OnlinePayDto onlinePayDto) {


        PaymentPoolDto paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setPpId(onlinePayDto.getPaymentPoolId());
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);

        Assert.listOnlyOne(paymentPoolDtos, "支付厂家不存在");

        if (StringUtil.isEmpty(paymentPoolDtos.get(0).getBeanRefund())) {
            throw new IllegalArgumentException("厂家不支持退款");
        }

        IRefundMoneyAdapt refundMoneyAdapt = ApplicationContextFactory.getBean(paymentPoolDtos.get(0).getBeanRefund(), IRefundMoneyAdapt.class);
        if (refundMoneyAdapt == null) {
            throw new IllegalArgumentException("厂家退款适配器配置错误" + paymentPoolDtos.get(0).getBeanRefund());
        }

        ResultVo resultVo = null;
        try {
            resultVo = refundMoneyAdapt.refund(onlinePayDto, paymentPoolDtos.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            resultVo = new ResultVo(ResultVo.CODE_ERROR, e.getLocalizedMessage());
        }
        return resultVo;
    }

}
