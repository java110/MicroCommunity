package com.java110.common.cmd.machineAuth;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.machine.MachineAuthDto;
import com.java110.intf.common.IMachineAuthInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.machineAuth.ApiMachineAuthDataVo;
import com.java110.vo.api.machineAuth.ApiMachineAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "machineAuth.listMachineAuths")
public class ListMachineAuths extends Cmd {

    @Autowired
    private IMachineAuthInnerServiceSMO machineAuthInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        MachineAuthDto machineAuthDto = BeanConvertUtil.covertBean(reqJson, MachineAuthDto.class);
        int count = machineAuthInnerServiceSMOImpl.queryMachineAuthsCount(machineAuthDto);
        List<ApiMachineAuthDataVo> machineAuths = null;
        if (count > 0) {
            machineAuths = BeanConvertUtil.covertBeanList(machineAuthInnerServiceSMOImpl.queryMachineAuths(machineAuthDto), ApiMachineAuthDataVo.class);
        } else {
            machineAuths = new ArrayList<>();
        }
        ApiMachineAuthVo apiMachineAuthVo = new ApiMachineAuthVo();
        apiMachineAuthVo.setTotal(count);
        apiMachineAuthVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMachineAuthVo.setMachineAuths(machineAuths);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMachineAuthVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
