package com.java110.api.listener.inspectionRoute;

import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.constant.ServiceCodeInspectionRouteConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.inspectionRoute.IInspectionRouteInnerServiceSMO;
import com.java110.dto.inspectionRoute.InspectionRouteDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.inspectionRoute.ApiInspectionRouteDataVo;
import com.java110.vo.api.inspectionRoute.ApiInspectionRouteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listInspectionRoutesListener")
public class ListInspectionRoutesListener extends AbstractServiceApiListener {

    @Autowired
    private IInspectionRouteInnerServiceSMO inspectionRouteInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionRouteConstant.LIST_INSPECTIONROUTES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IInspectionRouteInnerServiceSMO getInspectionRouteInnerServiceSMOImpl() {
        return inspectionRouteInnerServiceSMOImpl;
    }

    public void setInspectionRouteInnerServiceSMOImpl(IInspectionRouteInnerServiceSMO inspectionRouteInnerServiceSMOImpl) {
        this.inspectionRouteInnerServiceSMOImpl = inspectionRouteInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        InspectionRouteDto inspectionRouteDto = BeanConvertUtil.covertBean(reqJson, InspectionRouteDto.class);

        int count = inspectionRouteInnerServiceSMOImpl.queryInspectionRoutesCount(inspectionRouteDto);

        List<ApiInspectionRouteDataVo> inspectionRoutes = null;

        if (count > 0) {
            inspectionRoutes = BeanConvertUtil.covertBeanList(inspectionRouteInnerServiceSMOImpl.queryInspectionRoutes(inspectionRouteDto), ApiInspectionRouteDataVo.class);
        } else {
            inspectionRoutes = new ArrayList<>();
        }

        ApiInspectionRouteVo apiInspectionRouteVo = new ApiInspectionRouteVo();

        apiInspectionRouteVo.setTotal(count);
        apiInspectionRouteVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiInspectionRouteVo.setInspectionRoutes(inspectionRoutes);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiInspectionRouteVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
