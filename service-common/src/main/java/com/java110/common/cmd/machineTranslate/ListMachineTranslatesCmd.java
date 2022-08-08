package com.java110.common.cmd.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.machineTranslate.ApiMachineTranslateDataVo;
import com.java110.vo.api.machineTranslate.ApiMachineTranslateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "machineTranslate.listMachineTranslates")
public class ListMachineTranslatesCmd extends Cmd {

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.jsonObjectHaveKey(reqJson,"communityId","请求报文中未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        MachineTranslateDto machineTranslateDto = BeanConvertUtil.covertBean(reqJson, MachineTranslateDto.class);

        int count = machineTranslateInnerServiceSMOImpl.queryMachineTranslatesCount(machineTranslateDto);

        List<ApiMachineTranslateDataVo> machineTranslates = null;

        if (count > 0) {
            machineTranslates = BeanConvertUtil.covertBeanList(machineTranslateInnerServiceSMOImpl.queryMachineTranslates(machineTranslateDto), ApiMachineTranslateDataVo.class);
        } else {
            machineTranslates = new ArrayList<>();
        }

        ApiMachineTranslateVo apiMachineTranslateVo = new ApiMachineTranslateVo();

        apiMachineTranslateVo.setTotal(count);
        apiMachineTranslateVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMachineTranslateVo.setMachineTranslates(machineTranslates);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMachineTranslateVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
