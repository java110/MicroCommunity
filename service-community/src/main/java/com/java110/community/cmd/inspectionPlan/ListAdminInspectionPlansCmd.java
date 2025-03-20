package com.java110.community.cmd.inspectionPlan;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.inspection.InspectionPlanDto;
import com.java110.dto.inspection.InspectionPlanStaffDto;
import com.java110.dto.repair.RepairDto;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.community.IInspectionPlanStaffV1InnerServiceSMO;
import com.java110.intf.community.IInspectionPlanV1InnerServiceSMO;
import com.java110.intf.community.IInspectionRouteInnerServiceSMO;
import com.java110.intf.user.IOrgStaffRelInnerServiceSMO;
import com.java110.intf.user.IStaffCommunityV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "inspectionPlan.listAdminInspectionPlans")
public class ListAdminInspectionPlansCmd extends Cmd {

    @Autowired
    private IInspectionPlanV1InnerServiceSMO inspectionPlanV1InnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelInnerServiceSMO iOrgStaffRelInnerServiceSMO;

    @Autowired
    private IInspectionRouteInnerServiceSMO inspectionRouteInnerServiceSMOImpl;

    @Autowired
    private IInspectionPlanStaffV1InnerServiceSMO inspectionPlanStaffV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;


    @Autowired
    private IStaffCommunityV1InnerServiceSMO staffCommunityV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionPlanDto inspectionPlanDto = BeanConvertUtil.covertBean(reqJson, InspectionPlanDto.class);
        String staffId = CmdContextUtils.getUserId(context);

        List<String> communityIds = staffCommunityV1InnerServiceSMOImpl.queryStaffCommunityIds(staffId);

        if (!ListUtil.isNull(communityIds)) {
            inspectionPlanDto.setCommunityIds(communityIds.toArray(new String[communityIds.size()]));
        }
        int count = inspectionPlanV1InnerServiceSMOImpl.queryInspectionPlansCount(inspectionPlanDto);

        List<InspectionPlanDto> inspectionPlans = null;
        if (count > 0) {
            inspectionPlans = BeanConvertUtil.covertBeanList(inspectionPlanV1InnerServiceSMOImpl.queryInspectionPlans(inspectionPlanDto), InspectionPlanDto.class);
            queryStaffs(inspectionPlans);
        } else {
            inspectionPlans = new ArrayList<>();
        }
        refreshCommunityName(inspectionPlans);


        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, inspectionPlans);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    private void refreshCommunityName(List<InspectionPlanDto> inspectionPlans) {

        if(ListUtil.isNull(inspectionPlans)){
            return;
        }

        List<String> communityIds = new ArrayList<>();
        for (InspectionPlanDto inspectionPlanDto : inspectionPlans) {
            communityIds.add(inspectionPlanDto.getCommunityId());
        }

        if(ListUtil.isNull(communityIds)){
            return ;
        }
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityIds(communityIds.toArray(new String[communityIds.size()]));
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);
        if(ListUtil.isNull(communityDtos)){
            return;
        }
        for (InspectionPlanDto inspectionPlanDto : inspectionPlans) {
            for (CommunityDto tCommunityDto : communityDtos) {
                if (!inspectionPlanDto.getCommunityId().equals(tCommunityDto.getCommunityId())) {
                    continue;
                }
                inspectionPlanDto.setCommunityName(tCommunityDto.getName());
            }
        }
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
