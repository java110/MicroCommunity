package com.java110.api.listener.machineRecord;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.common.IMachineRecordInnerServiceSMO;
import com.java110.dto.machine.MachineRecordDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeMachineRecordConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.machineRecord.ApiMachineRecordDataVo;
import com.java110.vo.api.machineRecord.ApiMachineRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listMachineRecordsListener")
public class ListMachineRecordsListener extends AbstractServiceApiListener {

    @Autowired
    private IMachineRecordInnerServiceSMO machineRecordInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineRecordConstant.LIST_MACHINERECORDS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IMachineRecordInnerServiceSMO getMachineRecordInnerServiceSMOImpl() {
        return machineRecordInnerServiceSMOImpl;
    }

    public void setMachineRecordInnerServiceSMOImpl(IMachineRecordInnerServiceSMO machineRecordInnerServiceSMOImpl) {
        this.machineRecordInnerServiceSMOImpl = machineRecordInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        MachineRecordDto machineRecordDto = BeanConvertUtil.covertBean(reqJson, MachineRecordDto.class);

        int count = machineRecordInnerServiceSMOImpl.queryMachineRecordsCount(machineRecordDto);

        List<ApiMachineRecordDataVo> machineRecords = null;

        if (count > 0) {
            machineRecords = BeanConvertUtil.covertBeanList(machineRecordInnerServiceSMOImpl.queryMachineRecords(machineRecordDto), ApiMachineRecordDataVo.class);
        } else {
            machineRecords = new ArrayList<>();
        }

        ApiMachineRecordVo apiMachineRecordVo = new ApiMachineRecordVo();

        apiMachineRecordVo.setTotal(count);
        apiMachineRecordVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMachineRecordVo.setMachineRecords(machineRecords);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMachineRecordVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
