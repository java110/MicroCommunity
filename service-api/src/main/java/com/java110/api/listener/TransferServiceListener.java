package com.java110.api.listener;

import com.java110.utils.constant.ServiceCodeConstant;
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
 * 保存 用户信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("transferServiceListener")
public class TransferServiceListener extends AbstractServiceApiDataFlowListener{

    private final static Logger logger = LoggerFactory.getLogger(TransferServiceListener.class);



    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_DO_SERVICE_TRANSFER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return null;
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
        HttpHeaders header = new HttpHeaders();
        for(String key : dataFlowContext.getRequestCurrentHeaders().keySet()){
            header.add(key,dataFlowContext.getRequestCurrentHeaders().get(key));
        }
        HttpEntity<String> httpEntity = new HttpEntity<String>(paramIn, header);
        //http://user-service/test/sayHello
        super.doRequest(dataFlowContext, service, httpEntity);
    }





}
