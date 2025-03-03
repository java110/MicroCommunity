package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
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
 * 查询押金
 */
@Java110Cmd(serviceCode = "fee.queryAdminFeeDeposit")
public class QueryAdminFeeDepositCmd extends Cmd {

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO feeDetailV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        FeeDetailDto feeDetailDto = BeanConvertUtil.covertBean(reqJson, FeeDetailDto.class);

        int total = feeDetailV1InnerServiceSMOImpl.queryFeeDepositCount(feeDetailDto);
        List<FeeDetailDto> feeDetailDtos = null;
        if (total > 0) {
            feeDetailDtos = feeDetailV1InnerServiceSMOImpl.queryFeeDeposit(BeanConvertUtil.covertBean(reqJson, FeeDetailDto.class));
        } else {
            feeDetailDtos = new ArrayList<>();
        }

        int row = reqJson.getInteger("row");
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, feeDetailDtos);
        context.setResponseEntity(responseEntity);
    }
}
