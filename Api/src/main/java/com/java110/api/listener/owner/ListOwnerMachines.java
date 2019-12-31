package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerInnerServiceSMO;
import com.java110.core.smo.room.IRoomInnerServiceSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.hardwareAdapation.MachineDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
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
 * 查询业主或成员 对应门禁设备，一般是房屋门，单元门 小区大门
 */
@Java110Listener("listOwnerMachines")
public class ListOwnerMachines extends AbstractServiceApiListener {

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "memberId", "请求报文中未包含业主信息");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
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
        }
        machineDto.setLocationObjIds(locationObjIds.toArray(new String[locationObjIds.size()]));
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
        if (machineDtos == null) {
            machineDtos = new ArrayList<>();
        }

        ApiMachineVo apiMachineVo = new ApiMachineVo();
        apiMachineVo.setPage(1);
        apiMachineVo.setRecords(1);
        apiMachineVo.setRows(machineDtos.size());
        apiMachineVo.setMachines(BeanConvertUtil.covertBeanList(machineDtos, ApiMachineDataVo.class));
        ResponseEntity responseEntity = new ResponseEntity(JSONObject.toJSONString(apiMachineVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.LIST_OWNER_MACHINES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IRoomInnerServiceSMO getRoomInnerServiceSMOImpl() {
        return roomInnerServiceSMOImpl;
    }

    public void setRoomInnerServiceSMOImpl(IRoomInnerServiceSMO roomInnerServiceSMOImpl) {
        this.roomInnerServiceSMOImpl = roomInnerServiceSMOImpl;
    }

    public IMachineInnerServiceSMO getMachineInnerServiceSMOImpl() {
        return machineInnerServiceSMOImpl;
    }

    public void setMachineInnerServiceSMOImpl(IMachineInnerServiceSMO machineInnerServiceSMOImpl) {
        this.machineInnerServiceSMOImpl = machineInnerServiceSMOImpl;
    }
}
