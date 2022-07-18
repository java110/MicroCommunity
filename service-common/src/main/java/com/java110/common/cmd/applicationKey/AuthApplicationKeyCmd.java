package com.java110.common.cmd.applicationKey;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.machine.ApplicationKeyDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.unit.FloorAndUnitDto;
import com.java110.intf.common.IApplicationKeyInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineRecordV1InnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.po.machine.MachineRecordPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "applicationKey.authApplicationKeys")
public class AuthApplicationKeyCmd extends Cmd {

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;


    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IMachineRecordV1InnerServiceSMO machineRecordV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");
        Assert.hasKeyAndValue(reqJson, "machineCode", "必填，请填写设备编码");
        Assert.hasKeyAndValue(reqJson, "pwd", "必填，请填写密码");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
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

        addMachineRecord(reqParam);

    }

    /**
     * 添加小区信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addMachineRecord(JSONObject paramInJson) {

        //paramInJson.put("fileTime", DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
        paramInJson.put("name", "匿名");
        paramInJson.put("tel", "");
        paramInJson.put("idCard", "");
        paramInJson.put("openTypeCd", "2000");
        paramInJson.put("machineRecordId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineRecordId));
        MachineRecordPo machineRecordPo = BeanConvertUtil.covertBean(paramInJson, MachineRecordPo.class);
        int flag = machineRecordV1InnerServiceSMOImpl.saveMachineRecord(machineRecordPo);
        if (flag < 1) {
            throw new CmdException("保存开门记录失败");
        }
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
}
