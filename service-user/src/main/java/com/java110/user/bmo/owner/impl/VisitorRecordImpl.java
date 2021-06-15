package com.java110.user.bmo.owner.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.RoomDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.visit.VisitDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IVisitInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.user.bmo.owner.IVisitorRecord;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitorRecordImpl implements IVisitorRecord {

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> query(JSONObject reqJson) {
        VisitDto visitDto = new VisitDto();
        visitDto.setCommunityId(reqJson.getString("code"));
        List<VisitDto> visitDtos = visitInnerServiceSMOImpl.queryVisits(visitDto);
        JSONArray data = new JSONArray();

        for (VisitDto tmpVisitDto : visitDtos) {
            dealVisit(reqJson, tmpVisitDto, data);
        }
        return ResultVo.createResponseEntity(1,data.size(),data);
    }

    private void dealVisit(JSONObject reqJson, VisitDto tmpVisitDto, JSONArray data) {
        JSONObject dataObj = new JSONObject();
        dataObj.put("name", tmpVisitDto.getvName());
        dataObj.put("gender", tmpVisitDto.getVisitGender());
        dataObj.put("certificationType", "1");
        dataObj.put("certificationCode", "");
        dataObj.put("contact", tmpVisitDto.getPhoneNumber());
        dataObj.put("visitorType", "9");
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(tmpVisitDto.getOwnerId());
        ownerDto.setCommunityId(tmpVisitDto.getCommunityId());
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        if (ownerDtos != null && ownerDtos.size() > 0) {
            dataObj.put("ownerName", ownerDtos.get(0).getName());
            dataObj.put("ownerCertificationType", "1");
            dataObj.put("ownerCertificationCode", ownerDtos.get(0).getIdCard());
        } else {
            dataObj.put("ownerName", "");
            dataObj.put("ownerCertificationType", "");
            dataObj.put("ownerCertificationCode", "");
        }

        dataObj.put("reserveTime", tmpVisitDto.getVisitTime());
        dataObj.put("pwd", "");
        dataObj.put("photo", "");
        dataObj.put("update_time", tmpVisitDto.getVisitTime());

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(tmpVisitDto.getOwnerId());
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
