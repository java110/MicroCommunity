package com.java110.api.listener.@@templateCode@@;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.common.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.ServiceCode@@TemplateCode@@Constant;




import com.java110.core.annotation.Java110Listener;
/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("binding@@TemplateCode@@Listener")
public class Binding@@TemplateCode@@Listener extends AbstractServiceApiListener {
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        @@validateTemplateColumns@@
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();


        JSONArray infos = reqJson.getJSONArray("data");

        JSONObject appInfo = getObj(infos, "APP"); //应用信息
        JSONObject serviceInfo = getObj(infos, "SERVICE"); // 服务信息

        //处理 应用信息
        if(!hasKey(appInfo, "appId")){
             appInfo.put("appId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appId));
             businesses.add(addViewUnitInfo(reqJson, context));
        }

        JSONObject paramInObj = super.restToCenterProtocol(businesses, context.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, context.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(context, service.getServiceCode(), paramInObj);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCode@@TemplateCode@@Constant.@@TEMPLATECODE@@;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    @@bindingMethod@@

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject addViewUnitInfo(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_BIND_@@TEMPLATECODE@@);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessObj = new JSONObject();
        businessObj.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessRoom", businessObj);
        return business;
    }


    private boolean hasKey(JSONObject info, String key){
        if(!info.containsKey(key)
            || StringUtils.isEmpty(info.getString(key))
            || info.getString(key).startsWith("-")){
            return false;
        }
        return true;

    }

    private JSONObject getObj(JSONArray infos , String flowComponent){

        for(int infoIndex = 0 ; infoIndex < infos.size(); infoIndex ++){

            Assert.hasKeyAndValue(infos.getJSONObject(infoIndex), "flowComponent", "未包含服务流程组件名称");

            if(flowComponent.equals(infos.getJSONObject(infoIndex).getString("flowComponent"))){
                Assert.notNull(serviceInfo, "未包含服务信息");
                return infos.getJSONObject(infoIndex);
            }
         }

        throw new IllegalArgumentException("未找到组件编码为【" + flowComponent + "】数据");
     }


}
