package com.java110.api.listener.machine;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.dto.hardwareAdapation.MachineDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeMachineConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.machine.ApiMachineDataVo;
import com.java110.vo.api.machine.ApiMachineVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listMachinesListener")
public class ListMachinesListener extends AbstractServiceApiListener {

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineConstant.LIST_MACHINES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IMachineInnerServiceSMO getMachineInnerServiceSMOImpl() {
        return machineInnerServiceSMOImpl;
    }

    public void setMachineInnerServiceSMOImpl(IMachineInnerServiceSMO machineInnerServiceSMOImpl) {
        this.machineInnerServiceSMOImpl = machineInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        super.validatePageInfo(reqJson);

        Assert.jsonObjectHaveKey(reqJson,"communityId","请求报文中未包含小区信息");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        MachineDto machineDto = BeanConvertUtil.covertBean(reqJson, MachineDto.class);

        int count = machineInnerServiceSMOImpl.queryMachinesCount(machineDto);

        List<ApiMachineDataVo> machines = null;

        if (count > 0) {
            machines = BeanConvertUtil.covertBeanList(machineInnerServiceSMOImpl.queryMachines(machineDto), ApiMachineDataVo.class);
        } else {
            machines = new ArrayList<>();
        }

        ApiMachineVo apiMachineVo = new ApiMachineVo();

        apiMachineVo.setTotal(count);
        apiMachineVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMachineVo.setMachines(machines);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMachineVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
