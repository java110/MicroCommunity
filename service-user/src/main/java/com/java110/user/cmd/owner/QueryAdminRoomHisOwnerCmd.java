package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "owner.queryAdminRoomHisOwner")
public class QueryAdminRoomHisOwnerCmd extends Cmd {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
        super.validateAdmin(context);
        Assert.hasKeyAndValue(reqJson, "roomId", "未包含房屋编号");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);
        int count = ownerInnerServiceSMOImpl.queryOwnerLogsCountByRoom(ownerDto);

        List<OwnerDto> ownerDtos = null;
        if (count > 0) {
            ownerDtos = ownerInnerServiceSMOImpl.queryOwnerLogsByRoom(ownerDto);
        } else {
            ownerDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) ownerDto.getRow()), count, ownerDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
