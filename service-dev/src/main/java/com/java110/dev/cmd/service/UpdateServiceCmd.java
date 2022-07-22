package com.java110.dev.cmd.service;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.service.ServiceDto;
import com.java110.intf.community.IServiceInnerServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Java110Cmd(serviceCode = "service.updateService")
public class UpdateServiceCmd extends Cmd {

    @Autowired
    private IServiceInnerServiceSMO serviceInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "serviceId", "服务ID不能为空");
        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写服务名称");
        Assert.hasKeyAndValue(reqJson, "serviceCode", "必填，请填写服务编码如 service.saveService");
        Assert.hasKeyAndValue(reqJson, "businessTypeCd", "可填，请填写秘钥，如果填写了需要加密传输");
        Assert.hasKeyAndValue(reqJson, "seq", "必填，请填写序列");
        Assert.hasKeyAndValue(reqJson, "isInstance", "可填，请填写实例 Y 或N");
        Assert.hasKeyAndValue(reqJson, "method", "必填，请填写调用方式");
        Assert.hasKeyAndValue(reqJson, "timeout", "必填，请填写超时时间");
        Assert.hasKeyAndValue(reqJson, "retryCount", "必填，请填写重试次数");
        Assert.hasKeyAndValue(reqJson, "provideAppId", "必填，请填写提供服务");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ServiceDto serviceDto = BeanConvertUtil.covertBean(reqJson, ServiceDto.class);


        int count = serviceInnerServiceSMOImpl.updateService(serviceDto);


        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "修改数据失败");
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
