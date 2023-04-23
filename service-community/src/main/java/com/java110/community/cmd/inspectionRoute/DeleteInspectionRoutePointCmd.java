package com.java110.community.cmd.inspectionRoute;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.inspection.InspectionRoutePointRelDto;
import com.java110.intf.community.IInspectionRoutePointRelInnerServiceSMO;
import com.java110.intf.community.IInspectionRoutePointRelV1InnerServiceSMO;
import com.java110.po.inspection.InspectionRoutePointRelPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "inspectionRoute.deleteInspectionRoutePoint")
public class DeleteInspectionRoutePointCmd extends Cmd {

    @Autowired
    private IInspectionRoutePointRelInnerServiceSMO inspectionRoutePointRelInnerServiceSMOImpl;

    @Autowired
    private IInspectionRoutePointRelV1InnerServiceSMO inspectionRoutePointRelV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "inspectionRouteId", "路线巡检路线不能为空");
        Assert.hasKeyAndValue(reqJson, "inspectionId", "路线巡检点不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionRoutePointRelDto inspectionRoutePointRelDto = new InspectionRoutePointRelDto();
        inspectionRoutePointRelDto.setCommunityId(reqJson.getString("communityId"));
        inspectionRoutePointRelDto.setInspectionId(reqJson.getString("inspectionId"));
        inspectionRoutePointRelDto.setInspectionRouteId(reqJson.getString("inspectionRouteId"));
        List<InspectionRoutePointRelDto> inspectionRoutePointRelDtos = inspectionRoutePointRelInnerServiceSMOImpl.queryInspectionRoutePointRels(inspectionRoutePointRelDto);

        Assert.listOnlyOne(inspectionRoutePointRelDtos, "未查询到（或多条）要删除的 巡检路线下的巡检点");
        JSONObject businessInspectionRoute = new JSONObject();
        businessInspectionRoute.putAll(BeanConvertUtil.beanCovertMap(inspectionRoutePointRelDtos.get(0)));

        InspectionRoutePointRelPo inspectionRoutePointRelPo = BeanConvertUtil.covertBean(businessInspectionRoute, InspectionRoutePointRelPo.class);

        int flag = inspectionRoutePointRelV1InnerServiceSMOImpl.deleteInspectionRoutePointRel(inspectionRoutePointRelPo);
        if (flag < 1) {
            throw new CmdException("删除巡检路线失败");
        }
    }
}
