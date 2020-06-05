package com.java110.api.listener.serviceProvide;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.IServiceInnerServiceSMO;
import com.java110.dto.service.ServiceProvideDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.java110.utils.constant.ServiceCodeServiceProvideConstant;


import com.java110.core.annotation.Java110Listener;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveServiceProvideListener")
public class SaveServiceProvideListener extends AbstractServiceApiListener {

    @Autowired
    private IServiceInnerServiceSMO serviceInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        JSONArray infos = reqJson.getJSONArray("data");

        Assert.hasKeyByFlowData(infos, "Service", "name", "必填，请填写服务名称");
        Assert.hasKeyByFlowData(infos, "Service", "serviceCode", "必填，请填写服务编码");
        Assert.hasKeyByFlowData(infos, "devServiceProvideView", "queryModel", "必填，请选择是否显示菜单");
        Assert.hasKeyByFlowData(infos, "devServiceProvideView", "params", "必填，请填写参数");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        JSONArray infos = reqJson.getJSONArray("data");


        JSONObject viewServiceInfo = getObj(infos, "Service");
        JSONObject devServiceProvideView = getObj(infos, "devServiceProvideView");
        JSONObject serviceProvideRemarkView = getObj(infos, "serviceProvideRemarkView");


        ServiceProvideDto serviceProvideDto = BeanConvertUtil.covertBean(viewServiceInfo, ServiceProvideDto.class);

        serviceProvideDto = BeanConvertUtil.covertBean(devServiceProvideView, serviceProvideDto);

        serviceProvideDto = BeanConvertUtil.covertBean(serviceProvideRemarkView, serviceProvideDto);

        int count = serviceInnerServiceSMOImpl.saveServiceProvide(serviceProvideDto);
        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "保存数据失败");
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(viewServiceInfo.toJSONString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeServiceProvideConstant.ADD_SERVICEPROVIDE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IServiceInnerServiceSMO getServiceInnerServiceSMOImpl() {
        return serviceInnerServiceSMOImpl;
    }

    public void setServiceInnerServiceSMOImpl(IServiceInnerServiceSMO serviceInnerServiceSMOImpl) {
        this.serviceInnerServiceSMOImpl = serviceInnerServiceSMOImpl;
    }

    private boolean hasKey(JSONObject info, String key) {
        if (!info.containsKey(key)
                || StringUtil.isEmpty(info.getString(key))
                || info.getString(key).startsWith("-")) {
            return false;
        }
        return true;

    }

    private JSONObject getObj(JSONArray infos, String flowComponent) {

        JSONObject serviceInfo = null;

        for (int infoIndex = 0; infoIndex < infos.size(); infoIndex++) {

            Assert.hasKeyAndValue(infos.getJSONObject(infoIndex), "flowComponent", "未包含服务流程组件名称");

            if (flowComponent.equals(infos.getJSONObject(infoIndex).getString("flowComponent"))) {
                serviceInfo = infos.getJSONObject(infoIndex);
                Assert.notNull(serviceInfo, "未包含服务信息");
                return serviceInfo;
            }
        }

        throw new IllegalArgumentException("未找到组件编码为【" + flowComponent + "】数据");
    }
}
