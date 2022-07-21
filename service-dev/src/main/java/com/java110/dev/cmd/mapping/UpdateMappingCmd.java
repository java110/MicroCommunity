package com.java110.dev.cmd.mapping;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.mapping.MappingDto;
import com.java110.intf.community.IMappingInnerServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Java110Cmd(serviceCode = "mapping.updateMapping")
public class UpdateMappingCmd extends Cmd {

    @Autowired
    private IMappingInnerServiceSMO mappingInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "id", "编码ID不能为空");
        Assert.hasKeyAndValue(reqJson, "domain", "必填，请填写域");
        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写名称");
        Assert.hasKeyAndValue(reqJson, "key", "必填，请填写键");
        Assert.hasKeyAndValue(reqJson, "value", "必填，请填写值");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        MappingDto mappingDto = BeanConvertUtil.covertBean(reqJson, MappingDto.class);

        int count = mappingInnerServiceSMOImpl.updateMapping(mappingDto);


        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "编辑数据失败");
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
