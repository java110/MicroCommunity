package com.java110.api.listener.activities;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.IActivitiesInnerServiceSMO;
import com.java110.dto.activities.ActivitiesDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeActivitiesConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.activities.ApiActivitiesDataVo;
import com.java110.vo.api.activities.ApiActivitiesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listActivitiessListener")
public class ListActivitiessListener extends AbstractServiceApiListener {

    @Autowired
    private IActivitiesInnerServiceSMO activitiesInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeActivitiesConstant.LIST_ACTIVITIESS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IActivitiesInnerServiceSMO getActivitiesInnerServiceSMOImpl() {
        return activitiesInnerServiceSMOImpl;
    }

    public void setActivitiesInnerServiceSMOImpl(IActivitiesInnerServiceSMO activitiesInnerServiceSMOImpl) {
        this.activitiesInnerServiceSMOImpl = activitiesInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ActivitiesDto activitiesDto = BeanConvertUtil.covertBean(reqJson, ActivitiesDto.class);

        int count = activitiesInnerServiceSMOImpl.queryActivitiessCount(activitiesDto);

        List<ApiActivitiesDataVo> activitiess = null;

        if (count > 0) {
            activitiess = BeanConvertUtil.covertBeanList(activitiesInnerServiceSMOImpl.queryActivitiess(activitiesDto), ApiActivitiesDataVo.class);
        } else {
            activitiess = new ArrayList<>();
        }

        ApiActivitiesVo apiActivitiesVo = new ApiActivitiesVo();

        apiActivitiesVo.setTotal(count);
        apiActivitiesVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiActivitiesVo.setActivitiess(activitiess);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiActivitiesVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
