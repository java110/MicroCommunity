package com.java110.community.cmd.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.IotDataDto;
import com.java110.intf.job.IIotInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

@Java110Cmd(serviceCode = "visit.queryVisitQrCode")
public class QueryVisitQrCodeCmd extends Cmd {

    @Autowired
    private IIotInnerServiceSMO iotInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {


        ResultVo resultVo = iotInnerServiceSMOImpl.postIotData(new IotDataDto("getVisitQrCodeUrlBmoImpl", reqJson));
        context.setResponseEntity(ResultVo.createResponseEntity(resultVo));
    }
}
