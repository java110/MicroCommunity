package com.java110.community.cmd.inspectionPoint;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.StringUtils;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.room.RoomDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.inspection.InspectionDto;
import com.java110.dto.unit.FloorAndUnitDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IInspectionInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.inspectionPoint.ApiInspectionPointDataVo;
import com.java110.vo.api.inspectionPoint.ApiInspectionPointVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "inspectionPoint.listInspectionPoints")
public class ListInspectionPointsCmd extends Cmd {

    @Autowired
    private IInspectionInnerServiceSMO inspectionPointInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        if (reqJson.containsKey("relationship")) {
            queryRelationship(event, context, reqJson);
        } else {
            queryCommon(event, context, reqJson);
        }

    }

    /**
     * 关系查询
     *
     * @param event
     * @param context
     * @param reqJson
     */
    private void queryRelationship(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) {
        InspectionDto inspectionPointDto = BeanConvertUtil.covertBean(reqJson, InspectionDto.class);

        int count = inspectionPointInnerServiceSMOImpl.queryInspectionsRelationShipCount(inspectionPointDto);

        List<ApiInspectionPointDataVo> inspectionPoints = null;

        if (count > 0) {
            inspectionPoints = BeanConvertUtil.covertBeanList(inspectionPointInnerServiceSMOImpl.getInspectionRelationShip(inspectionPointDto), ApiInspectionPointDataVo.class);
            // 刷新 位置信息
            refreshInspections(inspectionPoints);
        } else {
            inspectionPoints = new ArrayList<>();
        }

        ApiInspectionPointVo apiInspectionPointVo = new ApiInspectionPointVo();

        apiInspectionPointVo.setTotal(count);
        apiInspectionPointVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiInspectionPointVo.setInspectionPoints(inspectionPoints);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiInspectionPointVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    private void refreshInspections(List<ApiInspectionPointDataVo> inspectionPoints) {
        String staffWechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN,"STAFF_WECHAT_URL");
        for (ApiInspectionPointDataVo apiInspectionPointDataVo : inspectionPoints) {
            apiInspectionPointDataVo.setUrl(staffWechatUrl
                    + "pages/excuteOneQrCodeInspection/excuteOneQrCodeInspection?inspectionId="
                    + apiInspectionPointDataVo.getInspectionId()
                    + "&inspectionName=" + apiInspectionPointDataVo.getInspectionName()
                    + "&itemId=" + apiInspectionPointDataVo.getItemId());
        }
    }

    /**
     * 普通查询
     *
     * @param event
     * @param context
     * @param reqJson
     */
    private void queryCommon(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) {
        InspectionDto inspectionPointDto = BeanConvertUtil.covertBean(reqJson, InspectionDto.class);

        int count = inspectionPointInnerServiceSMOImpl.queryInspectionsCount(inspectionPointDto);

        List<ApiInspectionPointDataVo> inspectionPoints = null;

        if (count > 0) {
            inspectionPoints = BeanConvertUtil.covertBeanList(inspectionPointInnerServiceSMOImpl.queryInspections(inspectionPointDto), ApiInspectionPointDataVo.class);
            // 刷新 位置信息
            refreshMachines(inspectionPoints);
            // 刷新 位置信息
            refreshInspections(inspectionPoints);
        } else {
            inspectionPoints = new ArrayList<>();
        }

        ApiInspectionPointVo apiInspectionPointVo = new ApiInspectionPointVo();

        apiInspectionPointVo.setTotal(count);
        apiInspectionPointVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiInspectionPointVo.setInspectionPoints(inspectionPoints);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiInspectionPointVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }


    private void refreshMachines(List<ApiInspectionPointDataVo> inspectionPoints) {
        //批量处理 小区
        refreshCommunitys(inspectionPoints);
        //批量处理单元信息
        refreshUnits(inspectionPoints);
        //批量处理 房屋信息
        refreshRooms(inspectionPoints);
    }


    /**
     * 处理小区信息
     */
    private void refreshCommunitys(List<ApiInspectionPointDataVo> inspectionPoints) {
        List<String> communityIds = new ArrayList<String>();
        List<ApiInspectionPointDataVo> inspectionPointDataVo = new ArrayList<>();
        for (ApiInspectionPointDataVo inspectionPoint : inspectionPoints) {

            if (!"2000".equals(inspectionPoint.getLocationTypeCd()) && !"3000".equals(inspectionPoint.getLocationTypeCd())) {
                if (StringUtils.isEmpty(inspectionPoint.getLocationObjId())) {
                    continue;
                }
                communityIds.add(inspectionPoint.getLocationObjId());
                inspectionPointDataVo.add(inspectionPoint);
            }
        }

        if (communityIds.size() < 1) {
            return;
        }
        String[] tmpCommunityIds = communityIds.toArray(new String[communityIds.size()]);

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityIds(tmpCommunityIds);
        //根据 userId 查询用户信息
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);

        for (ApiInspectionPointDataVo inspectionPoint : inspectionPointDataVo) {
            for (CommunityDto tmpCommunityDto : communityDtos) {
                if (inspectionPoint.getLocationObjId().equals(tmpCommunityDto.getCommunityId())) {
                    inspectionPoint.setLocationObjName(tmpCommunityDto.getName() + " " + inspectionPoint.getLocationTypeName());
                }
            }
        }
    }


    /**
     * 获取批量单元
     *
     * @param inspectionPoints
     * @return 批量userIds 信息
     */
    private void refreshUnits(List<ApiInspectionPointDataVo> inspectionPoints) {
        List<String> unitIds = new ArrayList<String>();
        List<ApiInspectionPointDataVo> tmpInspectionPoints = new ArrayList<>();
        for (ApiInspectionPointDataVo inspectionPointDataVo : inspectionPoints) {
            if ("2000".equals(inspectionPointDataVo.getLocationTypeCd())) {
                unitIds.add(inspectionPointDataVo.getLocationObjId());
                tmpInspectionPoints.add(inspectionPointDataVo);
            }
        }

        if (unitIds.size() < 1) {
            return;
        }
        String[] tmpUnitIds = unitIds.toArray(new String[unitIds.size()]);

        FloorAndUnitDto floorAndUnitDto = new FloorAndUnitDto();
        floorAndUnitDto.setUnitIds(tmpUnitIds);
        //根据 userId 查询用户信息
        List<FloorAndUnitDto> unitDtos = unitInnerServiceSMOImpl.getFloorAndUnitInfo(floorAndUnitDto);

        for (ApiInspectionPointDataVo inspectionPointDataVo : tmpInspectionPoints) {
            for (FloorAndUnitDto tmpUnitDto : unitDtos) {
                if (inspectionPointDataVo.getLocationObjId().equals(tmpUnitDto.getUnitId())) {
                    inspectionPointDataVo.setLocationObjName(tmpUnitDto.getFloorNum() + "栋" + tmpUnitDto.getUnitNum() + "单元");
                    BeanConvertUtil.covertBean(tmpUnitDto, inspectionPointDataVo);
                }
            }
        }
    }

    /**
     * 获取批量单元
     */
    private void refreshRooms(List<ApiInspectionPointDataVo> inspectionPoints) {
        List<String> roomIds = new ArrayList<String>();
        List<ApiInspectionPointDataVo> tmpInspectionPoint = new ArrayList<>();
        for (ApiInspectionPointDataVo inspectionPointDataVo : inspectionPoints) {

            if ("3000".equals(inspectionPointDataVo.getLocationTypeCd())) {
                roomIds.add(inspectionPointDataVo.getLocationObjId());
                tmpInspectionPoint.add(inspectionPointDataVo);
            }
        }
        if (roomIds.size() < 1) {
            return;
        }
        String[] tmpRoomIds = roomIds.toArray(new String[roomIds.size()]);

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(tmpRoomIds);
        roomDto.setCommunityId(inspectionPoints.get(0).getCommunityId());
        //根据 userId 查询用户信息
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        for (ApiInspectionPointDataVo inspectionPointDataVo : tmpInspectionPoint) {
            for (RoomDto tmpRoomDto : roomDtos) {
                if (inspectionPointDataVo.getLocationObjId().equals(tmpRoomDto.getRoomId())) {
                    inspectionPointDataVo.setLocationObjName(tmpRoomDto.getFloorNum() + "栋" + tmpRoomDto.getUnitNum() + "单元" + tmpRoomDto.getRoomNum() + "室");
                    BeanConvertUtil.covertBean(tmpRoomDto, inspectionPoints);
                }
            }
        }
    }
}
