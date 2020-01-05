package com.java110.api.listener.applicationKey;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.hardwareAdapation.IApplicationKeyInnerServiceSMO;
import com.java110.dto.hardwareAdapation.ApplicationKeyDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeApplicationKeyConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 生成访客密码
 */
@Java110Listener("applyVisitorApplicationKey")
public class ApplyVisitorApplicationKey extends AbstractServiceApiListener {

    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");
        Assert.hasKeyAndValue(reqJson, "idCard", "必填，请填写身份证号");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();
        ResponseEntity<String> responseEntity = null;
        ApplicationKeyDto applicationKeyDto = new ApplicationKeyDto();
        applicationKeyDto.setIdCard(reqJson.getString("idCard"));
        applicationKeyDto.setCommunityId(reqJson.getString("communityId"));
        applicationKeyDto.setState("10001"); //审核万彩城
        applicationKeyDto.setTypeFlag("1100103"); //固定密码
        applicationKeyDto.setEndTime(DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
        List<ApplicationKeyDto> applicationKeys = applicationKeyInnerServiceSMOImpl.queryApplicationKeys(applicationKeyDto);

        if (applicationKeys != null && applicationKeys.size() > 0) {
            JSONObject resObj = new JSONObject();
            resObj.put("pwd", applicationKeys.get(0).getPwd());
            responseEntity = new ResponseEntity<>(resObj.toJSONString(), HttpStatus.OK);
            context.setResponseEntity(responseEntity);

            return;
        }


        applicationKeyDto = new ApplicationKeyDto();
        applicationKeyDto.setIdCard(reqJson.getString("idCard"));
        applicationKeyDto.setCommunityId(reqJson.getString("communityId"));
        applicationKeyDto.setState("10001");
        applicationKeys = applicationKeyInnerServiceSMOImpl.queryApplicationKeys(applicationKeyDto);

        if (applicationKeys == null || applicationKeys.size() < 1) {
            throw new IllegalArgumentException("还没有住户密码，不能申请访客密码");
        }

        Calendar cl = Calendar.getInstance();
        cl.add(Calendar.DAY_OF_MONTH, 1);
        String endTime = DateUtil.getFormatTimeString(cl.getTime(), DateUtil.DATE_FORMATE_STRING_A);

        for (ApplicationKeyDto tmpApplicationKey : applicationKeys) {
            //添加单元信息
            reqJson.putAll(BeanConvertUtil.beanCovertMap(tmpApplicationKey));

            reqJson.put("endTime", endTime);
            reqJson.put("applicationKeyId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applicationKeyId));
            reqJson.put("pwd", getRandom());
            businesses.add(addApplicationKey(reqJson, context));
        }

        JSONObject paramInObj = super.restToCenterProtocol(businesses, context.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, context.getRequestCurrentHeaders());
        responseEntity = this.callService(context, service.getServiceCode(), paramInObj);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            context.setResponseEntity(responseEntity);
            return;
        }

        JSONObject resObj = new JSONObject();
        resObj.put("pwd", reqJson.getString("pwd"));
        resObj.put("endTime", endTime);

        responseEntity = new ResponseEntity<>(resObj.toJSONString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);

    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject addApplicationKey(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        //查询 是否住户密码已经审核完成


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_APPLICATION_KEY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessApplicationKey = new JSONObject();
        businessApplicationKey.putAll(paramInJson);
        businessApplicationKey.put("applicationKeyId", paramInJson.getString("applicationKeyId"));
        businessApplicationKey.put("state", "10001");
        businessApplicationKey.put("typeFlag", "1100103");
        businessApplicationKey.put("startTime", DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));

        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessApplicationKey", businessApplicationKey);
        return business;
    }


    @Override
    public String getServiceCode() {
        return ServiceCodeApplicationKeyConstant.APPLY_VISITOR_APPLICATION_KEY;
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
     * 获取随机数
     *
     * @return
     */
    private static String getRandom() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += random.nextInt(10);
        }
        return result;
    }

    public IApplicationKeyInnerServiceSMO getApplicationKeyInnerServiceSMOImpl() {
        return applicationKeyInnerServiceSMOImpl;
    }

    public void setApplicationKeyInnerServiceSMOImpl(IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl) {
        this.applicationKeyInnerServiceSMOImpl = applicationKeyInnerServiceSMOImpl;
    }
}
