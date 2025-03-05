package com.java110.report.cmd.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.inspection.InspectionPlanDto;
import com.java110.intf.dev.IDictV1InnerServiceSMO;
import com.java110.intf.report.IReportCommunityInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 查询小区巡检树形结构
 */
@Java110Cmd(serviceCode = "community.queryCommunityInspectionTree")
public class QueryCommunityInspectionTreeCmd extends Cmd {

    @Autowired
    private IReportCommunityInnerServiceSMO reportCommunityInnerServiceSMOImpl;

    @Autowired
    private IDictV1InnerServiceSMO dictV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        // must be administrator
        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        List<InspectionPlanDto> inspectionPlanDtos = null;

        InspectionPlanDto inspectionPlanDto = new InspectionPlanDto();


        inspectionPlanDtos = reportCommunityInnerServiceSMOImpl.queryCommunityInspectionTree(inspectionPlanDto);
        JSONArray communitys = new JSONArray();
        if (ListUtil.isNull(inspectionPlanDtos)) {
            context.setResponseEntity(ResultVo.createResponseEntity(communitys));
            return;
        }

        JSONObject communityInfo = null;
        for (InspectionPlanDto tmpInspectionPlanDto : inspectionPlanDtos) {
            if (!hasInCommunity(tmpInspectionPlanDto, communitys)) {
                communityInfo = new JSONObject();
                communityInfo.put("id", "c_" + tmpInspectionPlanDto.getCommunityId());
                communityInfo.put("communityId", tmpInspectionPlanDto.getCommunityId());
                communityInfo.put("text", tmpInspectionPlanDto.getCommunityName());
                communityInfo.put("icon", "/img/org.png");
                communityInfo.put("children", new JSONArray());
                communitys.add(communityInfo);
            }
        }


        JSONObject community = null;
        for (int cIndex = 0; cIndex < communitys.size(); cIndex++) {
            community = communitys.getJSONObject(cIndex);
            // find floor data in unitDtos
            findInspectionPlan(community, inspectionPlanDtos);
        }
        context.setResponseEntity(ResultVo.createResponseEntity(communitys));


    }

    /**
     * find community floor data
     *
     * @param community       current community
     * @param inspectionPlanDtos all units data
     */
    private void findInspectionPlan(JSONObject community, List<InspectionPlanDto> inspectionPlanDtos) {
        JSONArray plans = community.getJSONArray("children");
        JSONObject parkingInfo = null;
        for (InspectionPlanDto tmpInspectionPlanDto : inspectionPlanDtos) {
            if(!community.getString("communityId").equals(tmpInspectionPlanDto.getCommunityId())){
                continue;
            }
            if (!hasInInspectionPlan(tmpInspectionPlanDto, plans)) {
                parkingInfo = new JSONObject();
                parkingInfo.put("id", "p_" + tmpInspectionPlanDto.getInspectionPlanId());
                parkingInfo.put("inspectionPlanId", tmpInspectionPlanDto.getInspectionPlanId());
                parkingInfo.put("communityId", community.getString("communityId"));
                parkingInfo.put("text", tmpInspectionPlanDto.getInspectionPlanName());
                parkingInfo.put("icon", "/img/floor.png");
                parkingInfo.put("children", new JSONArray());
                plans.add(parkingInfo);
            }
        }


        JSONObject plan = null;
        for (int cIndex = 0; cIndex < plans.size(); cIndex++) {
            plan = plans.getJSONObject(cIndex);
            // find floor data in unitDtos
            findInspectionStaff(plan, inspectionPlanDtos);
        }
    }

    private void findInspectionStaff(JSONObject plan, List<InspectionPlanDto> inspectionPlanDtos) {
        JSONArray staffs = plan.getJSONArray("children");
        JSONObject unitInfo = null;
        for (InspectionPlanDto tmpInspectionPlanDto : inspectionPlanDtos) {
            if(!plan.getString("communityId").equals(tmpInspectionPlanDto.getCommunityId())){
                continue;
            }
            if(!plan.getString("inspectionPlanId").equals(tmpInspectionPlanDto.getInspectionPlanId())){
                continue;
            }
            if (!hasInPlanStaff(tmpInspectionPlanDto, staffs)) {
                unitInfo = new JSONObject();
                unitInfo.put("id", "s_" + tmpInspectionPlanDto.getStaffId());
                unitInfo.put("staffId", tmpInspectionPlanDto.getStaffId());
                unitInfo.put("inspectionPlanId",plan.getString("inspectionPlanId"));
                unitInfo.put("communityId", plan.getString("communityId"));
                unitInfo.put("text", tmpInspectionPlanDto.getStaffName());
                unitInfo.put("icon", "/img/unit.png");
                staffs.add(unitInfo);
            }
        }

    }

    private boolean hasInPlanStaff(InspectionPlanDto inspectionPlanDto, JSONArray staffs) {
        JSONObject staff = null;
        for (int cIndex = 0; cIndex < staffs.size(); cIndex++) {
            staff = staffs.getJSONObject(cIndex);
            if (!staff.containsKey("staffId")) {
                continue;
            }
            if (StringUtil.isEmpty(staff.getString("staffId"))) {
                continue;
            }
            if (staff.getString("staffId").equals(inspectionPlanDto.getStaffId())) {
                return true;
            }
        }
        return false;
    }


    private boolean hasInInspectionPlan(InspectionPlanDto tmpInspectionPlanDto, JSONArray parkings) {
        JSONObject plan = null;
        for (int cIndex = 0; cIndex < parkings.size(); cIndex++) {
            plan = parkings.getJSONObject(cIndex);
            if (!plan.containsKey("inspectionPlanId")) {
                continue;
            }
            if (StringUtil.isEmpty(plan.getString("inspectionPlanId"))) {
                continue;
            }
            if (plan.getString("inspectionPlanId").equals(tmpInspectionPlanDto.getInspectionPlanId())) {
                return true;
            }
        }
        return false;
    }


    private boolean hasInCommunity(InspectionPlanDto tmpInspectionPlanDto, JSONArray communitys) {
        JSONObject community = null;
        for (int cIndex = 0; cIndex < communitys.size(); cIndex++) {
            community = communitys.getJSONObject(cIndex);
            if (!community.containsKey("communityId")) {
                continue;
            }
            if (StringUtil.isEmpty(community.getString("communityId"))) {
                continue;
            }
            if (community.getString("communityId").equals(tmpInspectionPlanDto.getCommunityId())) {
                return true;
            }
        }
        return false;
    }


}
