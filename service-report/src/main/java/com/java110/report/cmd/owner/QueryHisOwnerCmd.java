package com.java110.report.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerCarDto;
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
 * 查询业主变更记录
 */
@Java110Cmd(serviceCode = "owner.queryHisOwner")
public class QueryHisOwnerCmd extends Cmd {

    @Autowired
    private IReportCommunityInnerServiceSMO reportCommunityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson,"memberId","未包含业主ID");
        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        int row = reqJson.getInteger("row");
        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);

        int total = reportCommunityInnerServiceSMOImpl.queryHisOwnerCount(ownerDto);
//        int count = 0;
        List<OwnerDto> ownerDtos = null;
        if (total > 0) {
            ownerDtos = reportCommunityInnerServiceSMOImpl.queryHisOwners(ownerDto);
        } else {
            ownerDtos = new ArrayList<>();
        }

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, ownerDtos);
        context.setResponseEntity(responseEntity);
    }
}
