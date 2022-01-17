package com.java110.api.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * 订单类信息处理 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("orderServiceListener")
public class OrderServiceListener extends AbstractServiceApiDataFlowListener{

    private final static Logger logger = LoggerFactory.getLogger(OrderServiceListener.class);



    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_DO_SERVICE_ORDER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    public int getOrder() {
        return 0;
    }


    @Override
    public void soService(ServiceDataFlowEvent event) {
        //获取数据上下文对象
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        String paramIn = dataFlowContext.getReqData();
        Assert.jsonObjectHaveKey(paramIn,"orders","请求参数中未包含");
        JSONObject paramInObj = JSONObject.parseObject(paramIn);
        HttpHeaders header = new HttpHeaders();
        for(String key : dataFlowContext.getRequestCurrentHeaders().keySet()){
            header.add(key,dataFlowContext.getRequestCurrentHeaders().get(key));

            if(CommonConstant.HTTP_APP_ID.equals(key)) {
                paramInObj.put("appId", dataFlowContext.getRequestCurrentHeaders().get(key));
            }
            if(CommonConstant.HTTP_TRANSACTION_ID.equals(key)) {
                paramInObj.put("transactionId", dataFlowContext.getRequestCurrentHeaders().get(key));
            }
            if(CommonConstant.HTTP_SIGN.equals(key)) {
                paramInObj.put("sign", dataFlowContext.getRequestCurrentHeaders().get(key));
            }

            if(CommonConstant.HTTP_SIGN.equals(key)) {
                paramInObj.put("requestTime", dataFlowContext.getRequestCurrentHeaders().get(key));
            }
        }

        HttpEntity<String> httpEntity = new HttpEntity<String>(paramInObj.toJSONString(), header);
        //http://user-service/test/sayHello
        super.doRequest(dataFlowContext, service, httpEntity);
    }





}
