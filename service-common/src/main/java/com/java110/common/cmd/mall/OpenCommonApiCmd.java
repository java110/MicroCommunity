package com.java110.common.cmd.mall;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;

import java.util.List;

/**
 * 专属于HC小区管理系统调用
 */
@Java110Cmd(serviceCode = "mall.openCommonApi")
public class OpenCommonApiCmd extends Cmd {

    private IMallCommonApiBmo mallCommonApiBmoImpl;



    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "mallApiCode", "未包含MALL接口编码");


        mallCommonApiBmoImpl = ApplicationContextFactory.getBean(reqJson.getString("mallApiCode"), IMallCommonApiBmo.class);
        if (mallCommonApiBmoImpl == null) {
            throw new CmdException("未实现该能力");
        }

        mallCommonApiBmoImpl.validate(context, reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        mallCommonApiBmoImpl = ApplicationContextFactory.getBean(reqJson.getString("iotApiCode"), IMallCommonApiBmo.class);
        if (mallCommonApiBmoImpl == null) {
            throw new CmdException("未实现该能力");
        }
        mallCommonApiBmoImpl.doCmd(context, reqJson);
    }
}
