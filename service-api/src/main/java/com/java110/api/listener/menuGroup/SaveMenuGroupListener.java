package com.java110.api.listener.menuGroup;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.community.IMenuInnerServiceSMO;
import com.java110.dto.menuGroup.MenuGroupDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.java110.utils.constant.ServiceCodeMenuGroupConstant;


import com.java110.core.annotation.Java110Listener;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveMenuGroupListener")
public class SaveMenuGroupListener extends AbstractServiceApiListener {

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写组名称");
        Assert.hasKeyAndValue(reqJson, "icon", "必填，请填写icon");
        Assert.hasKeyAndValue(reqJson, "seq", "必填，请填写序列");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ResponseEntity<String> responseEntity = null;

        MenuGroupDto menuGroupDto = BeanConvertUtil.covertBean(reqJson, MenuGroupDto.class);

        freshGId(menuGroupDto);


        int saveFlag = menuInnerServiceSMOImpl.saveMenuGroup(menuGroupDto);

        responseEntity = new ResponseEntity<String>(saveFlag > 0 ? "成功" : "失败", saveFlag > 0 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 刷新 菜单组ID
     * @param menuGroupDto
     */
    private void freshGId(MenuGroupDto menuGroupDto) {

        if(!StringUtils.isEmpty(menuGroupDto.getGId())){
            return ;
        }
        //生成流水
        menuGroupDto.setGId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.MENU_GROUP));
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMenuGroupConstant.ADD_MENUGROUP;
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
