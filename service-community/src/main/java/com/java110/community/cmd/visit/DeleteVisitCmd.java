package com.java110.community.cmd.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.community.IVisitV1InnerServiceSMO;
import com.java110.po.owner.VisitPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;


@Java110Cmd(serviceCode = "visit.deleteVisit")
public class DeleteVisitCmd extends Cmd {

    @Autowired
    private IVisitV1InnerServiceSMO visitV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "vId", "访客记录ID不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        VisitPo visitPo = BeanConvertUtil.covertBean(reqJson, VisitPo.class);
        visitPo.setStatusCd("1");

        int flag = visitV1InnerServiceSMOImpl.deleteVisit(visitPo);

        if(flag < 1){
            throw new CmdException("删除异常");
        }
    }
}
