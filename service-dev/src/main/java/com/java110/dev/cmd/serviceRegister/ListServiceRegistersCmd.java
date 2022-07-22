package com.java110.dev.cmd.serviceRegister;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.service.RouteDto;
import com.java110.intf.community.IRouteInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.serviceRegister.ApiServiceRegisterDataVo;
import com.java110.vo.api.serviceRegister.ApiServiceRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "serviceRegister.listServiceRegisters")
public class ListServiceRegistersCmd extends Cmd {

    @Autowired
    private IRouteInnerServiceSMO routeInnerServiceSMOIMpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        RouteDto routeDto = BeanConvertUtil.covertBean(reqJson, RouteDto.class);

        int count = routeInnerServiceSMOIMpl.queryRoutesCount(routeDto);

        List<ApiServiceRegisterDataVo> serviceRegisters = null;

        if (count > 0) {
            serviceRegisters = BeanConvertUtil.covertBeanList(routeInnerServiceSMOIMpl.queryRoutes(routeDto), ApiServiceRegisterDataVo.class);
        } else {
            serviceRegisters = new ArrayList<>();
        }

        ApiServiceRegisterVo apiServiceRegisterVo = new ApiServiceRegisterVo();

        apiServiceRegisterVo.setTotal(count);
        apiServiceRegisterVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiServiceRegisterVo.setServiceRegisters(serviceRegisters);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiServiceRegisterVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
