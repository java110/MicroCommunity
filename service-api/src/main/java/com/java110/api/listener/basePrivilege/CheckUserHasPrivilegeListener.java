package com.java110.api.listener.basePrivilege;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.privilege.IPrivilegeBMO;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 检查用户是否有权限
 */
@Java110Listener("checkUserHasPrivilegeListener")
public class CheckUserHasPrivilegeListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IPrivilegeBMO privilegeBMOImpl;

    private final static Logger logger = LoggerFactory.getLogger(CheckUserHasPrivilegeListener.class);

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_CHECK_USER_HAS_PRIVILEGE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        JSONObject data = dataFlowContext.getReqJson();
        logger.debug("请求信息：{}",JSONObject.toJSONString(dataFlowContext));
        Assert.hasKeyAndValue(data,"userId","请求报文中未包含userId节点");
        Assert.hasKeyAndValue(data,"pId","请求报文中未包含pId节点");
        ResponseEntity<String> responseEntity = null;

        //根据名称查询用户信息
        responseEntity = privilegeBMOImpl.callService(event);

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            dataFlowContext.setResponseEntity(responseEntity);
            return ;
        }

        JSONObject resultInfo = JSONObject.parseObject(responseEntity.getBody().toString());

        JSONArray _privileges = resultInfo.getJSONArray("privileges");

        if(_privileges.size() == 0 ){
            responseEntity = new ResponseEntity<String>("没有权限操作",HttpStatus.UNAUTHORIZED);
        }else{

            responseEntity = new ResponseEntity<String>("成功",HttpStatus.OK);

        }

        dataFlowContext.setResponseEntity(responseEntity);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
