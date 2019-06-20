package com.java110.api.listener.demo;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.util.Assert;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.demo.IDemoInnerServiceSMO;
import com.java110.dto.demo.DemoDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 用例数据层侦听类
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@Java110Listener("queryDemoConfig")
public class QueryDemoConfigListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IDemoInnerServiceSMO demoInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_DEMO_CONFIG;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    /**
     * 业务层数据处理
     *
     * @param event 时间对象
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        //获取请求数据
        JSONObject reqJson = dataFlowContext.getReqJson();
        //validateDemoConfigData(reqJson);

        List<DemoDto> DemoDtos = demoInnerServiceSMOImpl.queryDemos(BeanConvertUtil.covertBean(reqJson, DemoDto.class));
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(DemoDtos), HttpStatus.OK);

        dataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 校验查询条件是否满足条件
     *
     * @param reqJson 包含查询条件
     */
    private void validateDemoConfigData(JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "demoName", "请求中未包含demoName信息");
        Assert.jsonObjectHaveKey(reqJson, "demoValue", "请求中未包含demoValue信息");


    }

    @Override
    public int getOrder() {
        return super.DEFAULT_ORDER;
    }

    public IDemoInnerServiceSMO getDemoInnerServiceSMOImpl() {
        return demoInnerServiceSMOImpl;
    }

    public void setDemoInnerServiceSMOImpl(IDemoInnerServiceSMO demoInnerServiceSMOImpl) {
        this.demoInnerServiceSMOImpl = demoInnerServiceSMOImpl;
    }
}
