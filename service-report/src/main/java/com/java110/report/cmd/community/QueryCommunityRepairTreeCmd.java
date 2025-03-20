package com.java110.report.cmd.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.dict.DictDto;
import com.java110.dto.repair.RepairSettingDto;
import com.java110.intf.dev.IDictV1InnerServiceSMO;
import com.java110.intf.report.IReportCommunityInnerServiceSMO;
import com.java110.intf.user.IStaffCommunityV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 查询小区报修树形结构
 */
@Java110Cmd(serviceCode = "community.queryCommunityRepairTree")
public class QueryCommunityRepairTreeCmd extends Cmd {

    @Autowired
    private IReportCommunityInnerServiceSMO reportCommunityInnerServiceSMOImpl;

    @Autowired
    private IDictV1InnerServiceSMO dictV1InnerServiceSMOImpl;


    @Autowired
    private IStaffCommunityV1InnerServiceSMO staffCommunityV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        // must be administrator
        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        List<RepairSettingDto> repairSettingDtos = null;

        RepairSettingDto repairSettingDto = new RepairSettingDto();
        String staffId = CmdContextUtils.getUserId(context);

        List<String> communityIds = staffCommunityV1InnerServiceSMOImpl.queryStaffCommunityIds(staffId);

        if (!ListUtil.isNull(communityIds)) {
            repairSettingDto.setCommunityIds(communityIds.toArray(new String[communityIds.size()]));
        }

        repairSettingDtos = reportCommunityInnerServiceSMOImpl.queryCommunityRepairTree(repairSettingDto);
        JSONArray communitys = new JSONArray();
        if (ListUtil.isNull(repairSettingDtos)) {
            context.setResponseEntity(ResultVo.createResponseEntity(communitys));
            return;
        }

        JSONObject communityInfo = null;
        for (RepairSettingDto tmpRepairSettingDto : repairSettingDtos) {
            if (!hasInCommunity(tmpRepairSettingDto, communitys)) {
                communityInfo = new JSONObject();
                communityInfo.put("id", "c_" + tmpRepairSettingDto.getCommunityId());
                communityInfo.put("communityId", tmpRepairSettingDto.getCommunityId());
                communityInfo.put("text", tmpRepairSettingDto.getCommunityName());
                communityInfo.put("icon", "/img/org.png");
                communityInfo.put("children", new JSONArray());
                communitys.add(communityInfo);
            }
        }

        DictDto dictDto = new DictDto();
        dictDto.setTableName("r_repair_pool");
        dictDto.setTableColumns("state");
        List<DictDto> dictDtos = dictV1InnerServiceSMOImpl.queryDicts(dictDto);

        JSONObject community = null;
        for (int cIndex = 0; cIndex < communitys.size(); cIndex++) {
            community = communitys.getJSONObject(cIndex);
            // find floor data in unitDtos
            findRepairSetting(community, repairSettingDtos, dictDtos);
        }
        context.setResponseEntity(ResultVo.createResponseEntity(communitys));


    }

    /**
     * find community floor data
     *
     * @param community       current community
     * @param repairSettingDtos all units data
     */
    private void findRepairSetting(JSONObject community, List<RepairSettingDto> repairSettingDtos, List<DictDto> dictDtos) {
        JSONArray parkings = community.getJSONArray("children");
        JSONObject parkingInfo = null;
        for (RepairSettingDto tmpRepairSettingDto : repairSettingDtos) {
            if(!community.getString("communityId").equals(tmpRepairSettingDto.getCommunityId())){
                continue;
            }
            if (!hasInRepairSetting(tmpRepairSettingDto, parkings)) {
                parkingInfo = new JSONObject();
                parkingInfo.put("id", "r_" + tmpRepairSettingDto.getRepairType());
                parkingInfo.put("repairType", tmpRepairSettingDto.getRepairType());
                parkingInfo.put("communityId", community.getString("communityId"));
                parkingInfo.put("text", tmpRepairSettingDto.getRepairTypeName());
                parkingInfo.put("icon", "/img/floor.png");
                parkingInfo.put("children", new JSONArray());
                parkings.add(parkingInfo);
            }
        }


        JSONObject parking = null;
        for (int cIndex = 0; cIndex < parkings.size(); cIndex++) {
            parking = parkings.getJSONObject(cIndex);
            // find floor data in unitDtos
            findState(parking, repairSettingDtos, dictDtos);
        }
    }

    private void findState(JSONObject parking, List<RepairSettingDto> repairSettingDtos, List<DictDto> dictDtos) {
        JSONArray states = parking.getJSONArray("children");
        JSONObject stateInfo = null;
        for (DictDto tmpDictDto : dictDtos) {
            stateInfo = new JSONObject();
            stateInfo.put("id", "s_" + GenerateCodeFactory.getUUID());
            stateInfo.put("state", tmpDictDto.getStatusCd());
            stateInfo.put("repairType", parking.getString("repairType"));
            stateInfo.put("communityId", parking.getString("communityId"));
            stateInfo.put("text", tmpDictDto.getName());
            stateInfo.put("icon", "/img/unit.png");
            states.add(stateInfo);
        }

    }


    private boolean hasInRepairSetting(RepairSettingDto tmpRepairSettingDto, JSONArray parkings) {
        JSONObject parkingArea = null;
        for (int cIndex = 0; cIndex < parkings.size(); cIndex++) {
            parkingArea = parkings.getJSONObject(cIndex);
            if (!parkingArea.containsKey("repairType")) {
                continue;
            }
            if (StringUtil.isEmpty(parkingArea.getString("repairType"))) {
                continue;
            }
            if (parkingArea.getString("repairType").equals(tmpRepairSettingDto.getRepairType())) {
                return true;
            }
        }
        return false;
    }


    private boolean hasInCommunity(RepairSettingDto tmpRepairSettingDto, JSONArray communitys) {
        JSONObject community = null;
        for (int cIndex = 0; cIndex < communitys.size(); cIndex++) {
            community = communitys.getJSONObject(cIndex);
            if (!community.containsKey("communityId")) {
                continue;
            }
            if (StringUtil.isEmpty(community.getString("communityId"))) {
                continue;
            }
            if (community.getString("communityId").equals(tmpRepairSettingDto.getCommunityId())) {
                return true;
            }
        }
        return false;
    }


}
