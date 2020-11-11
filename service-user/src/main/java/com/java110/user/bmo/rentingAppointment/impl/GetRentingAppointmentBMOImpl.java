package com.java110.user.bmo.rentingAppointment.impl;

import com.java110.dto.RoomDto;
import com.java110.dto.rentingAppointment.RentingAppointmentDto;
import com.java110.intf.user.IRentingAppointmentInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.user.bmo.rentingAppointment.IGetRentingAppointmentBMO;
import com.java110.vo.ResultVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("getRentingAppointmentBMOImpl")
public class GetRentingAppointmentBMOImpl implements IGetRentingAppointmentBMO {

    @Autowired
    private IRentingAppointmentInnerServiceSMO rentingAppointmentInnerServiceSMOImpl;


    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    /**
     * @param rentingAppointmentDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(RentingAppointmentDto rentingAppointmentDto) {


        int count = rentingAppointmentInnerServiceSMOImpl.queryRentingAppointmentsCount(rentingAppointmentDto);

        List<RentingAppointmentDto> rentingAppointmentDtos = null;
        if (count > 0) {
            rentingAppointmentDtos = rentingAppointmentInnerServiceSMOImpl.queryRentingAppointments(rentingAppointmentDto);

            refreshEveryRoom(rentingAppointmentDtos);
        } else {
            rentingAppointmentDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) rentingAppointmentDto.getRow()), count, rentingAppointmentDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void refreshRoom(List<RentingAppointmentDto> rentingAppointmentDtos) {

        List<String> roomIds = new ArrayList<>();

        for (RentingAppointmentDto rentingAppointmentDto : rentingAppointmentDtos) {
            if (!StringUtils.isEmpty(rentingAppointmentDto.getAppointmentRoomId())) {
                roomIds.add(rentingAppointmentDto.getAppointmentRoomId());
            }

            if (!StringUtils.isEmpty(rentingAppointmentDto.getRoomId())) {
                roomIds.add(rentingAppointmentDto.getAppointmentRoomId());
            }
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);


        for (RoomDto tmpRoomDto : roomDtos) {
            for (RentingAppointmentDto rentingAppointmentDto : rentingAppointmentDtos) {
                if (tmpRoomDto.getRoomId().equals(rentingAppointmentDto.getAppointmentRoomId())) {
                    rentingAppointmentDto.setAppointmentRoomName(tmpRoomDto.getFloorNum() + "栋" + tmpRoomDto.getUnitNum() + "单元" + tmpRoomDto.getRoomNum() + "室");
                }

                if (tmpRoomDto.getRoomId().equals(rentingAppointmentDto.getRoomId())) {
                    rentingAppointmentDto.setRoomName(tmpRoomDto.getFloorNum() + "栋" + tmpRoomDto.getUnitNum() + "单元" + tmpRoomDto.getRoomNum() + "室");
                }
            }
        }

    }

    private void refreshEveryRoom(List<RentingAppointmentDto> rentingAppointmentDtos) {

        List<Map> roomInfos = new ArrayList<>();

        Map room = null;
        for (RentingAppointmentDto rentingAppointmentDto : rentingAppointmentDtos) {
            if (!StringUtils.isEmpty(rentingAppointmentDto.getAppointmentRoomId()) && !StringUtils.isEmpty(rentingAppointmentDto.getAppointmentCommunityId())) {
                room = new HashMap();
                room.put("roomId", rentingAppointmentDto.getAppointmentRoomId());
                room.put("communityId", rentingAppointmentDto.getAppointmentCommunityId());
                roomInfos.add(room);
            }
            if (!StringUtils.isEmpty(rentingAppointmentDto.getRoomId())&&!StringUtils.isEmpty(rentingAppointmentDto.getCommunityId())) {
                room = new HashMap();
                room.put("roomId", rentingAppointmentDto.getRoomId());
                room.put("communityId", rentingAppointmentDto.getCommunityId());
                roomInfos.add(room);
            }
        }

        if (roomInfos.size() < 1) {
            return;
        }
        List<Map> newRoomInfos = new ArrayList<>();
        Boolean hasRoom = false;
        for (Map roomMap : roomInfos) {
            hasRoom = false;
            for (Map newRoomInfo : newRoomInfos) {
                if (roomMap.get("roomId").equals(newRoomInfo.get("roomId"))) {
                    hasRoom = true;
                    break;
                }
            }
            if (!hasRoom) {
                newRoomInfos.add(roomMap);
            }
        }
        RoomDto roomDto = new RoomDto();
        for (Map newRoomInfo : newRoomInfos) {
            roomDto.setRoomId(newRoomInfo.get("roomId").toString());
            roomDto.setCommunityId(newRoomInfo.get("communityId").toString());
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

            if (roomDtos == null || roomDtos.size() < 1) {
                continue;
            }
            RoomDto tmpRoomDto = roomDtos.get(0);
            newRoomInfo.put("roomName", tmpRoomDto.getFloorNum() + "栋" + tmpRoomDto.getUnitNum() + "单元" + tmpRoomDto.getRoomNum() + "室");
        }



        for (Map newRoomInfo : newRoomInfos) {
            for (RentingAppointmentDto rentingAppointmentDto : rentingAppointmentDtos) {
                if (newRoomInfo.get("roomId").equals(rentingAppointmentDto.getAppointmentRoomId())) {
                    rentingAppointmentDto.setAppointmentRoomName(newRoomInfo.get("roomName").toString());
                }

                if (newRoomInfo.get("roomId").equals(rentingAppointmentDto.getRoomId())) {
                    rentingAppointmentDto.setRoomName(newRoomInfo.get("roomName").toString());
                }
            }
        }

    }

}
