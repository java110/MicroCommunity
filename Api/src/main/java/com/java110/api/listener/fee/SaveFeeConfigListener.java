package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
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
@Java110Listener("saveFeeConfigListener")
public class SaveFeeConfigListener extends AbstractServiceApiDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(SaveFeeConfigListener.class);


    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_FEE_CONFIG;
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

        //添加单元信息
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
        Assert.jsonObjectHaveKey(paramIn, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(paramIn, "squarePrice", "请求报文中未包含squarePrice节点");
        Assert.jsonObjectHaveKey(paramIn, "additionalAmount", "请求报文中未包含additionalAmount节点");
        Assert.jsonObjectHaveKey(paramIn, "feeTypeCd", "请求报文中未包含feeTypeCd节点");
        JSONObject reqJson = JSONObject.parseObject(paramIn);
        Assert.isMoney(reqJson.getString("squarePrice"), "squarePrice不是有效金额格式");
        Assert.isMoney(reqJson.getString("additionalAmount"), "additionalAmount不是有效金额格式");

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        //校验小区楼ID和小区是否有对应关系
        List<FeeConfigDto> configDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        if (configDtos != null && configDtos.size() > 0) {
            throw new IllegalArgumentException("已经存在费用配置信息");
        }

    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IFeeConfigInnerServiceSMO getFeeConfigInnerServiceSMOImpl() {
        return feeConfigInnerServiceSMOImpl;
    }

    public void setFeeConfigInnerServiceSMOImpl(IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl) {
        this.feeConfigInnerServiceSMOImpl = feeConfigInnerServiceSMOImpl;
    }
}