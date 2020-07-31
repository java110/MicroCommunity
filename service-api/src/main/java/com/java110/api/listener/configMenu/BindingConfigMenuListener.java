package com.java110.api.listener.configMenu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.menu.MenuDto;
import com.java110.dto.menuGroup.MenuGroupDto;
import com.java110.entity.center.AppService;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.java110.utils.constant.CommonConstant;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.utils.constant.ServiceCodeConfigMenuConstant;


import com.java110.core.annotation.Java110Listener;

import java.util.Map;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("bindingConfigMenuListener")
public class BindingConfigMenuListener extends AbstractServiceApiListener {

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        JSONArray infos = reqJson.getJSONArray("data");

        Assert.hasKeyByFlowData(infos, "addMenuView", "name", "必填，请填写菜单名称");
        Assert.hasKeyByFlowData(infos, "addMenuView", "url", "必填，请菜单菜单地址");
        Assert.hasKeyByFlowData(infos, "addMenuView", "seq", "必填，请填写序列");
        Assert.hasKeyByFlowData(infos, "addMenuView", "isShow", "必填，请选择是否显示菜单");
        Assert.hasKeyByFlowData(infos, "addPrivilegeView", "name", "必填，请填写权限名称");
        Assert.hasKeyByFlowData(infos, "addPrivilegeView", "domain", "必填，请选择商户类型");
        Assert.hasKeyByFlowData(infos, "addPrivilegeView", "resource", "必填，请填写资源路径");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();


        JSONArray infos = reqJson.getJSONArray("data");


        JSONObject viewMenuGroupInfo = getObj(infos, "viewMenuGroupInfo");
        JSONObject addMenuView = getObj(infos, "addMenuView");
        JSONObject addPrivilegeView = getObj(infos, "addPrivilegeView");
        addPrivilegeView.put("mId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.MENU));
        addMenuView.put("mId", addPrivilegeView.getString("mId"));
        if (!hasKey(viewMenuGroupInfo, "gId")) {
            saveMenuGroup(viewMenuGroupInfo, context);
        }

        if (!hasKey(addPrivilegeView, "pId")) {
            saveMenuPrivilege(addPrivilegeView, context);
        }

        if (!hasKey(addMenuView, "mId")) {
            addMenuView.put("gId", viewMenuGroupInfo.getString("gId"));
            addMenuView.put("pId", addPrivilegeView.getString("pId"));
            saveMenu(addMenuView, context);
        }

        JSONObject outParam = new JSONObject();
        outParam.put("gId", viewMenuGroupInfo.getString("gId"));
        outParam.put("pId", addPrivilegeView.getString("pId"));
        outParam.put("mId", addMenuView.getString("mId"));


        ResponseEntity<String> responseEntity = new ResponseEntity<String>(outParam.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    private void saveMenuGroup(Map info, DataFlowContext context) {
        info.put("gId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.MENU_GROUP));
        info.put("userId", context.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        MenuGroupDto menuGroupDto = BeanConvertUtil.covertBean(info, MenuGroupDto.class);
        if (menuInnerServiceSMOImpl.saveMenuGroup(menuGroupDto) < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "参数异常，保存菜单组失败");
        }
    }

    private void saveMenu(Map info, DataFlowContext context) {
        //info.put("mId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.MENU));
        info.put("userId", context.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        MenuDto menuDto = BeanConvertUtil.covertBean(info, MenuDto.class);
        if (menuInnerServiceSMOImpl.saveMenu(menuDto) < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "参数异常，保存菜单失败");
        }
    }

    private void saveMenuPrivilege(Map info, DataFlowContext context) {
        info.put("pId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.BASE_PRIVILEGE));
        info.put("userId", context.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        BasePrivilegeDto basePrivilegeDto = BeanConvertUtil.covertBean(info, BasePrivilegeDto.class);
        if (menuInnerServiceSMOImpl.saveBasePrivilege(basePrivilegeDto) < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "参数异常，保存菜单权限失败");
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConfigMenuConstant.BINDING_CONFIGMENU;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    private boolean hasKey(JSONObject info, String key) {
        if (!info.containsKey(key)
                || StringUtil.isEmpty(info.getString(key))
                || info.getString(key).startsWith("-")) {
            return false;
        }
        return true;

    }

    private JSONObject getObj(JSONArray infos, String flowComponent) {

        JSONObject serviceInfo = null;

        for (int infoIndex = 0; infoIndex < infos.size(); infoIndex++) {

            Assert.hasKeyAndValue(infos.getJSONObject(infoIndex), "flowComponent", "未包含服务流程组件名称");

            if (flowComponent.equals(infos.getJSONObject(infoIndex).getString("flowComponent"))) {
                serviceInfo = infos.getJSONObject(infoIndex);
                Assert.notNull(serviceInfo, "未包含服务信息");
                return serviceInfo;
            }
        }

        throw new IllegalArgumentException("未找到组件编码为【" + flowComponent + "】数据");
    }


    public IMenuInnerServiceSMO getMenuInnerServiceSMOImpl() {
        return menuInnerServiceSMOImpl;
    }

    public void setMenuInnerServiceSMOImpl(IMenuInnerServiceSMO menuInnerServiceSMOImpl) {
        this.menuInnerServiceSMOImpl = menuInnerServiceSMOImpl;
    }
}
