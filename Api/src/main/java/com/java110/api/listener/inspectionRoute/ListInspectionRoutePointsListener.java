package com.java110.api.listener.inspectionRoute;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.inspectionRoute.IInspectionRouteInnerServiceSMO;
import com.java110.core.smo.inspectionRoute.IInspectionRoutePointRelInnerServiceSMO;
import com.java110.dto.inspectionRoute.InspectionRouteDto;
import com.java110.dto.inspectionRoute.InspectionRoutePointRelDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeInspectionRouteConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.inspectionPoint.ApiInspectionPointDataVo;
import com.java110.vo.api.inspectionPoint.ApiInspectionPointVo;
import com.java110.vo.api.inspectionRoute.ApiInspectionRouteDataVo;
import com.java110.vo.api.inspectionRoute.ApiInspectionRouteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listInspectionRoutePointsListener")
public class ListInspectionRoutePointsListener extends AbstractServiceApiListener {

    @Autowired
    private IInspectionRoutePointRelInnerServiceSMO inspectionRoutePointRelInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionRouteConstant.LIST_INSPECTIONROUTE_POINTS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IInspectionRoutePointRelInnerServiceSMO getInspectionRoutePointRelInnerServiceSMOImpl() {
        return inspectionRoutePointRelInnerServiceSMOImpl;
    }

    public void setInspectionRoutePointRelInnerServiceSMOImpl(IInspectionRoutePointRelInnerServiceSMO inspectionRoutePointRelInnerServiceSMOImpl) {
        this.inspectionRoutePointRelInnerServiceSMOImpl = inspectionRoutePointRelInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        InspectionRoutePointRelDto inspectionRoutePointRelDto = BeanConvertUtil.covertBean(reqJson, InspectionRoutePointRelDto.class);

        int count = inspectionRoutePointRelInnerServiceSMOImpl.queryInspectionRoutePointRelsCount(inspectionRoutePointRelDto);

        List<ApiInspectionPointDataVo> inspectionPoints = null;

        if (count > 0) {
            inspectionPoints = BeanConvertUtil.covertBeanList(inspectionRoutePointRelInnerServiceSMOImpl.queryInspectionRoutePointRels(inspectionRoutePointRelDto), ApiInspectionPointDataVo.class);
        } else {
            inspectionPoints = new ArrayList<>();
        }

        ApiInspectionPointVo apiInspectionPointVo = new ApiInspectionPointVo();

        apiInspectionPointVo.setTotal(count);
        apiInspectionPointVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiInspectionPointVo.setInspectionPoints(inspectionPoints);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiInspectionPointVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
