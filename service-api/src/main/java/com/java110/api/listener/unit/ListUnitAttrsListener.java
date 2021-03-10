package com.java110.api.listener.unit;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.community.IUnitAttrInnerServiceSMO;
import com.java110.dto.unitAttr.UnitAttrDto;
import com.java110.utils.constant.ServiceCodeUnitAttrConstant;
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
@Java110Listener("listUnitAttrsListener")
public class ListUnitAttrsListener extends AbstractServiceApiListener {

    @Autowired
    private IUnitAttrInnerServiceSMO unitAttrInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeUnitAttrConstant.LIST_UNITATTRS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IUnitAttrInnerServiceSMO getUnitAttrInnerServiceSMOImpl() {
        return unitAttrInnerServiceSMOImpl;
    }

    public void setUnitAttrInnerServiceSMOImpl(IUnitAttrInnerServiceSMO unitAttrInnerServiceSMOImpl) {
        this.unitAttrInnerServiceSMOImpl = unitAttrInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        UnitAttrDto unitAttrDto = BeanConvertUtil.covertBean(reqJson, UnitAttrDto.class);

        int count = unitAttrInnerServiceSMOImpl.queryUnitAttrsCount(unitAttrDto);

        List<UnitAttrDto> unitAttrDtos = null;

        if (count > 0) {
            unitAttrDtos = unitAttrInnerServiceSMOImpl.queryUnitAttrs(unitAttrDto);
        } else {
            unitAttrDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, unitAttrDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
