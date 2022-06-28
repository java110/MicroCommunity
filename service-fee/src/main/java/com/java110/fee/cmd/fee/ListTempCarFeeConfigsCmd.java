package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigDto;
import com.java110.intf.fee.ITempCarFeeConfigInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "fee.listTempCarFeeConfigs")
public class ListTempCarFeeConfigsCmd extends Cmd {


    @Autowired
    private ITempCarFeeConfigInnerServiceSMO tempCarFeeConfigInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        TempCarFeeConfigDto tempCarFeeConfigDto = BeanConvertUtil.covertBean(reqJson, TempCarFeeConfigDto.class);

        int count = tempCarFeeConfigInnerServiceSMOImpl.queryTempCarFeeConfigsCount(tempCarFeeConfigDto);

        List<TempCarFeeConfigDto> tempCarFeeConfigDtos = null;

        if (count > 0) {
            tempCarFeeConfigDtos = tempCarFeeConfigInnerServiceSMOImpl.queryTempCarFeeConfigs(tempCarFeeConfigDto);
        } else {
            tempCarFeeConfigDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, tempCarFeeConfigDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
