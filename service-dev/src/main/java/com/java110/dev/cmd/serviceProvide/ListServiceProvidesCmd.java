package com.java110.dev.cmd.serviceProvide;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.service.ServiceProvideDto;
import com.java110.intf.community.IServiceInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.serviceProvide.ApiServiceProvideDataVo;
import com.java110.vo.api.serviceProvide.ApiServiceProvideVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "serviceProvide.listServiceProvides")
public class ListServiceProvidesCmd extends Cmd {

    @Autowired
    private IServiceInnerServiceSMO serviceInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ServiceProvideDto serviceProvideDto = BeanConvertUtil.covertBean(reqJson, ServiceProvideDto.class);

        int count = serviceInnerServiceSMOImpl.queryServiceProvidesCount(serviceProvideDto);

        List<ApiServiceProvideDataVo> serviceProvides = null;

        if (count > 0) {
            serviceProvides = BeanConvertUtil.covertBeanList(serviceInnerServiceSMOImpl.queryServiceProvides(serviceProvideDto), ApiServiceProvideDataVo.class);
        } else {
            serviceProvides = new ArrayList<>();
        }

        ApiServiceProvideVo apiServiceProvideVo = new ApiServiceProvideVo();

        apiServiceProvideVo.setTotal(count);
        apiServiceProvideVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiServiceProvideVo.setServiceProvides(serviceProvides);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiServiceProvideVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
