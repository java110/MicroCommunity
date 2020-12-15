package com.java110.api.listener.owner;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName OwnerDto
 * @Description 查询业主车辆
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@Java110Listener("queryOwnerCarsListener")
public class QueryOwnerCarsListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;


    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_OWNER_CAR;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    /**
     * 业务层数据处理
     *
     * @param event 时间对象
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        //获取请求数据
        JSONObject reqJson = dataFlowContext.getReqJson();
        validateOwnerCarData(reqJson);

        int row = reqJson.getInteger("row");

        if (reqJson.containsKey("num") && !StringUtil.isEmpty(reqJson.getString("num"))) {
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setAreaNum(reqJson.getString("areaNum"));
            parkingSpaceDto.setNum(reqJson.getString("num"));
            parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
            if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) {
                ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(1, 1, new JSONArray());
                dataFlowContext.setResponseEntity(responseEntity);
                return;
            }

            reqJson.put("psId", parkingSpaceDtos.get(0).getPsId());
        }

        //查询总记录数
        int total = ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(BeanConvertUtil.covertBean(reqJson, OwnerCarDto.class));
        List<OwnerCarDto> ownerCarDtoList = null;

        if (total > 0) {
            ownerCarDtoList = ownerCarInnerServiceSMOImpl.queryOwnerCars(BeanConvertUtil.covertBean(reqJson, OwnerCarDto.class));

            freshPs(ownerCarDtoList);

            freshRoomInfo(ownerCarDtoList);
        } else {
            ownerCarDtoList = new ArrayList<>();
        }

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, ownerCarDtoList);
        dataFlowContext.setResponseEntity(responseEntity);
    }

    private void freshPs(List<OwnerCarDto> ownerCarDtoList) {

        if (ownerCarDtoList == null || ownerCarDtoList.size() < 1) {
            return;
        }

        List<String> psIds = new ArrayList<>();
        for (OwnerCarDto ownerCarDto : ownerCarDtoList) {
            if (StringUtil.isEmpty(ownerCarDto.getPsId())) {
                continue;
            }
            psIds.add(ownerCarDto.getPsId());
        }

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(ownerCarDtoList.get(0).getCommunityId());
        parkingSpaceDto.setPsIds(psIds.toArray(new String[psIds.size()]));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        for (ParkingSpaceDto tmpParkingSpaceDto : parkingSpaceDtos) {
            for (OwnerCarDto ownerCarDto : ownerCarDtoList) {
                if (tmpParkingSpaceDto.getPsId().equals(ownerCarDto.getPsId())) {
                    ownerCarDto.setAreaNum(tmpParkingSpaceDto.getAreaNum());
                    ownerCarDto.setNum(tmpParkingSpaceDto.getNum());
                }
            }
        }
    }

    /**
     * 刷入房屋信息
     *
     * @param ownerCarDtos
     */
    private void freshRoomInfo(List<OwnerCarDto> ownerCarDtos) {

        for (OwnerCarDto ownerCarDto : ownerCarDtos) {

            doFreshRoomInfo(ownerCarDto);
        }

    }

    /**
     * 车位信息刷入房屋信息
     *
     * @param ownerCarDto
     */
    private void doFreshRoomInfo(OwnerCarDto ownerCarDto) {
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(ownerCarDto.getOwnerId());

        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
            ownerCarDto.setRoomName("-");
            return;
        }

        List<String> roomIds = new ArrayList<>();
        for (OwnerRoomRelDto tOwnerRoomRelDto : ownerRoomRelDtos) {
            roomIds.add(tOwnerRoomRelDto.getRoomId());
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(ownerCarDto.getCommunityId());
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        String roomName = "";
        for (RoomDto tRoomDto : roomDtos) {
            roomName += (tRoomDto.getFloorNum() + "栋" + tRoomDto.getUnitNum() + "单元" + tRoomDto.getRoomNum() + "室" + "/");
        }

        roomName = roomName.endsWith("/") ? roomName.substring(0, roomName.length() - 1) : roomName;
        ownerCarDto.setRoomName(roomName);
    }


    /**
     * 校验查询条件是否满足条件
     *
     * @param reqJson 包含查询条件
     */
    private void validateOwnerCarData(JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.isInteger(reqJson.getString("page"), "不是有效数字");
        Assert.isInteger(reqJson.getString("row"), "不是有效数字");

    }

    @Override
    public int getOrder() {
        return super.DEFAULT_ORDER;
    }
}
