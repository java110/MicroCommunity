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
            List<Object> staffIds = new ArrayList<Object>();
            List<Object> inspectionRouteIds = new ArrayList<Object>();
            for (ApiInspectionPlanDataVo Plans : inspectionPlans) {
                staffIds.add(Plans.getStaffId());
                String[] ids = Plans.getInspectionRouteId().split(",");
                for (String s : ids) {
                    inspectionRouteIds.add(s);
                }
            }
            if (staffIds.size() > 0) {
                OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
                String[] staffIdsArray = new String[staffIds.size()];
                staffIds.toArray(staffIdsArray);
                orgStaffRelDto.setStaffIds(staffIdsArray);
                List<OrgStaffRelDto> orgStaffRelDtos = iOrgStaffRelInnerServiceSMO.queryOrgInfoByStaffIds(orgStaffRelDto);
                for (ApiInspectionPlanDataVo planDataVo : inspectionPlans) {
                    for (OrgStaffRelDto orgs : orgStaffRelDtos) {
                        if (planDataVo.getStaffId().equals(orgs.getStaffId())) {
                            planDataVo.setDepartmentId(orgs.getDepartmentId());
                            planDataVo.setDepartmentName(orgs.getDepartmentName());
                            planDataVo.setCompanyId(orgs.getCompanyId());
                            planDataVo.setCompanyName(orgs.getCompanyName());
                        }
                    }
                }
            }
            if (inspectionRouteIds.size() > 0) {
                //去重
                HashSet set = new HashSet(inspectionRouteIds);
                inspectionRouteIds.clear();
                inspectionRouteIds.addAll(set);
                InspectionRouteDto inspectionRouteDto = new InspectionRouteDto();
                String[] routeIds = (String[]) inspectionRouteIds.toArray(new String[inspectionRouteIds.size()]);
                inspectionRouteDto.setInspectionRouteIds(routeIds);
                List<InspectionRouteDto> inspectionRouteDtoList = inspectionRouteInnerServiceSMOImpl.queryInspectionRoutes(inspectionRouteDto);
                for (ApiInspectionPlanDataVo planDataVo : inspectionPlans) {
                    String[] routeIdArray = planDataVo.getInspectionRouteId().split(",");
                    for (String s : routeIdArray) {
                        for (InspectionRouteDto inspectionRouteDto1 : inspectionRouteDtoList) {
                            if (inspectionRouteDto1.getInspectionRouteId().equals(s)) {
                                if (planDataVo.getInspectionRouteName() == null) {
                                    planDataVo.setInspectionRouteName(inspectionRouteDto1.getRouteName());
                                } else {
                                    planDataVo.setInspectionRouteName(planDataVo.getInspectionRouteName() + "," + inspectionRouteDto1.getRouteName());
                                }
                            }
                        }

                    }
                }
            }
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
