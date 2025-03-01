package com.java110.report.cmd.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.dict.DictDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.unit.UnitDto;
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
 * 查询小区停车场树形结构
 */
@Java110Cmd(serviceCode = "community.queryCommunityParkingTree")
public class QueryCommunityParkingTreeCmd extends Cmd {

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

        List<ParkingAreaDto> parkingAreaDtos = null;

        ParkingAreaDto parkingAreaDto = new ParkingAreaDto();


        parkingAreaDtos = reportCommunityInnerServiceSMOImpl.queryCommunityParkingTree(parkingAreaDto);
        JSONArray communitys = new JSONArray();
        if (ListUtil.isNull(parkingAreaDtos)) {
            context.setResponseEntity(ResultVo.createResponseEntity(communitys));
            return;
        }

        JSONObject communityInfo = null;
        for (ParkingAreaDto tmpParkingAreaDto : parkingAreaDtos) {
            if (!hasInCommunity(tmpParkingAreaDto, communitys)) {
                communityInfo = new JSONObject();
                communityInfo.put("id", "c_" + tmpParkingAreaDto.getCommunityId());
                communityInfo.put("communityId", tmpParkingAreaDto.getCommunityId());
                communityInfo.put("text", tmpParkingAreaDto.getCommunityName());
                communityInfo.put("icon", "/img/org.png");
                communityInfo.put("children", new JSONArray());
                communitys.add(communityInfo);
            }
        }

        DictDto dictDto = new DictDto();
        dictDto.setTableName("owner_car");
        dictDto.setTableColumns("lease_type");
        List<DictDto> dictDtos = dictV1InnerServiceSMOImpl.queryDicts(dictDto);

        JSONObject community = null;
        for (int cIndex = 0; cIndex < communitys.size(); cIndex++) {
            community = communitys.getJSONObject(cIndex);
            // find floor data in unitDtos
            findParkingArea(community, parkingAreaDtos, dictDtos);
        }
        context.setResponseEntity(ResultVo.createResponseEntity(communitys));


    }

    /**
     * find community floor data
     *
     * @param community       current community
     * @param parkingAreaDtos all units data
     */
    private void findParkingArea(JSONObject community, List<ParkingAreaDto> parkingAreaDtos, List<DictDto> dictDtos) {
        JSONArray parkings = community.getJSONArray("children");
        JSONObject parkingInfo = null;
        for (ParkingAreaDto tmpParkingAreaDto : parkingAreaDtos) {
            if (!hasInParkingArea(tmpParkingAreaDto, parkings)) {
                parkingInfo = new JSONObject();
                parkingInfo.put("id", "p_" + tmpParkingAreaDto.getPaId());
                parkingInfo.put("paId", tmpParkingAreaDto.getPaId());
                parkingInfo.put("text", tmpParkingAreaDto.getNum() + "停车场");
                parkingInfo.put("icon", "/img/floor.png");
                parkingInfo.put("children", new JSONArray());
                parkings.add(parkingInfo);
            }
        }


        JSONObject parking = null;
        for (int cIndex = 0; cIndex < parkings.size(); cIndex++) {
            parking = parkings.getJSONObject(cIndex);
            // find floor data in unitDtos
            findLeaseType(parking, parkingAreaDtos, dictDtos);
        }
    }

    private void findLeaseType(JSONObject parking, List<ParkingAreaDto> parkingAreaDtos, List<DictDto> dictDtos) {
        JSONArray leaseTypes = parking.getJSONArray("children");
        JSONObject leaseTypeInfo = null;
        for (DictDto tmpDictDto : dictDtos) {
            leaseTypeInfo = new JSONObject();
            leaseTypeInfo.put("id", "l_" + tmpDictDto.getStatusCd());
            leaseTypeInfo.put("leaseType", tmpDictDto.getStatusCd());
            leaseTypeInfo.put("text", tmpDictDto.getName());
            leaseTypeInfo.put("icon", "/img/unit.png");
            leaseTypes.add(leaseTypeInfo);
        }

    }


    private boolean hasInParkingArea(ParkingAreaDto tmpParkingAreaDto, JSONArray parkings) {
        JSONObject parkingArea = null;
        for (int cIndex = 0; cIndex < parkings.size(); cIndex++) {
            parkingArea = parkings.getJSONObject(cIndex);
            if (!parkingArea.containsKey("paId")) {
                continue;
            }
            if (StringUtil.isEmpty(parkingArea.getString("paId"))) {
                continue;
            }
            if (parkingArea.getString("paId").equals(tmpParkingAreaDto.getPaId())) {
                return true;
            }
        }
        return false;
    }


    private boolean hasInCommunity(ParkingAreaDto tmpParkingAreaDto, JSONArray communitys) {
        JSONObject community = null;
        for (int cIndex = 0; cIndex < communitys.size(); cIndex++) {
            community = communitys.getJSONObject(cIndex);
            if (!community.containsKey("communityId")) {
                continue;
            }
            if (StringUtil.isEmpty(community.getString("communityId"))) {
                continue;
            }
            if (community.getString("communityId").equals(tmpParkingAreaDto.getCommunityId())) {
                return true;
            }
        }
        return false;
    }


}
