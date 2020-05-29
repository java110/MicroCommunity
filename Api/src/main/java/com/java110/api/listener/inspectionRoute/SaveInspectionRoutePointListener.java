package com.java110.api.listener.inspectionRoute;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.inspection.IInspectionBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeInspectionRouteConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveInspectionRoutePointListener")
public class SaveInspectionRoutePointListener extends AbstractServiceApiPlusListener {
    @Autowired
    private IInspectionBMO inspectionBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        if (reqJson.containsKey("inspectionId")) {
            Assert.hasKeyAndValue(reqJson, "inspectionId", "必填，请填写巡检点");
        } else {
            Assert.hasKeyAndValue(reqJson, "points", "必填，请填写多个巡检点");
        }
        Assert.hasKeyAndValue(reqJson, "inspectionRouteId", "必填，请填写巡检路线");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        if (reqJson.containsKey("inspectionId")) {
            //添加单元信息
           inspectionBMOImpl.addInspectionRoute(reqJson, context, 1);
        } else { //批量的情况
            JSONArray points = reqJson.getJSONArray("points");
            for (int pointIndex = 0; pointIndex < points.size(); pointIndex++) {
                reqJson.put("inspectionId", points.getJSONObject(pointIndex).getString("inspectionId"));
                reqJson.put("inspectionName", points.getJSONObject(pointIndex).getString("inspectionName"));
                inspectionBMOImpl.addInspectionRoute(reqJson, context, pointIndex + 1);
            }
        }

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionRouteConstant.ADD_INSPECTIONROUTE_POINT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
