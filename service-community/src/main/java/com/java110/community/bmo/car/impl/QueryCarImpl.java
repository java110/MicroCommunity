package com.java110.community.bmo.car.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.car.IQueryCar;
import com.java110.dto.RoomDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryCarImpl implements IQueryCar {

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> queryCarInfo(JSONObject reqJson) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(reqJson.getString("code"));
        ownerCarDto.setCarNum(reqJson.getString("license"));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        JSONArray data = new JSONArray();
        for (OwnerCarDto tmpOwnerCar : ownerCarDtos) {
            dealOwnerCar(reqJson, tmpOwnerCar, data);
        }
        return ResultVo.createResponseEntity(1,data.size(),data);
    }

    /**
     * 查询车辆信息
     *
     * @param reqJson
     * @param tmpOwnerCar
     * @param data
     */
    private void dealOwnerCar(JSONObject reqJson, OwnerCarDto tmpOwnerCar, JSONArray data) {
        JSONObject dataObj = new JSONObject();

        OwnerDto memberOwnerDto = new OwnerDto();
        memberOwnerDto.setOwnerId(tmpOwnerCar.getOwnerId());
        memberOwnerDto.setCommunityId(reqJson.getString("code"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(memberOwnerDto);

        if (ownerDtos != null && ownerDtos.size() > 0) {
            dataObj.put("name", ownerDtos.get(0).getName());
            dataObj.put("certificationType", "1");
            dataObj.put("certificationCode", ownerDtos.get(0).getIdCard());
            dataObj.put("content", ownerDtos.get(0).getLink());
        } else {
            dataObj.put("name", "");
            dataObj.put("certificationType", "");
            dataObj.put("certificationCode", "");
            dataObj.put("content", "");
        }
        dataObj.put("brand", tmpOwnerCar.getCarBrand());
        dataObj.put("color", tmpOwnerCar.getCarColor());
        dataObj.put("license", tmpOwnerCar.getCarNum());
        dataObj.put("carType", tmpOwnerCar.getCarType());
        dataObj.put("carPhoto", "");
        dataObj.put("vehiclePhoto", "");
        dataObj.put("vin", "");
        dataObj.put("engineNo", "");
        dataObj.put("update_time", tmpOwnerCar.getCreateTime());
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(tmpOwnerCar.getOwnerId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
            dataObj.put("floor", "");
            dataObj.put("building", "");
            dataObj.put("houses", "");
            data.add(dataObj);
            return;
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(ownerRoomRelDtos.get(0).getRoomId());
        roomDto.setCommunityId(reqJson.getString("code"));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        if (roomDtos == null || roomDtos.size() < 1) {
            dataObj.put("floor", "");
            dataObj.put("building", "");
            dataObj.put("houses", "");
            data.add(dataObj);
            return;
        }
        String floor = "";
        String building = "";
        String houses = "";
        for (RoomDto tmpRoomDto : roomDtos) {
            floor += (tmpRoomDto.getLayer() + ",");
            building += (tmpRoomDto.getFloorNum() + ",");
            houses += (tmpRoomDto.getRoomNum() + ",");
        }
        dataObj.put("floor", floor);
        dataObj.put("building", building);
        dataObj.put("houses", houses);
        data.add(dataObj);


    }


}
