package com.java110.report.cmd.reportInfoAnswer;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.reportInfoAnswer.ReportInfoAnswerDto;
import com.java110.intf.report.IReportInfoAnswerInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "/reportInfoAnswer/queryReportInfoAnswerByOpenId")
public class QueryReportInfoAnswerByOpenId extends Cmd {

    @Autowired
    private IReportInfoAnswerInnerServiceSMO reportInfoAnswerInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "openId", "请求报文中未包含openId");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ReportInfoAnswerDto reportInfoAnswerDto = new ReportInfoAnswerDto();
        reportInfoAnswerDto.setOpenId(reqJson.getString("openId"));
        reportInfoAnswerDto.setPage(1);
        reportInfoAnswerDto.setRow(1);
        List<ReportInfoAnswerDto> reportInfoAnswerDtos = reportInfoAnswerInnerServiceSMOImpl.queryReportInfoAnswers(reportInfoAnswerDto);

        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(reportInfoAnswerDtos));
    }
}
