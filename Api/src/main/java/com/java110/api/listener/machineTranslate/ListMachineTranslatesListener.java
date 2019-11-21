package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.hardwareAdapation.IMachineTranslateInnerServiceSMO;
import com.java110.dto.hardwareAdapation.MachineTranslateDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.machineTranslate.ApiMachineTranslateDataVo;
import com.java110.vo.api.machineTranslate.ApiMachineTranslateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listMachineTranslatesListener")
public class ListMachineTranslatesListener extends AbstractServiceApiListener {

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineTranslateConstant.LIST_MACHINETRANSLATES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IMachineTranslateInnerServiceSMO getMachineTranslateInnerServiceSMOImpl() {
        return machineTranslateInnerServiceSMOImpl;
    }

    public void setMachineTranslateInnerServiceSMOImpl(IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl) {
        this.machineTranslateInnerServiceSMOImpl = machineTranslateInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.jsonObjectHaveKey(reqJson,"communityId","请求报文中未包含小区信息");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        MachineTranslateDto machineTranslateDto = BeanConvertUtil.covertBean(reqJson, MachineTranslateDto.class);

        int count = machineTranslateInnerServiceSMOImpl.queryMachineTranslatesCount(machineTranslateDto);

        List<ApiMachineTranslateDataVo> machineTranslates = null;

        if (count > 0) {
            machineTranslates = BeanConvertUtil.covertBeanList(machineTranslateInnerServiceSMOImpl.queryMachineTranslates(machineTranslateDto), ApiMachineTranslateDataVo.class);
        } else {
            machineTranslates = new ArrayList<>();
        }

        ApiMachineTranslateVo apiMachineTranslateVo = new ApiMachineTranslateVo();

        apiMachineTranslateVo.setTotal(count);
        apiMachineTranslateVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMachineTranslateVo.setMachineTranslates(machineTranslates);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMachineTranslateVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
