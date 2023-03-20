package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateV1InnerServiceSMO;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "owner.queryOwnerCars")
public class QueryOwnerCarsCmd extends Cmd {

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateV1InnerServiceSMO machineTranslateV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.isInteger(reqJson.getString("page"), "不是有效数字");
        Assert.isInteger(reqJson.getString("row"), "不是有效数字");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        int row = reqJson.getInteger("row");
        if (reqJson.containsKey("num") && !StringUtil.isEmpty(reqJson.getString("num"))) {
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setAreaNum(reqJson.getString("areaNum"));
            parkingSpaceDto.setNum(reqJson.getString("num"));
            parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
            if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) {
                ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(1, 1, new JSONArray());
                cmdDataFlowContext.setResponseEntity(responseEntity);
                return;
            }
            reqJson.put("psId", parkingSpaceDtos.get(0).getPsId());
        }
        OwnerCarDto ownerCarDto = BeanConvertUtil.covertBean(reqJson, OwnerCarDto.class);
        if (reqJson.containsKey("carTypeCds")) {
            ownerCarDto.setCarTypeCd("");
            ownerCarDto.setCarTypeCds(reqJson.getString("carTypeCds").split(","));
        }
        //查询总记录数
        int total = ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto);
//        int count = 0;
        List<OwnerCarDto> ownerCarDtoList = null;
        if (total > 0) {
            ownerCarDtoList = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            //小区20条时刷房屋和车位信息
            if (row < 20) {
                freshPs(ownerCarDtoList);
                freshRoomInfo(ownerCarDtoList);
                //刷入同步物联网状态
                freshTransactionIotState(ownerCarDtoList);
            }
        } else {
            ownerCarDtoList = new ArrayList<>();
        }
        //查询是否有脱敏权限
        List<Map> privileges = null;
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/roomCreateFee");
        basePrivilegeDto.setUserId(reqJson.getString("userId"));
        privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges == null || privileges.size() == 0) {
            for (OwnerCarDto tmpOwnerCarDto : ownerCarDtoList) {
                String link = tmpOwnerCarDto.getLink();
                if (!StringUtil.isEmpty(link)) {
                    link = link.substring(0, 3) + "****" + link.substring(7);
                    tmpOwnerCarDto.setLink(link);
                }
            }
        }
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, ownerCarDtoList);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void freshTransactionIotState(List<OwnerCarDto> ownerCarDtoList) {
        if (ownerCarDtoList == null || ownerCarDtoList.size() < 1) {
            return;
        }
        List<String> memberIds = new ArrayList<>();
        for (OwnerCarDto ownerCarDto : ownerCarDtoList) {
            if (StringUtil.isEmpty(ownerCarDto.getPsId())) {
                continue;
            }
            memberIds.add(ownerCarDto.getMemberId());
        }

        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setObjIds(memberIds.toArray(new String[memberIds.size()]));
        List<MachineTranslateDto> machineTranslateDtos = machineTranslateV1InnerServiceSMOImpl.queryObjStateInMachineTranslates(machineTranslateDto);

        if (machineTranslateDtos == null || machineTranslateDtos.size() < 1) {
            return;
        }

        for (OwnerCarDto ownerCarDto : ownerCarDtoList) {
            for (MachineTranslateDto tmpMachineTranslateDto : machineTranslateDtos) {
                if (!ownerCarDto.getMemberId().equals(tmpMachineTranslateDto.getObjId())) {
                    continue;
                }
                ownerCarDto.setIotStateName(tmpMachineTranslateDto.getStateName());
                ownerCarDto.setIotRemark(tmpMachineTranslateDto.getRemark());
            }
        }


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
                    ownerCarDto.setParkingType(tmpParkingSpaceDto.getParkingType());
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
        ownerRoomRelDto.setPage(1);
        ownerRoomRelDto.setRow(3); //只展示3个房屋以内 不然页面太乱
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
}
