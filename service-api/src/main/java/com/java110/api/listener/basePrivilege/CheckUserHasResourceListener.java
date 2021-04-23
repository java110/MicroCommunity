package com.java110.api.listener.basePrivilege;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.entity.center.AppService;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * 检查用户是否有资源
 */
@Java110Listener("checkUserHasResourceListener")
public class CheckUserHasResourceListener extends AbstractServiceApiDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(CheckUserHasResourceListener.class);

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_CHECK_USER_HAS_RESOURCE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        JSONObject data = dataFlowContext.getReqJson();
        logger.debug("请求信息：{}", data);
        ResponseEntity<String> responseEntity = null;
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource(data.getString("resource"));
        basePrivilegeDto.setUserId(data.getString("userId"));
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);

        data = new JSONObject();
        data.put("privileges", privileges);
        responseEntity = new ResponseEntity<String>(data.toJSONString(), HttpStatus.OK);
        dataFlowContext.setResponseEntity(responseEntity);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public IMenuInnerServiceSMO getMenuInnerServiceSMOImpl() {
        return menuInnerServiceSMOImpl;
    }

    public void setMenuInnerServiceSMOImpl(IMenuInnerServiceSMO menuInnerServiceSMOImpl) {
        this.menuInnerServiceSMOImpl = menuInnerServiceSMOImpl;
    }
}
