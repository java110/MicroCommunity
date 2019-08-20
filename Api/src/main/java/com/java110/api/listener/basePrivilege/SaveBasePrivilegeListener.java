package com.java110.api.listener.basePrivilege;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.common.util.Assert;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.menu.IMenuInnerServiceSMO;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.ServiceCodeBasePrivilegeConstant;


import com.java110.core.annotation.Java110Listener;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveBasePrivilegeListener")
public class SaveBasePrivilegeListener extends AbstractServiceApiListener {

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写权限名称");
        Assert.hasKeyAndValue(reqJson, "domain", "必填，请选择商户类型");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ResponseEntity<String> responseEntity = null;

        BasePrivilegeDto basePrivilegeDto = BeanConvertUtil.covertBean(reqJson, BasePrivilegeDto.class);

        freshPId(basePrivilegeDto);


        int saveFlag = menuInnerServiceSMOImpl.saveBasePrivilege(basePrivilegeDto);

        responseEntity = new ResponseEntity<String>(saveFlag > 0 ? "成功" : "失败", saveFlag > 0 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 刷新 菜单组ID
     * @param basePrivilegeDto
     */
    private void freshPId(BasePrivilegeDto basePrivilegeDto) {

        if(!StringUtils.isEmpty(basePrivilegeDto.getPId())){
            return ;
        }
        //生成流水
        basePrivilegeDto.setPId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.BASE_PRIVILEGE));
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeBasePrivilegeConstant.ADD_BASEPRIVILEGE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }




    public IMenuInnerServiceSMO getMenuInnerServiceSMOImpl() {
        return menuInnerServiceSMOImpl;
    }

    public void setMenuInnerServiceSMOImpl(IMenuInnerServiceSMO menuInnerServiceSMOImpl) {
        this.menuInnerServiceSMOImpl = menuInnerServiceSMOImpl;
    }
}
