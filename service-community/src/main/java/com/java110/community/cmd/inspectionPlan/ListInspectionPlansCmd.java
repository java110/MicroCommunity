package com.java110.community.cmd.inspectionPlan;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.inspection.InspectionPlanDto;
import com.java110.dto.inspection.InspectionRouteDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.intf.community.IInspectionPlanV1InnerServiceSMO;
import com.java110.intf.community.IInspectionRouteInnerServiceSMO;
import com.java110.intf.user.IOrgStaffRelInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.inspectionPlan.ApiInspectionPlanDataVo;
import com.java110.vo.api.inspectionPlan.ApiInspectionPlanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Java110Cmd(serviceCode = "inspectionPlan.listInspectionPlans")
public class ListInspectionPlansCmd extends Cmd {

    @Autowired
    private IInspectionPlanV1InnerServiceSMO inspectionPlanV1InnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelInnerServiceSMO iOrgStaffRelInnerServiceSMO;

    @Autowired
    private IInspectionRouteInnerServiceSMO inspectionRouteInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionPlanDto inspectionPlanDto = BeanConvertUtil.covertBean(reqJson, InspectionPlanDto.class);

        int count = inspectionPlanV1InnerServiceSMOImpl.queryInspectionPlansCount(inspectionPlanDto);

        List<ApiInspectionPlanDataVo> inspectionPlans = null;

        if (count > 0) {
            inspectionPlans = BeanConvertUtil.covertBeanList(inspectionPlanV1InnerServiceSMOImpl.queryInspectionPlans(inspectionPlanDto), ApiInspectionPlanDataVo.class);
        } else {
            inspectionPlans = new ArrayList<>();
        }

        ApiInspectionPlanVo apiInspectionPlanVo = new ApiInspectionPlanVo();

        apiInspectionPlanVo.setTotal(count);
        apiInspectionPlanVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiInspectionPlanVo.setInspectionPlans(inspectionPlans);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiInspectionPlanVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
