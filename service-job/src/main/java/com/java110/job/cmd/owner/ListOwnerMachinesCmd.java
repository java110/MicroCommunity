package com.java110.job.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.room.RoomDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.unit.FloorAndUnitDto;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.job.adapt.hcIotNew.http.ISendIot;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
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
    private ISendIot sendIotImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "memberId", "请求报文中未包含业主信息");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        super.validatePageInfo(reqJson);
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

        reqJson.put("link",ownerDtos.get(0).getLink());
        reqJson.put("iotApiCode","listOwnerAccessControlBmoImpl");

        ResultVo resultVo = sendIotImpl.post("/iot/api/common.openCommonApi", reqJson);

        if (resultVo.getCode() != ResultVo.CODE_OK) {
            throw new CmdException(resultVo.getMsg());
        }

        context.setResponseEntity(ResultVo.createResponseEntity(resultVo));

    }


}
