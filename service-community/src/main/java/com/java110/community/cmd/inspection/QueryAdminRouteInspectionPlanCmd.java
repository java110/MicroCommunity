package com.java110.community.cmd.inspection;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.inspection.InspectionPlanDto;
import com.java110.dto.inspection.InspectionPlanStaffDto;
import com.java110.intf.community.IInspectionPlanStaffV1InnerServiceSMO;
import com.java110.intf.community.IInspectionPlanV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据巡检点查询巡检计划
 */
@Java110Cmd(serviceCode = "inspection.queryAdminRouteInspectionPlan")
public class QueryAdminRouteInspectionPlanCmd extends Cmd {

    @Autowired
    private IInspectionPlanV1InnerServiceSMO inspectionPlanV1InnerServiceSMOImpl;

    @Autowired
    private IInspectionPlanStaffV1InnerServiceSMO inspectionPlanStaffV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
        super.validateAdmin(context);
        Assert.hasKeyAndValue(reqJson, "inspectionRouteId", "未包含巡检路线");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        InspectionPlanDto inspectionPlanDto = BeanConvertUtil.covertBean(reqJson, InspectionPlanDto.class);

        int count = inspectionPlanV1InnerServiceSMOImpl.queryRouteInspectionPlansCount(inspectionPlanDto);

        List<InspectionPlanDto> inspectionPlans = null;

        if (count > 0) {
            inspectionPlans = BeanConvertUtil.covertBeanList(inspectionPlanV1InnerServiceSMOImpl.queryRouteInspectionPlans(inspectionPlanDto), InspectionPlanDto.class);
            queryStaffs(inspectionPlans);
        } else {
            inspectionPlans = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, inspectionPlans);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 查询员工
     *
     * @param inspectionPlans
     */
    private void queryStaffs(List<InspectionPlanDto> inspectionPlans) {

        if (ListUtil.isNull(inspectionPlans)) {
            return;
        }

        List<String> planIds = new ArrayList<>();

        for (InspectionPlanDto inspectionPlanDto : inspectionPlans) {
            planIds.add(inspectionPlanDto.getInspectionPlanId());
        }

        InspectionPlanStaffDto inspectionPlanStaffDto = new InspectionPlanStaffDto();
        inspectionPlanStaffDto.setInspectionPlanIds(planIds.toArray(new String[planIds.size()]));
        inspectionPlanStaffDto.setCommunityId(inspectionPlans.get(0).getCommunityId());
        List<InspectionPlanStaffDto> inspectionPlanStaffDtos = inspectionPlanStaffV1InnerServiceSMOImpl.queryInspectionPlanStaffs(inspectionPlanStaffDto);

        if (ListUtil.isNull(inspectionPlanStaffDtos)) {
            return;
        }

        List<InspectionPlanStaffDto> staffDtos = null;
        for (InspectionPlanDto inspectionPlanDto : inspectionPlans) {
            staffDtos = new ArrayList<>();
            for (InspectionPlanStaffDto tmpInspectionPlanStaffDto : inspectionPlanStaffDtos) {
                if (inspectionPlanDto.getInspectionPlanId().equals(tmpInspectionPlanStaffDto.getInspectionPlanId())) {
                    staffDtos.add(tmpInspectionPlanStaffDto);
                }
            }
            inspectionPlanDto.setStaffs(staffDtos);
        }

    }
}
