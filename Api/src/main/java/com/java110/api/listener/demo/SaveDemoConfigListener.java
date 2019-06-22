package com.java110.api.listener.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.demo.IDemoInnerServiceSMO;
import com.java110.core.smo.fee.IFeeConfigInnerServiceSMO;
import com.java110.dto.FeeConfigDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @ClassName SaveFeeConfigListener
 * @Description TODO
 * @Author wuxw
 * @Date 2019/6/1 20:51
 * @Version 1.0
 * add by wuxw 2019/6/1
 **/
@Java110Listener("saveDemoConfigListener")
public class SaveDemoConfigListener extends AbstractServiceApiDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(SaveDemoConfigListener.class);


    @Autowired
    private IDemoInnerServiceSMO demoInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_DEMO_CONFIG;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {

        logger.debug("ServiceDataFlowEvent : {}", event);

        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();

        String paramIn = dataFlowContext.getReqData();

        //校验数据
        validate(paramIn);
        JSONObject paramObj = JSONObject.parseObject(paramIn);

        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        //添加DEMO信息
        businesses.add(addFeeConfig(paramObj, dataFlowContext));

        JSONObject paramInObj = super.restToCenterProtocol(businesses, dataFlowContext.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(dataFlowContext, service.getServiceCode(), paramInObj);

        dataFlowContext.setResponseEntity(responseEntity);

    }

    /**
     * 添加小区楼信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject addFeeConfig(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_CONFIG);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(paramInJson);
        businessFeeConfig.put("configId", "-1");
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFeeConfig", businessFeeConfig);

        return business;
    }

    /**
     * 数据校验
     *
     * @param paramIn "communityId": "7020181217000001",
     *                "memberId": "3456789",
     *                "memberTypeCd": "390001200001"
     */
    private void validate(String paramIn) {
        Assert.jsonObjectHaveKey(paramIn, "demoName", "请求报文中未包含demoName节点");
        Assert.jsonObjectHaveKey(paramIn, "demoValue", "请求报文中未包含demoValue节点");

        //校验小区楼ID和小区是否有对应关系
        //List<FeeConfigDto> configDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

       /* if (configDtos != null && configDtos.size() > 0) {
            throw new IllegalArgumentException("已经存在费用配置信息");
        }*/

    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IDemoInnerServiceSMO getDemoInnerServiceSMOImpl() {
        return demoInnerServiceSMOImpl;
    }

    public void setDemoInnerServiceSMOImpl(IDemoInnerServiceSMO demoInnerServiceSMOImpl) {
        this.demoInnerServiceSMOImpl = demoInnerServiceSMOImpl;
    }
}