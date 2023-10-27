package com.java110.acct.cmd.payment;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;

import java.text.ParseException;

/**
 * 查询native 支付信息
 */
@Java110Cmd(serviceCode = "payment.getNativeQrcodePayment")
public class GetNativeQrcodePaymentCmd extends Cmd {
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "qrToken", "未包含token信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String param = CommonCache.getAndRemoveValue("nativeQrcodePayment_" + reqJson.getString("qrToken"));


        if (StringUtil.isEmpty(param)) {
            throw new CmdException("支付码已经过期");
        }

        context.setResponseEntity(ResultVo.createResponseEntity(JSONObject.parseObject(param)));
    }
}
