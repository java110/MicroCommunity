package com.java110.api.listener.inspectionRoute;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IInspectionRoutePointRelInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.inspectionRoute.InspectionRoutePointRelDto;
import com.java110.dto.unit.FloorAndUnitDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeInspectionRouteConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.inspectionPoint.ApiInspectionPointDataVo;
import com.java110.vo.api.inspectionPoint.ApiInspectionPointVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listInspectionRoutePointsListener")
public class ListInspectionRoutePointsListener extends AbstractServiceApiListener {

    @Autowired
    private IInspectionRoutePointRelInnerServiceSMO inspectionRoutePointRelInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionRouteConstant.LIST_INSPECTIONROUTE_POINTS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IInspectionRoutePointRelInnerServiceSMO getInspectionRoutePointRelInnerServiceSMOImpl() {
        return inspectionRoutePointRelInnerServiceSMOImpl;
    }

    public void setInspectionRoutePointRelInnerServiceSMOImpl(IInspectionRoutePointRelInnerServiceSMO inspectionRoutePointRelInnerServiceSMOImpl) {
        this.inspectionRoutePointRelInnerServiceSMOImpl = inspectionRoutePointRelInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        InspectionRoutePointRelDto inspectionRoutePointRelDto = BeanConvertUtil.covertBean(reqJson, InspectionRoutePointRelDto.class);

        int count = inspectionRoutePointRelInnerServiceSMOImpl.queryInspectionRoutePointRelsCount(inspectionRoutePointRelDto);

        List<ApiInspectionPointDataVo> inspectionPoints = null;

        if (count > 0) {
            inspectionPoints = BeanConvertUtil.covertBeanList(inspectionRoutePointRelInnerServiceSMOImpl.queryInspectionRoutePointRels(inspectionRoutePointRelDto), ApiInspectionPointDataVo.class);
            // 刷新 位置信息
            refreshMachines(inspectionPoints);
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
