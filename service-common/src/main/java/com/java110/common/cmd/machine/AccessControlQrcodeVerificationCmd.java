package com.java110.common.cmd.machine;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.accessControl.AccessControlWhiteAuthDto;
import com.java110.dto.community.CommunityLocationDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IAccessControlWhiteAuthV1InnerServiceSMO;
import com.java110.intf.common.IMachineV1InnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;


@Java110CmdDoc(title = "门禁二维码核验接口",
        description = "主要用于门禁机刷二维码时调用该接口核验,<br/>" +
                "请求其他接口时 头信息中需要加 Authorization: Bearer token ，<br/>" +
                "token 是这个接口返回的内容<br/> " +
                "会话保持为2小时，请快要到2小时时，再次登录，保持会话</br>",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/machine.accessControlQrcodeVerification",
        resource = "commonDoc",
        author = "吴学文",
        serviceCode = "machine.accessControlQrcodeVerification",
        seq = 1
)

@Java110ParamsDoc(
        headers = {
                @Java110HeaderDoc(name = "APP-ID", defaultValue = "通过dev账户分配应用", description = "应用APP-ID"),
                @Java110HeaderDoc(name = "TRANSACTION-ID", defaultValue = "uuid", description = "交易流水号"),
                @Java110HeaderDoc(name = "REQ-TIME", defaultValue = "20220917120915", description = "请求时间 YYYYMMDDhhmmss"),
                @Java110HeaderDoc(name = "JAVA110-LANG", defaultValue = "zh-cn", description = "语言中文"),
                @Java110HeaderDoc(name = "USER-ID", defaultValue = "-1", description = "调用用户ID 一般写-1"),
        },
        params = {
                @Java110ParamDoc(name = "qrCode", length = 30, remark = "二维码"),
                @Java110ParamDoc(name = "machineCode", length = 30, remark = "设备编码"),
        })

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data", name = "userName", type = "String", remark = "用户名称"),
                @Java110ParamDoc(parentNodeName = "data", name = "openDoor", type = "boolean", remark = "开门 true 开门 false 不开门"),
        }
)

@Java110ExampleDoc(
        reqBody = "{'qrCode':'wuxw','machineCode':'admin'}",
        resBody = "{'code':0,'msg':'成功','data':{'userName':'123123','openDoor':true}}"
)
/**
 * 门禁二维码核验接口
 */
@Java110Cmd(serviceCode = "machine.accessControlQrcodeVerification")
public class AccessControlQrcodeVerificationCmd extends Cmd {

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IMachineV1InnerServiceSMO machineV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;


    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;


    @Autowired
    private IAccessControlWhiteAuthV1InnerServiceSMO accessControlWhiteAuthV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "qrCode", "未包含二维码信息");
        Assert.hasKeyAndValue(reqJson, "machineCode", "未包含设备编码");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        JSONObject data = new JSONObject();
        //todo 查询设备信息
        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(reqJson.getString("machineCode"));
        List<MachineDto> machineDtos = machineV1InnerServiceSMOImpl.queryMachines(machineDto);
        if (machineDtos == null || machineDtos.size() < 1) {
            throw new IllegalArgumentException("设备不存在");
        }

        String userId = userV1InnerServiceSMOImpl.getUserIdByQrCode(reqJson.getString("qrCode"));

        Assert.hasLength(userId, "二维码错误");

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");

        data.put("userName", userDtos.get(0).getName());

        boolean openDoor = false;
        //todo 判断是否为业主
        openDoor = hasOwnerRole(machineDtos.get(0), userDtos.get(0));
        if (openDoor) {
            data.put("openDoor", openDoor);
            context.setResponseEntity(ResultVo.createResponseEntity(data));
            return;
        }


        AccessControlWhiteAuthDto accessControlWhiteAuthDto = new AccessControlWhiteAuthDto();
        accessControlWhiteAuthDto.setMachineId(machineDtos.get(0).getMachineId());
        accessControlWhiteAuthDto.setPersonId(userDtos.get(0).getUserId());

        int count = accessControlWhiteAuthV1InnerServiceSMOImpl.queryAccessControlWhiteAuthsCount(accessControlWhiteAuthDto);

        if (count > 0) {
            openDoor = true;
        }
        data.put("openDoor", openDoor);
        context.setResponseEntity(ResultVo.createResponseEntity(data));
    }

    /**
     * 判断是否是业主角色
     *
     * @param machineDto
     * @param userDto
     * @return
     */
    private boolean hasOwnerRole(MachineDto machineDto, UserDto userDto) {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(userDto.getTel());
        ownerDto.setCommunityId(machineDto.getMachineCode());
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if (ownerDtos == null || ownerDtos.size() < 1) {//todo 是业主
            return false;
        }

        //在小区位置
        if (CommunityLocationDto.LOCAL_TYPE_COMMUNITY.equals(machineDto.getLocationType())) {
            return true;
        }

        //todo 在单元门位置
        if (!CommunityLocationDto.LOCAL_TYPE_UNIT.equals(machineDto.getMachineTypeCd())) {
            return false;
        }


        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(ownerDtos.get(0).getOwnerId());
        ownerRoomRelDto.setCommunityId(ownerDtos.get(0).getCommunityId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
            return false;
        }
        RoomDto roomDto = null;
        int count = 0;
        for (OwnerRoomRelDto tmpOwnerRoomRelDto : ownerRoomRelDtos) {
            roomDto = new RoomDto();
            roomDto.setRoomId(tmpOwnerRoomRelDto.getRoomId());
            roomDto.setUnitId(machineDto.getLocationObjId());
            count = roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
            if (count > 0) {
                return true;
            }
        }
        return false;
    }
}
