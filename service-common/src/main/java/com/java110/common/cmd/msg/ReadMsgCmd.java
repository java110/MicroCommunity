package com.java110.common.cmd.msg;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IMsgReadV1InnerServiceSMO;
import com.java110.po.message.MsgReadPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

@Java110Cmd(serviceCode = "msg.readMsg")
public class ReadMsgCmd extends Cmd {

    @Autowired
    private IMsgReadV1InnerServiceSMO msgReadV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "userName", "必填，请填写员工名称");
        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写员工ID");
        Assert.hasKeyAndValue(reqJson, "msgId", "必填，请填写消息ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        JSONObject businessMsgRead = new JSONObject();
        businessMsgRead.put("msgReadId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_msgReadId));
        businessMsgRead.putAll(reqJson);
        MsgReadPo msgReadPo = BeanConvertUtil.covertBean(businessMsgRead, MsgReadPo.class);

        int flag = msgReadV1InnerServiceSMOImpl.saveMsgRead(msgReadPo);

        if (flag < 1) {
            throw new CmdException("已读消息失败");
        }
    }
}
