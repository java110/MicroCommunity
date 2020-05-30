package com.java110.api.listener.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.user.IUserBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.DataFlowFactory;
import com.java110.entity.center.AppService;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.po.user.UserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 修改员工 2018年12月6日
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("modifyStaffServiceListener")
public class ModifyStaffServiceListener extends AbstractServiceApiPlusListener {

    private final static Logger logger = LoggerFactory.getLogger(ModifyStaffServiceListener.class);

    @Autowired
    private IUserBMO userBMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_USER_STAFF_MODIFY;
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
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求参数中未包含userId 节点，请确认");
        //校验json 格式中是否包含 name,email,levelCd,tel
        Assert.jsonObjectHaveKey(reqJson, "name", "请求参数中未包含name 节点，请确认");
        Assert.jsonObjectHaveKey(reqJson, "tel", "请求参数中未包含tel 节点，请确认");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        modifyStaff(reqJson, context);
    }


    private void modifyStaff(JSONObject paramObj, DataFlowContext dataFlowContext) {
        UserPo userPo = BeanConvertUtil.covertBean(builderStaffInfo(paramObj, dataFlowContext), UserPo.class);
        super.update(dataFlowContext, userPo, BusinessTypeConstant.BUSINESS_TYPE_MODIFY_USER_INFO);
    }

    /**
     * 构建员工信息
     *
     * @param paramObj
     * @param dataFlowContext
     * @return
     */
    private JSONObject builderStaffInfo(JSONObject paramObj, DataFlowContext dataFlowContext) {

        //首先根据员工ID查询员工信息，根据员工信息修改相应的数据
        ResponseEntity responseEntity = null;
        AppService appService = DataFlowFactory.getService(dataFlowContext.getAppId(), ServiceCodeConstant.SERVICE_CODE_QUERY_USER_USERINFO);
        if (appService == null) {
            throw new ListenerExecuteException(1999, "当前没有权限访问" + ServiceCodeConstant.SERVICE_CODE_QUERY_USER_USERINFO);

        }
        String requestUrl = appService.getUrl() + "?userId=" + paramObj.getString("userId");
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(), ServiceCodeConstant.SERVICE_CODE_QUERY_USER_USERINFO);
        dataFlowContext.getRequestHeaders().put("REQUEST_URL", requestUrl);
        HttpEntity<String> httpEntity = new HttpEntity<String>("", header);
        doRequest(dataFlowContext, appService, httpEntity);
        responseEntity = dataFlowContext.getResponseEntity();

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            dataFlowContext.setResponseEntity(responseEntity);
        }

        JSONObject userInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        userInfo.putAll(paramObj);

        return userInfo;
    }

}
