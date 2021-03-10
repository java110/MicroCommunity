package com.java110.api.listener.floor;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.community.IFloorAttrInnerServiceSMO;
import com.java110.dto.floorAttr.FloorAttrDto;
import com.java110.utils.constant.ServiceCodeFloorAttrConstant;
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
@Java110Listener("listFloorAttrsListener")
public class ListFloorAttrsListener extends AbstractServiceApiListener {

    @Autowired
    private IFloorAttrInnerServiceSMO floorAttrInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeFloorAttrConstant.LIST_FLOORATTRS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IFloorAttrInnerServiceSMO getFloorAttrInnerServiceSMOImpl() {
        return floorAttrInnerServiceSMOImpl;
    }

    public void setFloorAttrInnerServiceSMOImpl(IFloorAttrInnerServiceSMO floorAttrInnerServiceSMOImpl) {
        this.floorAttrInnerServiceSMOImpl = floorAttrInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        FloorAttrDto floorAttrDto = BeanConvertUtil.covertBean(reqJson, FloorAttrDto.class);

        int count = floorAttrInnerServiceSMOImpl.queryFloorAttrsCount(floorAttrDto);

        List<FloorAttrDto> floorAttrDtos = null;

        if (count > 0) {
            floorAttrDtos = floorAttrInnerServiceSMOImpl.queryFloorAttrs(floorAttrDto);
        } else {
            floorAttrDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, floorAttrDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
