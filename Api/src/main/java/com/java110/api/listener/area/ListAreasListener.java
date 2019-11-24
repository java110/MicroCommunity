package com.java110.api.listener.area;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.app.IAppInnerServiceSMO;
import com.java110.core.smo.common.IAreaInnerServiceSMO;
import com.java110.dto.app.AppDto;
import com.java110.dto.area.AreaDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeAppConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.app.ApiAppDataVo;
import com.java110.vo.api.app.ApiAppVo;
import com.java110.vo.api.area.ApiAreaDataVo;
import com.java110.vo.api.area.ApiAreaVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listAreasListener")
public class ListAreasListener extends AbstractServiceApiListener {

    @Autowired
    private IAreaInnerServiceSMO areaInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.LIST_AREAS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AreaDto areaDto = BeanConvertUtil.covertBean(reqJson, AreaDto.class);

        List<ApiAreaDataVo> areas = BeanConvertUtil.covertBeanList(areaInnerServiceSMOImpl.getArea(areaDto),ApiAreaDataVo.class);


        ApiAreaVo apiAreaVo = new ApiAreaVo();

        apiAreaVo.setTotal(1);
        apiAreaVo.setRecords(1);
        apiAreaVo.setAreas(areas);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiAreaVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    public IAreaInnerServiceSMO getAreaInnerServiceSMOImpl() {
        return areaInnerServiceSMOImpl;
    }

    public void setAreaInnerServiceSMOImpl(IAreaInnerServiceSMO areaInnerServiceSMOImpl) {
        this.areaInnerServiceSMOImpl = areaInnerServiceSMOImpl;
    }
}
