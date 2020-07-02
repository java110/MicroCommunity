package com.java110.api.listener.repair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.community.IRepairTypeUserInnerServiceSMO;
import com.java110.dto.repair.RepairTypeUserDto;
import com.java110.utils.constant.ServiceCodeRepairTypeUserConstant;
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
@Java110Listener("listRepairTypeUsersListener")
public class ListRepairTypeUsersListener extends AbstractServiceApiListener {

    @Autowired
    private IRepairTypeUserInnerServiceSMO repairTypeUserInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeRepairTypeUserConstant.LIST_REPAIRTYPEUSERS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IRepairTypeUserInnerServiceSMO getRepairTypeUserInnerServiceSMOImpl() {
        return repairTypeUserInnerServiceSMOImpl;
    }

    public void setRepairTypeUserInnerServiceSMOImpl(IRepairTypeUserInnerServiceSMO repairTypeUserInnerServiceSMOImpl) {
        this.repairTypeUserInnerServiceSMOImpl = repairTypeUserInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        RepairTypeUserDto repairTypeUserDto = BeanConvertUtil.covertBean(reqJson, RepairTypeUserDto.class);

        int count = repairTypeUserInnerServiceSMOImpl.queryRepairTypeUsersCount(repairTypeUserDto);

        List<RepairTypeUserDto> repairTypeUserDtos = null;

        if (count > 0) {
            repairTypeUserDtos = repairTypeUserInnerServiceSMOImpl.queryRepairTypeUsers(repairTypeUserDto);
        } else {
            repairTypeUserDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, repairTypeUserDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
