package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.unit.FloorAndUnitDto;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.machine.ApiMachineDataVo;
import com.java110.vo.api.machine.ApiMachineVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "owner.listOwnerMachines")
public class ListOwnerMachinesCmd extends Cmd {

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "memberId", "请求报文中未包含业主信息");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String communityId = reqJson.getString("communityId");
        String memberId = reqJson.getString("memberId");

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setMemberId(memberId);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
        Assert.listOnlyOne(ownerDtos, "存在多条业主数据或未找到业主数据");
        String ownerId = ownerDtos.get(0).getOwnerId();

        RoomDto roomDto = new RoomDto();
        roomDto.setOwnerId(ownerId);
        List<RoomDto> rooms = roomInnerServiceSMOImpl.queryRoomsByOwner(roomDto);

        MachineDto machineDto = new MachineDto();
        machineDto.setCommunityId(communityId);
        List<String> locationObjIds = new ArrayList<>();
        locationObjIds.add(communityId);
        for (RoomDto tmpRoomDto : rooms) {
            locationObjIds.add(tmpRoomDto.getUnitId());
            locationObjIds.add(tmpRoomDto.getRoomId());
            locationObjIds.add(tmpRoomDto.getFloorId());
        }
        machineDto.setLocationObjIds(locationObjIds.toArray(new String[locationObjIds.size()]));
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
        if (machineDtos == null) {
            machineDtos = new ArrayList<>();
        }
        // 刷新 位置信息
        refreshMachines(machineDtos);

        ApiMachineVo apiMachineVo = new ApiMachineVo();
        apiMachineVo.setPage(1);
        apiMachineVo.setRecords(1);
        apiMachineVo.setRows(machineDtos.size());
        apiMachineVo.setMachines(BeanConvertUtil.covertBeanList(machineDtos, ApiMachineDataVo.class));
        ResponseEntity responseEntity = new ResponseEntity(JSONObject.toJSONString(apiMachineVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
    private void refreshMachines(List<MachineDto> machines) {

        //批量处理 小区
        refreshCommunitys(machines);

        //批量处理单元信息
        refreshUnits(machines);

        //批量处理 房屋信息
        refreshRooms(machines);

    }
    /**
     * 获取批量小区
     *
     * @param machines 设备信息
     * @return 批量userIds 信息
     */
    private void refreshCommunitys(List<MachineDto> machines) {
        List<String> communityIds = new ArrayList<String>();
        List<MachineDto> tmpMachineDtos = new ArrayList<>();
        for (MachineDto machineDto : machines) {

            if (!"2000".equals(machineDto.getLocationTypeCd())
                    && !"3000".equals(machineDto.getLocationTypeCd())
            ) {
                communityIds.add(machineDto.getLocationObjId());
                tmpMachineDtos.add(machineDto);
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

        for (MachineDto machineDto : tmpMachineDtos) {
            for (CommunityDto tmpCommunityDto : communityDtos) {
                if (machineDto.getLocationObjId().equals(tmpCommunityDto.getCommunityId())) {
                    machineDto.setLocationObjName(tmpCommunityDto.getName() + " " + machineDto.getLocationTypeName());
                }
            }
        }
    }


    /**
     * 获取批量单元
     *
     * @param machines 设备信息
     * @return 批量userIds 信息
     */
    private void refreshUnits(List<MachineDto> machines) {
        List<String> unitIds = new ArrayList<String>();
        List<MachineDto> tmpMachineDtos = new ArrayList<>();
        for (MachineDto machineDto : machines) {

            if ("2000".equals(machineDto.getLocationTypeCd())) {
                unitIds.add(machineDto.getLocationObjId());
                tmpMachineDtos.add(machineDto);
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

        for (MachineDto machineDto : tmpMachineDtos) {
            for (FloorAndUnitDto tmpUnitDto : unitDtos) {
                if (machineDto.getLocationObjId().equals(tmpUnitDto.getUnitId())) {
                    machineDto.setLocationObjName(tmpUnitDto.getFloorNum() + "栋" + tmpUnitDto.getUnitNum() + "单元");
                    BeanConvertUtil.covertBean(tmpUnitDto, machineDto);
                }
            }
        }
    }

    /**
     * 获取批量单元
     *
     * @param machines 设备信息
     * @return 批量userIds 信息
     */
    private void refreshRooms(List<MachineDto> machines) {
        List<String> roomIds = new ArrayList<String>();
        List<MachineDto> tmpMachineDtos = new ArrayList<>();
        for (MachineDto machineDto : machines) {

            if ("3000".equals(machineDto.getLocationTypeCd())) {
                roomIds.add(machineDto.getLocationObjId());
                tmpMachineDtos.add(machineDto);
            }
        }
        if (roomIds.size() < 1) {
            return;
        }
        String[] tmpRoomIds = roomIds.toArray(new String[roomIds.size()]);

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(tmpRoomIds);
        roomDto.setCommunityId(machines.get(0).getCommunityId());
        //根据 userId 查询用户信息
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        for (MachineDto machineDto : tmpMachineDtos) {
            for (RoomDto tmpRoomDto : roomDtos) {
                if (machineDto.getLocationObjId().equals(tmpRoomDto.getRoomId())) {
                    machineDto.setLocationObjName(tmpRoomDto.getFloorNum() + "栋" + tmpRoomDto.getUnitNum() + "单元" + tmpRoomDto.getRoomNum() + "室");
                    BeanConvertUtil.covertBean(tmpRoomDto, machineDto);
                }
            }
        }
    }
}
