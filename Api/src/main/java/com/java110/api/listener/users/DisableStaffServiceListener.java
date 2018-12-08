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
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * 员工停用接口
 * @author wuxw
 * @create 2018-12-08 下午2:46
 * @desc 停用员工信息，如离职等情况 员工账号信息停用处理
 **/
@Java110Listener("disableStaffServiceListener")
public class DisableStaffServiceListener  extends AbstractServiceApiDataFlowListener {
    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_USER_STAFF_DISABLE;
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
    @Override
    public int getOrder() {
        return 0;
    }
}
