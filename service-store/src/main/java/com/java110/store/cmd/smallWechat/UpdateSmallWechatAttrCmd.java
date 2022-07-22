package com.java110.store.cmd.smallWechat;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.store.ISmallWechatAttrV1InnerServiceSMO;
import com.java110.po.smallWechatAttr.SmallWechatAttrPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "smallWechat.updateSmallWechatAttr")
public class UpdateSmallWechatAttrCmd extends Cmd {

    @Autowired
    private ISmallWechatAttrV1InnerServiceSMO smallWechatAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "attrId", "attrId不能为空");
        Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "value", "请求报文中未包含value");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        SmallWechatAttrPo smallWechatAttrPo = BeanConvertUtil.covertBean(reqJson, SmallWechatAttrPo.class);
        int flag = smallWechatAttrV1InnerServiceSMOImpl.updateSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        context.setResponseEntity(ResultVo.success());
    }
}
