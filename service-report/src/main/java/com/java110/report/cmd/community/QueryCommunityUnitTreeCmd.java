package com.java110.report.cmd.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.room.RoomDto;
import com.java110.dto.unit.UnitDto;
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
 * 查询小区单元树形结构
 */
@Java110Cmd(serviceCode = "community.queryCommunityUnitTree")
public class QueryCommunityUnitTreeCmd extends Cmd {

    @Autowired
    private IReportCommunityInnerServiceSMO reportCommunityInnerServiceSMOImpl;

    @Autowired
    private IStaffCommunityV1InnerServiceSMO staffCommunityV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        // must be administrator
        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        List<UnitDto> unitDtos = null;

        UnitDto unitDto = new UnitDto();
        String staffId = CmdContextUtils.getUserId(context);

        List<String> communityIds = staffCommunityV1InnerServiceSMOImpl.queryStaffCommunityIds(staffId);

        if (!ListUtil.isNull(communityIds)) {
            unitDto.setCommunityIds(communityIds.toArray(new String[communityIds.size()]));
        }

        unitDtos = reportCommunityInnerServiceSMOImpl.queryCommunityUnitTree(unitDto);
        JSONArray communitys = new JSONArray();
        if (ListUtil.isNull(unitDtos)) {
            context.setResponseEntity(ResultVo.createResponseEntity(communitys));
            return;
        }

        JSONObject communityInfo = null;
        for (UnitDto tmpUnitDto : unitDtos) {
            if (!hasInCommunity(tmpUnitDto, communitys)) {
                communityInfo = new JSONObject();
                communityInfo.put("id", "c_" + tmpUnitDto.getCommunityId());
                communityInfo.put("communityId", tmpUnitDto.getCommunityId());
                communityInfo.put("text", tmpUnitDto.getCommunityName());
                communityInfo.put("icon", "/img/org.png");
                communityInfo.put("children", new JSONArray());
                communitys.add(communityInfo);
            }
        }

        JSONObject community = null;
        for (int cIndex = 0; cIndex < communitys.size(); cIndex++) {
            community = communitys.getJSONObject(cIndex);
            // find floor data in unitDtos
            findFloor(community, unitDtos);
        }
        context.setResponseEntity(ResultVo.createResponseEntity(communitys));


    }

    /**
     * find community floor data
     *
     * @param community current community
     * @param unitDtos  all units data
     */
    private void findFloor(JSONObject community, List<UnitDto> unitDtos) {
        JSONArray floors = community.getJSONArray("children");
        JSONObject floorInfo = null;
        for (UnitDto tmpUnitDto : unitDtos) {
            if (!community.getString("communityId").equals(tmpUnitDto.getCommunityId())) {
                continue;
            }
            if (!hasInFloor(tmpUnitDto, floors)) {
                floorInfo = new JSONObject();
                floorInfo.put("id", "f_" + tmpUnitDto.getFloorId());
                floorInfo.put("floorId", tmpUnitDto.getFloorId());
                floorInfo.put("communityId", community.getString("communityId"));
                floorInfo.put("text", tmpUnitDto.getFloorNum() + "栋");
                floorInfo.put("icon", "/img/floor.png");
                floorInfo.put("children", new JSONArray());
                floors.add(floorInfo);
            }
        }


        JSONObject floor = null;
        for (int cIndex = 0; cIndex < floors.size(); cIndex++) {
            floor = floors.getJSONObject(cIndex);
            // find floor data in unitDtos
            findUnit(floor, unitDtos);
        }
    }

    private void findUnit(JSONObject floor, List<UnitDto> unitDtos) {
        JSONArray units = floor.getJSONArray("children");
        JSONObject unitInfo = null;
        for (UnitDto tmpUnitDto : unitDtos) {
            if (!floor.getString("communityId").equals(tmpUnitDto.getCommunityId())) {
                continue;
            }
            if (!floor.getString("floorId").equals(tmpUnitDto.getFloorId())) {
                continue;
            }
            if (!hasInUnit(tmpUnitDto, units)) {
                unitInfo = new JSONObject();
                unitInfo.put("id", "u_" + tmpUnitDto.getUnitId());
                unitInfo.put("unitId", tmpUnitDto.getUnitId());
                unitInfo.put("text", tmpUnitDto.getUnitNum() + "单元");
                unitInfo.put("icon", "/img/unit.png");
                units.add(unitInfo);
            }
        }

    }

    private boolean hasInUnit(UnitDto unitDto, JSONArray units) {
        JSONObject unit = null;
        for (int cIndex = 0; cIndex < units.size(); cIndex++) {
            unit = units.getJSONObject(cIndex);
            if (!unit.containsKey("unitId")) {
                continue;
            }
            if (StringUtil.isEmpty(unit.getString("unitId"))) {
                continue;
            }
            if (unit.getString("unitId").equals(unitDto.getUnitId())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasInFloor(UnitDto tmpUnitDto, JSONArray floors) {
        JSONObject floor = null;
        for (int cIndex = 0; cIndex < floors.size(); cIndex++) {
            floor = floors.getJSONObject(cIndex);
            if (!floor.containsKey("floorId")) {
                continue;
            }
            if (StringUtil.isEmpty(floor.getString("floorId"))) {
                continue;
            }
            if (floor.getString("floorId").equals(tmpUnitDto.getFloorId())) {
                return true;
            }
        }
        return false;
    }


    private boolean hasInCommunity(UnitDto tmpUnitDto, JSONArray communitys) {
        JSONObject community = null;
        for (int cIndex = 0; cIndex < communitys.size(); cIndex++) {
            community = communitys.getJSONObject(cIndex);
            if (!community.containsKey("communityId")) {
                continue;
            }
            if (StringUtil.isEmpty(community.getString("communityId"))) {
                continue;
            }
            if (community.getString("communityId").equals(tmpUnitDto.getCommunityId())) {
                return true;
            }
        }
        return false;
    }


}
