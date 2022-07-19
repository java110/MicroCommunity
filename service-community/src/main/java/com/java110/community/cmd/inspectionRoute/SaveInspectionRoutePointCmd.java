package com.java110.community.cmd.inspectionRoute;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.IInspectionRoutePointRelV1InnerServiceSMO;
import com.java110.po.inspection.InspectionRoutePointRelPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "inspectionRoute.saveInspectionRoutePoint")
public class SaveInspectionRoutePointCmd extends Cmd {
    @Autowired
    private IInspectionRoutePointRelV1InnerServiceSMO inspectionRoutePointRelV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        if (reqJson.containsKey("inspectionId")) {
            Assert.hasKeyAndValue(reqJson, "inspectionId", "必填，请填写巡检点");
        } else {
            Assert.hasKeyAndValue(reqJson, "points", "必填，请填写多个巡检点");
        }
        Assert.hasKeyAndValue(reqJson, "inspectionRouteId", "必填，请填写巡检路线");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        if (reqJson.containsKey("inspectionId")) {
            //添加单元信息
            addInspectionRoute(reqJson);
        } else { //批量的情况
            JSONArray points = reqJson.getJSONArray("points");
            for (int pointIndex = 0; pointIndex < points.size(); pointIndex++) {
                reqJson.put("inspectionId", points.getJSONObject(pointIndex).getString("inspectionId"));
                reqJson.put("inspectionName", points.getJSONObject(pointIndex).getString("inspectionName"));
                addInspectionRoute(reqJson);
            }
        }
    }

    public void addInspectionRoute(JSONObject paramInJson) {

        paramInJson.put("irpRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_irpRelId));
        InspectionRoutePointRelPo inspectionRoutePointRelPo = BeanConvertUtil.covertBean(paramInJson, InspectionRoutePointRelPo.class);
        int flag = inspectionRoutePointRelV1InnerServiceSMOImpl.saveInspectionRoutePointRel(inspectionRoutePointRelPo);
        if (flag < 1) {
            throw new CmdException("删除巡检路线失败");
        }
    }
}
