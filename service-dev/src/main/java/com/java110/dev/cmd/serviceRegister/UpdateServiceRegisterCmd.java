package com.java110.dev.cmd.serviceRegister;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.service.RouteDto;
import com.java110.intf.community.IRouteInnerServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Java110Cmd(serviceCode = "serviceRegister.updateServiceRegister")
public class UpdateServiceRegisterCmd extends Cmd {
    @Autowired
    private IRouteInnerServiceSMO routeInnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "id", "绑定ID不能为空");
        Assert.hasKeyAndValue(reqJson, "appId", "必填，请填写应用ID");
        Assert.hasKeyAndValue(reqJson, "serviceId", "必填，请填写服务ID");
        Assert.hasKeyAndValue(reqJson, "orderTypeCd", "必填，请填写订单类型");
        Assert.hasKeyAndValue(reqJson, "invokeLimitTimes", "必填，请填写调用次数");
        Assert.hasKeyAndValue(reqJson, "invokeModel", "可填，请填写消息队列，订单在异步调用时使用");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        RouteDto routeDto = BeanConvertUtil.covertBean(reqJson, RouteDto.class);


        int count = routeInnerServiceSMOImpl.updateRoute(routeDto);


        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "修改数据失败");
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
