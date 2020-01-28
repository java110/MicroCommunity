package com.java110.api.listener.carBlackWhite;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeCarBlackWhiteConstant;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 保存黑白名单侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateCarBlackWhiteListener")
public class UpdateCarBlackWhiteListener extends AbstractServiceApiListener {
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "bwId", "黑白名单ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");

        Assert.hasKeyAndValue(reqJson, "blackWhite", "必填，请填写名单类型");
        Assert.hasKeyAndValue(reqJson, "carNum", "必填，请填写车牌号");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择结束时间");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        //添加单元信息
        businesses.add(updateCarBlackWhite(reqJson, context));

        JSONObject paramInObj = super.restToCenterProtocol(businesses, context.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, context.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(context, service.getServiceCode(), paramInObj);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeCarBlackWhiteConstant.UPDATE_CARBLACKWHITE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    /**
     * 添加黑白名单信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject updateCarBlackWhite(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_CAR_BLACK_WHITE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCarBlackWhite = new JSONObject();
        businessCarBlackWhite.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCarBlackWhite", businessCarBlackWhite);
        return business;
    }

}
