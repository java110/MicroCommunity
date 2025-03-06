package com.java110.report.cmd.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.floor.FloorDto;
import com.java110.intf.report.IReportCommunityInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 查询小区楼栋树形结构
 */
@Java110Cmd(serviceCode = "community.queryCommunityFloorTree")
public class QueryCommunityFloorTreeCmd extends Cmd {

    @Autowired
    private IReportCommunityInnerServiceSMO reportCommunityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        // must be administrator
        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        List<FloorDto> floorDtos = null;

        FloorDto floorDto = new FloorDto();


        floorDtos = reportCommunityInnerServiceSMOImpl.queryCommunityFloorTree(floorDto);
        JSONArray communitys = new JSONArray();
        if (ListUtil.isNull(floorDtos)) {
            context.setResponseEntity(ResultVo.createResponseEntity(communitys));
            return;
        }

        JSONObject communityInfo = null;
        for (FloorDto tmpFloorDto : floorDtos) {
            if (!hasInCommunity(tmpFloorDto, communitys)) {
                communityInfo = new JSONObject();
                communityInfo.put("id", "c_" + tmpFloorDto.getCommunityId());
                communityInfo.put("communityId", tmpFloorDto.getCommunityId());
                communityInfo.put("text", tmpFloorDto.getCommunityName());
                communityInfo.put("icon", "/img/org.png");
                communityInfo.put("children", new JSONArray());
                communitys.add(communityInfo);
            }
        }

        JSONObject community = null;
        for (int cIndex = 0; cIndex < communitys.size(); cIndex++) {
            community = communitys.getJSONObject(cIndex);
            // find floor data in unitDtos
            findFloor(community, floorDtos);
        }
        context.setResponseEntity(ResultVo.createResponseEntity(communitys));


    }

    /**
     * find community floor data
     *
     * @param community current community
     * @param unitDtos  all units data
     */
    private void findFloor(JSONObject community, List<FloorDto> unitDtos) {
        JSONArray floors = community.getJSONArray("children");
        JSONObject floorInfo = null;
        for (FloorDto tmpFloorDto : unitDtos) {
            if(!community.getString("communityId").equals(tmpFloorDto.getCommunityId())){
                continue;
            }
            if (!hasInFloor(tmpFloorDto, floors)) {
                floorInfo = new JSONObject();
                floorInfo.put("id", "f_" + tmpFloorDto.getFloorId());
                floorInfo.put("floorId", tmpFloorDto.getFloorId());
                floorInfo.put("communityId", community.getString("communityId"));
                floorInfo.put("text", tmpFloorDto.getFloorNum()+"栋");
                floorInfo.put("icon", "/img/floor.png");
                floorInfo.put("children", new JSONArray());
                floors.add(floorInfo);
            }
        }
    }



    private boolean hasInFloor(FloorDto tmpFloorDto, JSONArray floors) {
        JSONObject floor = null;
        for (int cIndex = 0; cIndex < floors.size(); cIndex++) {
            floor = floors.getJSONObject(cIndex);
            if (!floor.containsKey("floorId")) {
                continue;
            }
            if (StringUtil.isEmpty(floor.getString("floorId"))) {
                continue;
            }
            if (floor.getString("floorId").equals(tmpFloorDto.getFloorId())) {
                return true;
            }
        }
        return false;
    }


    private boolean hasInCommunity(FloorDto tmpFloorDto, JSONArray communitys) {
        JSONObject community = null;
        for (int cIndex = 0; cIndex < communitys.size(); cIndex++) {
            community = communitys.getJSONObject(cIndex);
            if (!community.containsKey("communityId")) {
                continue;
            }
            if (StringUtil.isEmpty(community.getString("communityId"))) {
                continue;
            }
            if (community.getString("communityId").equals(tmpFloorDto.getCommunityId())) {
                return true;
            }
        }
        return false;
    }


}
