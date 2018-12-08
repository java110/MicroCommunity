package com.java110.api.listener.users;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.common.cache.MappingCache;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.MappingConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.constant.UserLevelConstant;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;

/**
 * 添加员工 2018年12月6日
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("addStaffServiceListener")
public class AddStaffServiceListener extends AbstractServiceApiDataFlowListener{

    private final static Logger logger = LoggerFactory.getLogger(AddStaffServiceListener.class);



    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_USER_STAFF_ADD;
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
     * 添加员工信息
     *
     *
     *
     * @param event
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
        //获取数据上下文对象
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        String paramIn = dataFlowContext.getReqData();
        Assert.isJsonObject(paramIn,"添加员工时请求参数有误，不是有效的json格式 "+paramIn);

        //校验json 格式中是否包含 name,email,levelCd,tel
        Assert.jsonObjectHaveKey(paramIn,"name","请求参数中未包含name 节点，请确认");
        Assert.jsonObjectHaveKey(paramIn,"email","请求参数中未包含email 节点，请确认");
        Assert.jsonObjectHaveKey(paramIn,"levelCd","请求参数中未包含levelCd 节点，请确认");
        Assert.jsonObjectHaveKey(paramIn,"tel","请求参数中未包含tel 节点，请确认");


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_SERVICE_CODE,ServiceCodeConstant.SERVICE_CODE_SAVE_USER_INFO);
        business.put(CommonConstant.HTTP_BUSINESS_SERVICE_NAME,"用户注册");

        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessUser",refreshParamIn(paramIn));
        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_USER_ID,"-1");
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD,"D");
        String paramInObj = super.restToCenterProtocol(business,dataFlowContext.getRequestCurrentHeaders()).toJSONString();

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
        paramObj.put("levelCd", UserLevelConstant.USER_LEVEL_STAFF);
        //设置默认密码
        String staffDefaultPassword = MappingCache.getValue(MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
        Assert.isNull(staffDefaultPassword,"映射表中未设置员工默认密码，请检查"+MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);

        paramObj.put("password",staffDefaultPassword);
        return paramObj;
    }





}
