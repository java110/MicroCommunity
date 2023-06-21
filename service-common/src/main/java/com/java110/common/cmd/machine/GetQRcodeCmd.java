package com.java110.common.cmd.machine;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineV1InnerServiceSMO;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

/**
 * 设备 二维码生成
 */
@Java110Cmd(serviceCode = "/machine/getQRcode")
public class GetQRcodeCmd extends Cmd {

    @Autowired
    IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;
    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含用户信息");
        Assert.hasKeyAndValue(reqJson, "machineCode", "请求报文中未包含设备信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        ResponseEntity<String> responseEntity = null;

        //todo 如果是业主 限制开门次数
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("userId"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        if (ownerDtos == null || ownerDtos.size() < 1) {
            throw new CmdException("没有权限开门");
        }

        //todo 校验设备到底在不在，不在就报错！！！
        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(reqJson.getString("machineCode"));
        machineDto.setCommunityId(reqJson.getString("communityId"));
        List<MachineDto> machineDtos = machineV1InnerServiceSMOImpl.queryMachines(machineDto);
        Assert.listOnlyOne(machineDtos, "设备不存在");

        // todo 如果海康 调用海康平台，不然自己生成。
        if (MachineDto.HM_HK.equals(machineDtos.get(0).getHmId())) {
            ResultVo resultVo = dataBusInnerServiceSMOImpl.getQRcode(reqJson);
            responseEntity = ResultVo.createResponseEntity(resultVo.getCode(), resultVo.getMsg(), resultVo.getData());
            context.setResponseEntity(responseEntity);
            return;
        }

        //todo 调用自己算法生成 haha
        UserDto userDto = new UserDto();
        userDto.setTel(ownerDtos.get(0).getLink());
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        String qrCode = "";
        //todo 理论上这种 走不到 因为业主在手机端展示二维码，所以业主肯定认证过了
        if (userDtos == null || userDtos.size() < 1) {
            qrCode = userV1InnerServiceSMOImpl.generatorUserIdQrCode(reqJson.getString("userId"));
        } else {
            qrCode = userV1InnerServiceSMOImpl.generatorUserIdQrCode(userDtos.get(0).getUserId());
        }

        context.setResponseEntity(ResultVo.createResponseEntity(qrCode));
    }
}
