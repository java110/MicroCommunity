package com.java110.api.listener.repairDispatchStep;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.smo.repair.IRepairInnerServiceSMO;
import com.java110.dto.repair.RepairDto;
import com.java110.utils.constant.StateConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.utils.constant.ServiceCodeRepairDispatchStepConstant;


import com.java110.core.annotation.Java110Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("bindingRepairDispatchStepListener")
public class BindingRepairDispatchStepListener extends AbstractServiceApiListener {

    private static Logger logger = LoggerFactory.getLogger(BindingRepairDispatchStepListener.class);



    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "userId", "未包含员工信息");
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");


    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        //添加派单员工关联关系
        businesses.add(addBusinessRepairUser(reqJson, context));

        //修改报修单状态
        businesses.add(modifyBusinessRepair(reqJson, context));



        JSONObject paramInObj = super.restToCenterProtocol(businesses, context.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, context.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(context, service.getServiceCode(), paramInObj);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeRepairDispatchStepConstant.BINDING_REPAIRDISPATCHSTEP;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    private JSONObject addBusinessRepairUser(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_USER);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessObj = new JSONObject();
        businessObj.putAll(paramInJson);
        businessObj.put("state", StateConstant.STAFF_NO_FINISH_ORDER);
        businessObj.put("ruId", "-1");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessRepairUser", businessObj);
        return business;
    }

    private JSONObject modifyBusinessRepair(JSONObject paramInJson, DataFlowContext dataFlowContext){
        //查询报修单
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(paramInJson.getString("repairId"));

        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);

        Assert.isOne(repairDtos, "查询到多条数据，repairId="+ repairDto.getRepairId());

        logger.debug("查询报修单结果："+JSONObject.toJSONString(repairDtos.get(0)));

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ+1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessOwnerRepair = new JSONObject();
        businessOwnerRepair.putAll(BeanConvertUtil.beanCovertMap(repairDtos.get(0)));
        businessOwnerRepair.put("state", StateConstant.REPAIR_DISPATCHING);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessRepair", businessOwnerRepair);
        logger.debug("拼装修改 报修单状态报文："+business.toJSONString());

        return business;
    }


    public IRepairInnerServiceSMO getRepairInnerServiceSMOImpl() {
        return repairInnerServiceSMOImpl;
    }

    public void setRepairInnerServiceSMOImpl(IRepairInnerServiceSMO repairInnerServiceSMOImpl) {
        this.repairInnerServiceSMOImpl = repairInnerServiceSMOImpl;
    }
}
