package com.java110.report.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
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
 * 查询费用变更记录
 */
@Java110Cmd(serviceCode = "fee.queryHisFee")
public class QueryHisFeeCmd extends Cmd {

    @Autowired
    private IReportCommunityInnerServiceSMO reportCommunityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson,"feeId","未包含费用ID");
        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        int row = reqJson.getInteger("row");
        FeeDto feeDto = BeanConvertUtil.covertBean(reqJson, FeeDto.class);

        int total = reportCommunityInnerServiceSMOImpl.queryHisFeeCount(feeDto);
//        int count = 0;
        List<FeeDto> feeDtos = null;
        if (total > 0) {
            feeDtos = reportCommunityInnerServiceSMOImpl.queryHisFees(feeDto);
        } else {
            feeDtos = new ArrayList<>();
        }

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, feeDtos);
        context.setResponseEntity(responseEntity);
    }
}
