package com.java110.api.listener.machine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeMachineConstant;


import com.java110.core.annotation.Java110Listener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveMachineListener")
public class SaveMachineListener extends AbstractServiceApiListener {
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "machineCode", "必填，请填写设备编码");
        Assert.hasKeyAndValue(reqJson, "machineVersion", "必填，请填写设备版本号");
        Assert.hasKeyAndValue(reqJson, "machineName", "必填，请填写设备名称");
        Assert.hasKeyAndValue(reqJson, "machineTypeCd", "必填，请选择设备类型");
        Assert.hasKeyAndValue(reqJson, "direction", "必填，请选择设备方向");
        Assert.hasKeyAndValue(reqJson, "authCode", "必填，请填写鉴权编码");
        Assert.hasKeyAndValue(reqJson, "locationTypeCd", "必填，请选择位置类型");
        Assert.hasKeyAndValue(reqJson, "locationObjId", "必填，请填写位置对象ID");


    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        //添加单元信息
        businesses.add(addMachine(reqJson, context));

        JSONObject paramInObj = super.restToCenterProtocol(businesses, context.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, context.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(context, service.getServiceCode(), paramInObj);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineConstant.ADD_MACHINE;
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
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject addMachine(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_MACHINE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessMachine = new JSONObject();
        businessMachine.putAll(paramInJson);
        businessMachine.put("state", "1000");
        businessMachine.put("machineId", "-1");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessMachine", businessMachine);
        return business;
    }

}
