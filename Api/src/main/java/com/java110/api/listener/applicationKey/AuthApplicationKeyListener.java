package com.java110.api.listener.applicationKey;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.applicationKey.IApplicationKeyBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.floor.IFloorInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IApplicationKeyInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.core.smo.room.IRoomInnerServiceSMO;
import com.java110.core.smo.unit.IUnitInnerServiceSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.hardwareAdapation.ApplicationKeyDto;
import com.java110.dto.hardwareAdapation.MachineDto;
import com.java110.dto.unit.FloorAndUnitDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeApplicationKeyConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 钥匙认证接口
 */
@Java110Listener("authApplicationKeyListener")
public class AuthApplicationKeyListener extends AbstractServiceApiPlusListener {
    private static Logger logger = LoggerFactory.getLogger(AuthApplicationKeyListener.class);

    @Autowired
    private IApplicationKeyBMO applicationKeyBMOImpl;

    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;

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

    @Override
    public String getServiceCode() {
        return ServiceCodeApplicationKeyConstant.AUTH_APPLICATIONKEYS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IApplicationKeyInnerServiceSMO getApplicationKeyInnerServiceSMOImpl() {
        return applicationKeyInnerServiceSMOImpl;
    }

    public void setApplicationKeyInnerServiceSMOImpl(IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl) {
        this.applicationKeyInnerServiceSMOImpl = applicationKeyInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");
        Assert.hasKeyAndValue(reqJson, "machineCode", "必填，请填写设备编码");
        Assert.hasKeyAndValue(reqJson, "pwd", "必填，请填写密码");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


        //1.0 根据 小区ID和 设备编码查询设备ID

        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(reqJson.getString("machineCode"));
        machineDto.setCommunityId(reqJson.getString("communityId"));
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        Assert.listOnlyOne(machineDtos, "根据设备编码为找到设备Id 或找到多条");

        ApplicationKeyDto applicationKeyDto = new ApplicationKeyDto();
        applicationKeyDto.setCommunityId(reqJson.getString("communityId"));
        applicationKeyDto.setMachineId(machineDtos.get(0).getMachineId());
        applicationKeyDto.setPwd(reqJson.getString("pwd"));

        int count = applicationKeyInnerServiceSMOImpl.queryApplicationKeysCount(applicationKeyDto);

        ResponseEntity<String> responseEntity = null;
        JSONObject reqParam = new JSONObject();
        reqParam.put("communityId", reqJson.getString("communityId"));
        reqParam.put("machineId", machineDtos.get(0).getMachineId());
        reqParam.put("machineCode", reqJson.getString("machineCode"));
        if (count > 0) {
            reqParam.put("recordTypeCd", "8888");
            responseEntity = new ResponseEntity<String>("成功", HttpStatus.OK);
        } else {
            reqParam.put("recordTypeCd", "6666");
            responseEntity = new ResponseEntity<String>("认证失败", HttpStatus.UNAUTHORIZED);
        }
        context.setResponseEntity(responseEntity);



        applicationKeyBMOImpl.addMachineRecord(reqParam, context);


        //context.setResponseEntity(responseEntity);

    }


    private void refreshMachines(List<ApplicationKeyDto> applicationKeyDtos) {

        //批量处理 小区
        refreshCommunitys(applicationKeyDtos);

        //批量处理单元信息
        refreshUnits(applicationKeyDtos);

        //批量处理 房屋信息
        refreshRooms(applicationKeyDtos);

    }

    /**
     * 获取批量小区
     *
     * @param applicationKeyDtos 设备信息
     * @return 批量userIds 信息
     */
    private void refreshCommunitys(List<ApplicationKeyDto> applicationKeyDtos) {
        List<String> communityIds = new ArrayList<String>();
        List<ApplicationKeyDto> tmpApplicationKeyDtos = new ArrayList<>();
        for (ApplicationKeyDto applicationKeyDto : applicationKeyDtos) {

            if (!"2000".equals(applicationKeyDto.getLocationTypeCd())
                    && !"3000".equals(applicationKeyDto.getLocationTypeCd())
            ) {
                communityIds.add(applicationKeyDto.getLocationObjId());
                tmpApplicationKeyDtos.add(applicationKeyDto);
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

        for (ApplicationKeyDto applicationKeyDto : tmpApplicationKeyDtos) {
            for (CommunityDto tmpCommunityDto : communityDtos) {
                if (applicationKeyDto.getLocationObjId().equals(tmpCommunityDto.getCommunityId())) {
                    applicationKeyDto.setLocationObjName(tmpCommunityDto.getName() + " " + applicationKeyDto.getLocationTypeName());
                }
            }
        }
    }


    /**
     * 获取批量单元
     *
     * @param applicationKeyDtos 设备信息
     * @return 批量userIds 信息
     */
    private void refreshUnits(List<ApplicationKeyDto> applicationKeyDtos) {
        List<String> unitIds = new ArrayList<String>();
        List<ApplicationKeyDto> tmpApplicationKeyDtos = new ArrayList<>();
        for (ApplicationKeyDto applicationKeyDto : applicationKeyDtos) {

            if ("2000".equals(applicationKeyDto.getLocationTypeCd())) {
                unitIds.add(applicationKeyDto.getLocationObjId());
                tmpApplicationKeyDtos.add(applicationKeyDto);
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

        for (ApplicationKeyDto applicationKeyDto : tmpApplicationKeyDtos) {
            for (FloorAndUnitDto tmpUnitDto : unitDtos) {
                if (applicationKeyDto.getLocationObjId().equals(tmpUnitDto.getUnitId())) {
                    applicationKeyDto.setLocationObjName(tmpUnitDto.getFloorNum() + "栋" + tmpUnitDto.getUnitNum() + "单元");
                    BeanConvertUtil.covertBean(tmpUnitDto, applicationKeyDto);
                }
            }
        }
    }

    /**
     * 获取批量单元
     *
     * @param applicationKeyDtos 设备信息
     * @return 批量userIds 信息
     */
    private void refreshRooms(List<ApplicationKeyDto> applicationKeyDtos) {
        List<String> roomIds = new ArrayList<String>();
        List<ApplicationKeyDto> tmpApplicationKeyDtos = new ArrayList<>();
        for (ApplicationKeyDto applicationKeyDto : applicationKeyDtos) {

            if ("3000".equals(applicationKeyDto.getLocationTypeCd())) {
                roomIds.add(applicationKeyDto.getLocationObjId());
                tmpApplicationKeyDtos.add(applicationKeyDto);
            }
        }
        if (roomIds.size() < 1) {
            return;
        }
        String[] tmpRoomIds = roomIds.toArray(new String[roomIds.size()]);

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(tmpRoomIds);
        roomDto.setCommunityId(applicationKeyDtos.get(0).getCommunityId());
        //根据 userId 查询用户信息
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        for (ApplicationKeyDto applicationKeyDto : tmpApplicationKeyDtos) {
            for (RoomDto tmpRoomDto : roomDtos) {
                if (applicationKeyDto.getLocationObjId().equals(tmpRoomDto.getRoomId())) {
                    applicationKeyDto.setLocationObjName(tmpRoomDto.getFloorNum() + "栋" + tmpRoomDto.getUnitNum() + "单元" + tmpRoomDto.getRoomNum() + "室");
                    BeanConvertUtil.covertBean(tmpRoomDto, applicationKeyDto);
                }
            }
        }
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
