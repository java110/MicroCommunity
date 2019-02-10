package com.java110.api.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.*;
import com.java110.common.util.Assert;
import com.java110.common.util.StringUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;

import java.util.Map;

/**
 * 用户注册 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("userRegisterServiceListener")
public class UserRegisterServiceListener extends AbstractServiceApiDataFlowListener{

    private final static Logger logger = LoggerFactory.getLogger(UserRegisterServiceListener.class);



    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_USER_SERVICE_REGISTER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    public int getOrder() {
        return 0;
    }


    /**
     * 请求参数格式：
     * {
     "userId": "-1",
     "name": "张三",
     "email": "928255095@qq.com",
     "address": "青海省西宁市城中区129号",
     "password": "ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK",
     "locationCd": "001",
     "age": 19,
     "sex": "0",
     "tel": "17797173943",
     "level_cd": "1",
     "businessUserAttr": [{
     "attrId":"-1",
     "specCd":"1001",
     "value":"01"
     }]
     }
     * @param event
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
        //获取数据上下文对象
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        String paramIn = dataFlowContext.getReqData();
        Assert.isJsonObject(paramIn,"用户注册请求参数有误，不是有效的json格式 "+paramIn);
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_USER_INFO);
        business.put(CommonConstant.HTTP_SEQ,1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL,CommonConstant.HTTP_INVOKE_MODEL_S);

        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessUser",refreshParamIn(paramIn));
        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_USER_ID,"-1");
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
        paramObj.put("userId","-1");
        paramObj.put("levelCd", UserLevelConstant.USER_LEVEL_ADMIN);

        return paramObj;
    }





}
