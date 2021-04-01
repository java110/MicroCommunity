package com.java110.api.listener.ownerCarAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.user.IOwnerCarAttrInnerServiceSMO;
import com.java110.dto.ownerCarAttr.OwnerCarAttrDto;
import com.java110.utils.constant.ServiceCodeOwnerCarAttrConstant;
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
@Java110Listener("listOwnerCarAttrsListener")
public class ListOwnerCarAttrsListener extends AbstractServiceApiListener {

    @Autowired
    private IOwnerCarAttrInnerServiceSMO ownerCarAttrInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeOwnerCarAttrConstant.LIST_OWNERCARATTRS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IOwnerCarAttrInnerServiceSMO getOwnerCarAttrInnerServiceSMOImpl() {
        return ownerCarAttrInnerServiceSMOImpl;
    }

    public void setOwnerCarAttrInnerServiceSMOImpl(IOwnerCarAttrInnerServiceSMO ownerCarAttrInnerServiceSMOImpl) {
        this.ownerCarAttrInnerServiceSMOImpl = ownerCarAttrInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        OwnerCarAttrDto ownerCarAttrDto = BeanConvertUtil.covertBean(reqJson, OwnerCarAttrDto.class);

        int count = ownerCarAttrInnerServiceSMOImpl.queryOwnerCarAttrsCount(ownerCarAttrDto);

        List<OwnerCarAttrDto> ownerCarAttrDtos = null;

        if (count > 0) {
            ownerCarAttrDtos = ownerCarAttrInnerServiceSMOImpl.queryOwnerCarAttrs(ownerCarAttrDto);
        } else {
            ownerCarAttrDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, ownerCarAttrDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
