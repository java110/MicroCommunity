package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.importData.ImportFeeDetailDto;
import com.java110.intf.fee.IImportFeeDetailInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "fee.queryAdminImportFeeDetail")
public class QueryAdminImportFeeDetailCmd extends Cmd {

    @Autowired
    private IImportFeeDetailInnerServiceSMO importFeeDetailInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validateAdmin(context);
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        ImportFeeDetailDto importFeeDetailDto = BeanConvertUtil.covertBean(reqJson, ImportFeeDetailDto.class);
        int count = importFeeDetailInnerServiceSMOImpl.queryImportFeeDetailsCount(importFeeDetailDto);

        List<ImportFeeDetailDto> importFeeDetailDtos = null;
        if (count > 0) {
            importFeeDetailDtos = importFeeDetailInnerServiceSMOImpl.queryImportFeeDetails(importFeeDetailDto);
        } else {
            importFeeDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) importFeeDetailDto.getRow()), count, importFeeDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
