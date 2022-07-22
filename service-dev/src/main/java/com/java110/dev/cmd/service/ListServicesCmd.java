package com.java110.dev.cmd.service;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.service.ServiceDto;
import com.java110.intf.community.IServiceInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.service.ApiServiceDataVo;
import com.java110.vo.api.service.ApiServiceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "service.listServices")
public class ListServicesCmd extends Cmd {


    @Autowired
    private IServiceInnerServiceSMO serviceInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        ServiceDto serviceDto = BeanConvertUtil.covertBean(reqJson, ServiceDto.class);

        int count = serviceInnerServiceSMOImpl.queryServicesCount(serviceDto);

        List<ApiServiceDataVo> services = null;

        if (count > 0) {
            services = BeanConvertUtil.covertBeanList(serviceInnerServiceSMOImpl.queryServices(serviceDto), ApiServiceDataVo.class);
        } else {
            services = new ArrayList<>();
        }

        ApiServiceVo apiServiceVo = new ApiServiceVo();

        apiServiceVo.setTotal(count);
        apiServiceVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiServiceVo.setServices(services);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiServiceVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
