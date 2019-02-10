package com.java110.api.listener.users;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * 启用员工接口
 * @author wuxw
 * @create 2018-12-08 下午2:46
 * @desc 启用员工信息，如二次入职，启用之前工号信息
 **/
@Java110Listener("enableStaffServiceListener")
public class EnableStaffServiceListener extends AbstractServiceApiDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(EnableStaffServiceListener.class);

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_USER_STAFF_ENABLE;
    }

    /**
     * 接口请求方法
     * @return
     */
    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.PUT;
    }

    /**
     * 业务逻辑处理
     * 参数要求必须有员工ID
     * @param event
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
        //获取数据上下文对象
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        String paramIn = dataFlowContext.getReqData();

        Assert.jsonObjectHaveKey(paramIn,"userId","当前请求报文中未包含userId节点");

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_RECOVER_USER_INFO);
        business.put(CommonConstant.HTTP_SEQ,1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL,CommonConstant.HTTP_INVOKE_MODEL_S);

        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessUser",refreshParamIn(paramIn));
        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD,"D");
        String paramInObj = super.restToCenterProtocol(business,dataFlowContext.getRequestCurrentHeaders()).toJSONString();

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header,dataFlowContext.getRequestCurrentHeaders());

        HttpEntity<String> httpEntity = new HttpEntity<String>(paramInObj, header);
        //http://user-service/test/sayHello
        super.doRequest(dataFlowContext, service, httpEntity);

        super.doResponse(dataFlowContext);
    }

    /**
     * 对请求报文处理
     * @param paramIn
     * @return
     */
    private JSONObject refreshParamIn(String paramIn){
        JSONObject paramObj = JSONObject.parseObject(paramIn);
        return paramObj;
    }
    @Override
    public int getOrder() {
        return 0;
    }
}
