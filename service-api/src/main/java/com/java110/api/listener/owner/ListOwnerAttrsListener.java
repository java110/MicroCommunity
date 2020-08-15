package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.user.IOwnerAttrInnerServiceSMO;
import com.java110.dto.owner.OwnerAttrDto;
import com.java110.utils.constant.ServiceCodeOwnerAttrConstant;
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
@Java110Listener("listOwnerAttrsListener")
public class ListOwnerAttrsListener extends AbstractServiceApiListener {

    @Autowired
    private IOwnerAttrInnerServiceSMO ownerAttrInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeOwnerAttrConstant.LIST_OWNERATTRS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IOwnerAttrInnerServiceSMO getOwnerAttrInnerServiceSMOImpl() {
        return ownerAttrInnerServiceSMOImpl;
    }

    public void setOwnerAttrInnerServiceSMOImpl(IOwnerAttrInnerServiceSMO ownerAttrInnerServiceSMOImpl) {
        this.ownerAttrInnerServiceSMOImpl = ownerAttrInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        OwnerAttrDto ownerAttrDto = BeanConvertUtil.covertBean(reqJson, OwnerAttrDto.class);

        int count = ownerAttrInnerServiceSMOImpl.queryOwnerAttrsCount(ownerAttrDto);

        List<OwnerAttrDto> ownerAttrDtos = null;

        if (count > 0) {
            ownerAttrDtos = ownerAttrInnerServiceSMOImpl.queryOwnerAttrs(ownerAttrDto);
        } else {
            ownerAttrDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, ownerAttrDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
