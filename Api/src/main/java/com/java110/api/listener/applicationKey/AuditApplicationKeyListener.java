package com.java110.api.listener.applicationKey;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.hardwareAdapation.IApplicationKeyInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.dto.hardwareAdapation.ApplicationKeyDto;
import com.java110.dto.hardwareAdapation.MachineDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeApplicationKeyConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 保存钥匙申请侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("auditApplicationKeyListener")
public class AuditApplicationKeyListener extends AbstractServiceApiListener {

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "applicationKeyId", "钥匙申请ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写审核状态");
        Assert.hasKeyAndValue(reqJson, "remark", "必填，请填写审核原因");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        //添加单元信息
        businesses.add(updateApplicationKey(reqJson, context));

        JSONObject paramInObj = super.restToCenterProtocol(businesses, context.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, context.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(context, service.getServiceCode(), paramInObj);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeApplicationKeyConstant.AUDIT_APPLICATIONKEY;
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
     * 添加钥匙申请信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject updateApplicationKey(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        //根据位置id 和 位置对象查询相应 设备ID

        ApplicationKeyDto applicationKeyDto = new ApplicationKeyDto();
        applicationKeyDto.setApplicationKeyId(paramInJson.getString("applicationKeyId"));
        applicationKeyDto.setCommunityId(paramInJson.getString("communityId"));
        List<ApplicationKeyDto> applicationKeyDtos = applicationKeyInnerServiceSMOImpl.queryApplicationKeys(applicationKeyDto);
        Assert.listOnlyOne(applicationKeyDtos, "未找到申请记录或找到多条记录");

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_APPLICATION_KEY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessApplicationKey = new JSONObject();
        businessApplicationKey.putAll(BeanConvertUtil.beanCovertMap(applicationKeyDtos.get(0)));
        businessApplicationKey.put("state", "1100".equals(paramInJson.getString("state")) ? "10001" : "10003");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessApplicationKey", businessApplicationKey);
        return business;
    }

    public IMachineInnerServiceSMO getMachineInnerServiceSMOImpl() {
        return machineInnerServiceSMOImpl;
    }

    public void setMachineInnerServiceSMOImpl(IMachineInnerServiceSMO machineInnerServiceSMOImpl) {
        this.machineInnerServiceSMOImpl = machineInnerServiceSMOImpl;
    }

    public IApplicationKeyInnerServiceSMO getApplicationKeyInnerServiceSMOImpl() {
        return applicationKeyInnerServiceSMOImpl;
    }

    public void setApplicationKeyInnerServiceSMOImpl(IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl) {
        this.applicationKeyInnerServiceSMOImpl = applicationKeyInnerServiceSMOImpl;
    }
}
