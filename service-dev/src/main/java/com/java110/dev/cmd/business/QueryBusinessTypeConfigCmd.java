package com.java110.dev.cmd.business;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.businesstype.CbusinesstypeDto;
import com.java110.intf.store.ICbusinesstypeInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "business.queryBusinessTypeConfig")
public class QueryBusinessTypeConfigCmd extends Cmd {

    @Autowired
    private ICbusinesstypeInnerServiceSMO iCbusinesstypeInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        //validateDemoConfigData(reqJson);

        List<CbusinesstypeDto> cbusinesstypeDto = iCbusinesstypeInnerServiceSMOImpl.queryCbusinesstypes(BeanConvertUtil.covertBean(reqJson, CbusinesstypeDto.class));
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(cbusinesstypeDto), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
