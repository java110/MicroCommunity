package com.java110.community.cmd.inspectionPoint;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.community.IInspectionPointV1InnerServiceSMO;
import com.java110.po.inspection.InspectionPointPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "inspectionPoint.updateInspectionPoint")
public class UpdateInspectionPointCmd extends Cmd {
    @Autowired
    private IInspectionPointV1InnerServiceSMO inspectionPointV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "inspectionId", "巡检点ID不能为空");
        Assert.hasKeyAndValue(reqJson, "inspectionName", "必填，请填写巡检点名称");
        Assert.hasKeyAndValue(reqJson, "pointObjId", "必填，请填写位置信息");
        Assert.hasKeyAndValue(reqJson, "pointObjType", "必填，请填写巡检类型");
        Assert.hasKeyAndValue(reqJson, "pointObjName", "必填，请填写位置信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        InspectionPointPo inspectionPointPo = BeanConvertUtil.covertBean(reqJson, InspectionPointPo.class);
        int flag = inspectionPointV1InnerServiceSMOImpl.updateInspectionPoint(inspectionPointPo);
        if (flag < 1) {
            throw new CmdException("保存巡检点失败");
        }
    }
}
