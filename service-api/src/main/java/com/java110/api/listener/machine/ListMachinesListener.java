package com.java110.api.listener.machine;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.community.ICommunityLocationInnerServiceSMO;
import com.java110.core.smo.community.IFloorInnerServiceSMO;
import com.java110.core.smo.common.IMachineInnerServiceSMO;
import com.java110.core.smo.community.IRoomInnerServiceSMO;
import com.java110.core.smo.community.IUnitInnerServiceSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.communityLocation.CommunityLocationDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.unit.FloorAndUnitDto;
import com.java110.utils.constant.ServiceCodeMachineConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.machine.ApiMachineDataVo;
import com.java110.vo.api.machine.ApiMachineVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listMachinesListener")
public class ListMachinesListener extends AbstractServiceApiListener {

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private ICommunityLocationInnerServiceSMO communityLocationInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineConstant.LIST_MACHINES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IMachineInnerServiceSMO getMachineInnerServiceSMOImpl() {
        return machineInnerServiceSMOImpl;
    }

    public void setMachineInnerServiceSMOImpl(IMachineInnerServiceSMO machineInnerServiceSMOImpl) {
        this.machineInnerServiceSMOImpl = machineInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        super.validatePageInfo(reqJson);

        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含小区信息");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        MachineDto machineDto = BeanConvertUtil.covertBean(reqJson, MachineDto.class);

        int count = machineInnerServiceSMOImpl.queryMachinesCount(machineDto);

        List<ApiMachineDataVo> machines = null;

        if (count > 0) {
            List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
            // 刷新 位置信息
            //refreshMachines(machineDtos);
            refreshMachineLocation(machineDtos);
            machines = BeanConvertUtil.covertBeanList(machineDtos, ApiMachineDataVo.class);
        } else {
            machines = new ArrayList<>();
        }

        ApiMachineVo apiMachineVo = new ApiMachineVo();

        apiMachineVo.setTotal(count);
        apiMachineVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMachineVo.setMachines(machines);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMachineVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    private void refreshMachineLocation(List<MachineDto> machines) {
        for (MachineDto machineDto : machines) {
            getMachineLocation(machineDto);
        }
    }

    private void refreshMachines(List<MachineDto> machines) {

        //批量处理 小区
        refreshCommunitys(machines);

        //批量处理单元信息
        refreshUnits(machines);

        //批量处理 房屋信息
        refreshRooms(machines);

        //位置未分配时
        refreshOther(machines);

    }

    private void getMachineLocation(MachineDto machineDto) {
        CommunityLocationDto communityLocationDto = new CommunityLocationDto();
        communityLocationDto.setCommunityId(machineDto.getCommunityId());
        communityLocationDto.setLocationId(machineDto.getLocationTypeCd());
        List<CommunityLocationDto> communityLocationDtos = communityLocationInnerServiceSMOImpl.queryCommunityLocations(communityLocationDto);

        if (communityLocationDtos == null || communityLocationDtos.size() < 1) {
            machineDto.setLocationType(machineDto.getLocationTypeCd());
            return;
        }

        machineDto.setLocationType(communityLocationDtos.get(0).getLocationType());
        machineDto.setLocationObjName(communityLocationDtos.get(0).getLocationName());
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
            getMachineLocation(machineDto);
            if (!"2000".equals(machineDto.getLocationType())
                    && !"3000".equals(machineDto.getLocationType())
                    && !"4000".equals(machineDto.getLocationType())
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
            getMachineLocation(machineDto);
            if ("2000".equals(machineDto.getLocationType())) {
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
            getMachineLocation(machineDto);
            if ("3000".equals(machineDto.getLocationType())) {
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

    /**
     * 获取批量单元
     *
     * @param machines 设备信息
     * @return 批量userIds 信息
     */
    private void refreshOther(List<MachineDto> machines) {
        for (MachineDto machineDto : machines) {

            if ("4000".equals(machineDto.getLocationTypeCd())) {
                machineDto.setLocationObjName("未分配");
            }
        }

    }

    public ICommunityInnerServiceSMO getCommunityInnerServiceSMOImpl() {
        return communityInnerServiceSMOImpl;
    }

    public void setCommunityInnerServiceSMOImpl(ICommunityInnerServiceSMO communityInnerServiceSMOImpl) {
        this.communityInnerServiceSMOImpl = communityInnerServiceSMOImpl;
    }

    public IFloorInnerServiceSMO getFloorInnerServiceSMOImpl() {
        return floorInnerServiceSMOImpl;
    }

    public void setFloorInnerServiceSMOImpl(IFloorInnerServiceSMO floorInnerServiceSMOImpl) {
        this.floorInnerServiceSMOImpl = floorInnerServiceSMOImpl;
    }

    public IUnitInnerServiceSMO getUnitInnerServiceSMOImpl() {
        return unitInnerServiceSMOImpl;
    }

    public void setUnitInnerServiceSMOImpl(IUnitInnerServiceSMO unitInnerServiceSMOImpl) {
        this.unitInnerServiceSMOImpl = unitInnerServiceSMOImpl;
    }

    public IRoomInnerServiceSMO getRoomInnerServiceSMOImpl() {
        return roomInnerServiceSMOImpl;
    }

    public void setRoomInnerServiceSMOImpl(IRoomInnerServiceSMO roomInnerServiceSMOImpl) {
        this.roomInnerServiceSMOImpl = roomInnerServiceSMOImpl;
    }
}
