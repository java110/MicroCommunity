package com.java110.common.cmd.corders;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.order.ICordersInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Java110Cmd(serviceCode = "corders.listUnitemLog")
public class ListUnitemLogCmd extends Cmd {

    @Autowired
    private ICordersInnerServiceSMO cordersInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "bId", "未包含业务ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Map info = new HashMap();
        info.put("bId", reqJson.getString("bId"));
        Map log = cordersInnerServiceSMOImpl.queryUnitemLog(info);

        if(log == null){
            throw new CmdException("数据不存在");
        }

        context.setResponseEntity(ResultVo.createResponseEntity(log));
    }
}
