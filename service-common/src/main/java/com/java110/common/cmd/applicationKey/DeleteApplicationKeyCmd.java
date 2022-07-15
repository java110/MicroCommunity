package com.java110.common.cmd.applicationKey;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.common.IApplicationKeyV1InnerServiceSMO;
import com.java110.po.applicationKey.ApplicationKeyPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "applicationKey.deleteApplicationKey")
public class DeleteApplicationKeyCmd extends Cmd{

    @Autowired
    private IApplicationKeyV1InnerServiceSMO applicationKeyV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");

        Assert.hasKeyAndValue(reqJson, "applicationKeyId", "钥匙申请ID不能为空");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        ApplicationKeyPo applicationKeyPo = BeanConvertUtil.covertBean(reqJson, ApplicationKeyPo.class);
        int flag = applicationKeyV1InnerServiceSMOImpl.deleteApplicationKey(applicationKeyPo);

        if (flag < 1) {
            throw new CmdException("保存开门记录失败");
        }
    }
}
