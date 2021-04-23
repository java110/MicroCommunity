package com.java110.api.listener.machine;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.common.IMachineAuthInnerServiceSMO;
import com.java110.dto.machineAuth.MachineAuthDto;
import com.java110.utils.constant.ServiceCodeMachineAuthConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listMachineAuthsListener")
public class ListMachineAuthsListener extends AbstractServiceApiListener {

    @Autowired
    private IMachineAuthInnerServiceSMO machineAuthInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineAuthConstant.LIST_MACHINEAUTHS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IMachineAuthInnerServiceSMO getMachineAuthInnerServiceSMOImpl() {
        return machineAuthInnerServiceSMOImpl;
    }

    public void setMachineAuthInnerServiceSMOImpl(IMachineAuthInnerServiceSMO machineAuthInnerServiceSMOImpl) {
        this.machineAuthInnerServiceSMOImpl = machineAuthInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        MachineAuthDto machineAuthDto = BeanConvertUtil.covertBean(reqJson, MachineAuthDto.class);

        int count = machineAuthInnerServiceSMOImpl.queryMachineAuthsCount(machineAuthDto);

        List<MachineAuthDto> machineAuthDtos = null;

        if (count > 0) {
            machineAuthDtos = machineAuthInnerServiceSMOImpl.queryMachineAuths(machineAuthDto);
        } else {
            machineAuthDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, machineAuthDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
