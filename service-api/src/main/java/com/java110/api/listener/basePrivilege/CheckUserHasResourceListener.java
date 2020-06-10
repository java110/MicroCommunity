package com.java110.api.listener.basePrivilege;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.community.IMenuInnerServiceSMO;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.entity.center.AppService;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
        Assert.hasKeyAndValue(data, "resource", "请求报文中未包含资源路径");
        logger.debug("请求信息：{}", data);
        //Assert.hasKeyAndValue(data, "userId", "请求报文中未包含userId节点");
        ResponseEntity<String> responseEntity = null;

        //判断资源路径是否配置权限 ，没有配置权限则都能访问
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource(data.getString("resource"));
        int count = menuInnerServiceSMOImpl.queryBasePrivilegesCount(basePrivilegeDto);
        //没有配置权限，都能访问
        if (count < 1) {
            responseEntity = new ResponseEntity<String>("该资源路径没有配置权限", HttpStatus.OK);
            dataFlowContext.setResponseEntity(responseEntity);
            return;
        }
        basePrivilegeDto.setUserId(data.getString("userId"));
        boolean checkFlag = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);

        if (!checkFlag) {
            responseEntity = new ResponseEntity<String>("没有权限操作", HttpStatus.UNAUTHORIZED);
        } else {

            responseEntity = new ResponseEntity<String>("成功", HttpStatus.OK);

        }

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
