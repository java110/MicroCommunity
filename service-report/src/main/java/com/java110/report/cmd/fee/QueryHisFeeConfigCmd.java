package com.java110.report.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.intf.report.IReportCommunityInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询收费项目 变更记录
 */
@Java110Cmd(serviceCode = "fee.queryHisFeeConfig")
public class QueryHisFeeConfigCmd extends Cmd {

    @Autowired
    private IReportCommunityInnerServiceSMO reportCommunityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {


        int row = reqJson.getInteger("row");
        FeeConfigDto feeDto = BeanConvertUtil.covertBean(reqJson, FeeConfigDto.class);

        int total = reportCommunityInnerServiceSMOImpl.queryHisFeeConfigCount(feeDto);
//        int count = 0;
        List<FeeConfigDto> feeConfigDtos = null;
        if (total > 0) {
            feeConfigDtos = reportCommunityInnerServiceSMOImpl.queryHisFeeConfigs(feeDto);
        } else {
            feeConfigDtos = new ArrayList<>();
        }

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, feeConfigDtos);
        context.setResponseEntity(responseEntity);
    }
}
