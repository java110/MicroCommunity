package com.java110.community.cmd.inspectionPoint;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.inspection.InspectionRoutePointRelDto;
import com.java110.intf.community.IInspectionPointV1InnerServiceSMO;
import com.java110.intf.community.IInspectionRoutePointRelInnerServiceSMO;
import com.java110.po.inspection.InspectionPointPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "inspectionPoint.deleteInspectionPoint")
public class DeleteInspectionPointCmd extends Cmd {

    @Autowired
    private IInspectionRoutePointRelInnerServiceSMO inspectionRoutePointRelInnerServiceSMOImpl;

    @Autowired
    private IInspectionPointV1InnerServiceSMO inspectionPointV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "inspectionId", "巡检点ID不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionPointPo inspectionPointPo = BeanConvertUtil.covertBean(reqJson, InspectionPointPo.class);
        InspectionRoutePointRelDto inspectionRoutePointRelDto = new InspectionRoutePointRelDto();
        inspectionRoutePointRelDto.setInspectionId(inspectionPointPo.getInspectionId());
        //查询巡检点巡检路线关系表
        List<InspectionRoutePointRelDto> inspectionRoutePointRelDtos = inspectionRoutePointRelInnerServiceSMOImpl.queryInspectionRoutePointRels(inspectionRoutePointRelDto);
        Assert.listIsNull(inspectionRoutePointRelDtos, "该巡检点正在使用，不能删除！");

        int flag = inspectionPointV1InnerServiceSMOImpl.deleteInspectionPoint(inspectionPointPo);
        if (flag < 1) {
            throw new CmdException("删除巡检点失败");
        }
    }
}
