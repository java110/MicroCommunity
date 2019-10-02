package com.java110.api.listener.ownerRepair;

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
import com.java110.utils.constant.ServiceCodeOwnerRepairConstant;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 保存业主报修侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateOwnerRepairListener")
public class UpdateOwnerRepairListener extends AbstractServiceApiListener {
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "repairId", "报修ID不能为空");
        Assert.hasKeyAndValue(reqJson, "repairType", "必填，请选择报修类型");
        Assert.hasKeyAndValue(reqJson, "repairName", "必填，请填写报修人名称");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写报修人手机号");
        Assert.hasKeyAndValue(reqJson, "roomId", "必填，请填写房屋ID");
        Assert.hasKeyAndValue(reqJson, "appointmentTime", "必填，请填写预约时间");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写报修内容");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        //添加单元信息
        businesses.add(updateOwnerRepair(reqJson, context));

        JSONObject paramInObj = super.restToCenterProtocol(businesses, context.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, context.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(context, service.getServiceCode(), paramInObj);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeOwnerRepairConstant.UPDATE_OWNERREPAIR;
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
     * 添加业主报修信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject updateOwnerRepair(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessOwnerRepair = new JSONObject();
        businessOwnerRepair.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessOwnerRepair", businessOwnerRepair);
        return business;
    }

}
