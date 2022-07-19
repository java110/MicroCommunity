package com.java110.community.cmd.inspectionTaskDetail;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.IInspectionTaskDetailV1InnerServiceSMO;
import com.java110.po.inspection.InspectionTaskDetailPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "inspectionTaskDetail.saveInspectionTaskDetail")
public class SaveInspectionTaskDetailCmd extends Cmd {

    @Autowired
    private IInspectionTaskDetailV1InnerServiceSMO inspectionTaskDetailV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "taskDetailId", "请求报文中未包含taskDetailId");
        Assert.hasKeyAndValue(reqJson, "taskId", "请求报文中未包含taskId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "inspectionId", "请求报文中未包含inspectionId");
        Assert.hasKeyAndValue(reqJson, "inspectionName", "请求报文中未包含inspectionName");
        Assert.hasKeyAndValue(reqJson, "signType", "请求报文中未包含signType");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        reqJson.put("taskDetailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskDetailId));
        InspectionTaskDetailPo inspectionTaskPo = BeanConvertUtil.covertBean(reqJson, InspectionTaskDetailPo.class);
        int flag = inspectionTaskDetailV1InnerServiceSMOImpl.saveInspectionTaskDetail(inspectionTaskPo);
        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }
    }
}
