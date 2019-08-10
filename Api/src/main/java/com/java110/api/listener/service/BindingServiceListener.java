package com.java110.api.listener.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceCodeServiceConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.common.util.BeanConvertUtil;
import com.java110.common.util.StringUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.app.IAppInnerServiceSMO;
import com.java110.core.smo.service.IRouteInnerServiceSMO;
import com.java110.core.smo.service.IServiceInnerServiceSMO;
import com.java110.dto.app.AppDto;
import com.java110.dto.service.RouteDto;
import com.java110.dto.service.ServiceDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Java110Listener("bindingServiceListener")
public class BindingServiceListener extends AbstractServiceApiListener {


    @Autowired
    private IAppInnerServiceSMO appInnerServiceSMOImpl;


    @Autowired
    private IServiceInnerServiceSMO serviceInnerServiceSMOImpl;


    @Autowired
    private IRouteInnerServiceSMO routeInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        JSONArray infos = reqJson.getJSONArray("data");

        Assert.hasKeyByFlowData(infos, "addRouteView", "orderTypeCd", "必填，请填写订单类型");
        Assert.hasKeyByFlowData(infos, "addRouteView", "invokeLimitTimes", "必填，请填写调用次数");
        Assert.hasKeyByFlowData(infos, "addRouteView", "invokeModel", "可填，请填写消息队列，订单在异步调用时使用");

        if(infos == null || infos.size() !=3){
            throw new IllegalArgumentException("请求参数错误，为包含 应用，服务或扩展信息");
        }
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        JSONArray infos = reqJson.getJSONArray("data");

        JSONObject viewAppInfo = getObj(infos, "App");
        JSONObject viewServiceInfo = getObj(infos, "Service");
        JSONObject addRouteView = getObj(infos, "addRouteView");

        Assert.notNull(viewAppInfo, "未包含应用信息");
        Assert.notNull(viewServiceInfo, "未包含服务信息");
        Assert.notNull(addRouteView, "未包含扩展信息");


        //处理 应用信息
        if(!hasKey(viewAppInfo, "appId")){
            viewAppInfo.put("appId", saveAppInfo(reqJson, viewAppInfo));
        }

        //处理 服务信息
        if(!hasKey(viewServiceInfo, "serviceId")){
            viewServiceInfo.put("serviceId", saveServiceInfo(reqJson, viewServiceInfo));
        }

        //处理路由信息

        RouteDto routeDto = BeanConvertUtil.covertBean(addRouteView, RouteDto.class);
        routeDto.setAppId(viewAppInfo.getString("appId"));
        routeDto.setServiceId(viewServiceInfo.getString("serviceId"));

        int count = routeInnerServiceSMOImpl.saveRoute(routeDto);


        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "保存应用数据失败");
        }



        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(routeDto), HttpStatus.OK);

        context.setResponseEntity(responseEntity);


    }

    /**
     * 保存应用信息
     * @param reqJson 请求报文信息
     * @param appInfo 应用组件信息
     * @return 应用ID
     */
    private String saveAppInfo(JSONObject reqJson, JSONObject appInfo){

        AppDto appDto = BeanConvertUtil.covertBean(appInfo, AppDto.class);

        appDto.setAppId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_id));

        int count = appInnerServiceSMOImpl.saveApp(appDto);

        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "保存应用数据失败");
        }

        return appDto.getAppId();
    }

    /**
     * 保存服务信息
     * @param reqJson 请求报文信息
     * @param serviceInfo 服务组件信息
     * @return 服务ID
     */
    private String saveServiceInfo(JSONObject reqJson, JSONObject serviceInfo){
        ServiceDto serviceDto = BeanConvertUtil.covertBean(serviceInfo, ServiceDto.class);
        serviceDto.setProvideAppId("8000418002");
        serviceDto.setBusinessTypeCd("API");
        serviceDto.setSeq("1");

        serviceDto.setServiceId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_service_id));

        int count = serviceInnerServiceSMOImpl.saveService(serviceDto);

        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "保存服务数据失败");
        }
        return serviceDto.getServiceId();
    }

    private JSONObject getObj(JSONArray infos , String flowComponent){

        JSONObject serviceInfo = null;

        for(int infoIndex = 0 ; infoIndex < infos.size(); infoIndex ++){

            Assert.hasKeyAndValue(infos.getJSONObject(infoIndex), "flowComponent", "未包含服务流程组件名称");

            if(flowComponent.equals(infos.getJSONObject(infoIndex).getString("flowComponent"))){
                serviceInfo = infos.getJSONObject(infoIndex);
                Assert.notNull(serviceInfo, "未包含服务信息");
                return serviceInfo;
            }
        }

        throw new IllegalArgumentException("未找到组件编码为【" + flowComponent + "】数据");
    }

    private boolean hasKey(JSONObject info, String key){
        if(!info.containsKey(key)
                || StringUtil.isEmpty(info.getString(key))
                || info.getString(key).startsWith("-")){
            return false;
        }
        return true;

    }


    @Override
    public String getServiceCode() {
        return ServiceCodeServiceConstant.BINDING_SERVICE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IAppInnerServiceSMO getAppInnerServiceSMOImpl() {
        return appInnerServiceSMOImpl;
    }

    public void setAppInnerServiceSMOImpl(IAppInnerServiceSMO appInnerServiceSMOImpl) {
        this.appInnerServiceSMOImpl = appInnerServiceSMOImpl;
    }


    public IServiceInnerServiceSMO getServiceInnerServiceSMOImpl() {
        return serviceInnerServiceSMOImpl;
    }

    public void setServiceInnerServiceSMOImpl(IServiceInnerServiceSMO serviceInnerServiceSMOImpl) {
        this.serviceInnerServiceSMOImpl = serviceInnerServiceSMOImpl;
    }

    public IRouteInnerServiceSMO getRouteInnerServiceSMOImpl() {
        return routeInnerServiceSMOImpl;
    }

    public void setRouteInnerServiceSMOImpl(IRouteInnerServiceSMO routeInnerServiceSMOImpl) {
        this.routeInnerServiceSMOImpl = routeInnerServiceSMOImpl;
    }
}
