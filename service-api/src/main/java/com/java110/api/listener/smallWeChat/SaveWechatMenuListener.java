package com.java110.api.listener.smallWeChat;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.smallWeChat.IWechatMenuBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.wechatMenu.IWechatMenuInnerServiceSMO;
import com.java110.dto.wechatMenu.WechatMenuDto;
import com.java110.utils.constant.ServiceCodeWechatMenuConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveWechatMenuListener")
public class SaveWechatMenuListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IWechatMenuBMO wechatMenuBMOImpl;

    @Autowired
    private IWechatMenuInnerServiceSMO wechatMenuInnerServiceSMOImpl;


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "menuName", "请求报文中未包含menuName");
        Assert.hasKeyAndValue(reqJson, "menuType", "请求报文中未包含menuType");
        Assert.hasKeyAndValue(reqJson, "menuLevel", "请求报文中未包含menuLevel");
        //Assert.hasKeyAndValue(reqJson, "menuValue", "请求报文中未包含menuValue");
        Assert.hasKeyAndValue(reqJson, "seq", "请求报文中未包含顺序");
        Assert.isInteger(reqJson.getString("seq"),"顺序不是有效数字");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        String menuName = reqJson.getString("menuName");

        if (WechatMenuDto.MENU_LEVEL_ONE.equals(reqJson.getString("menuLevel"))) {
            // 名字不能超过
            if (menuName.length() > 4 && menuName.length() < 1) {
                throw new IllegalArgumentException("一级菜单必须1至4位");
            }
            //查询 一级菜单是否操作3个
            WechatMenuDto wechatMenuDto = new WechatMenuDto();
            wechatMenuDto.setCommunityId(reqJson.getString("communityId"));
            wechatMenuDto.setMenuLevel(reqJson.getString("menuLevel"));
            List<WechatMenuDto> wechatMenuDtos = wechatMenuInnerServiceSMOImpl.queryWechatMenus(wechatMenuDto);
            if(wechatMenuDtos.size() > 2){
                throw new IllegalArgumentException("一级菜单最多3个");
            }
        } else {
            Assert.hasKeyAndValue(reqJson, "parentMenuId", "未包含一级菜单信息");
            if (menuName.length() > 7 && menuName.length() < 1) {
                throw new IllegalArgumentException("二级菜单必须1至7位");
            }

            WechatMenuDto wechatMenuDto = new WechatMenuDto();
            wechatMenuDto.setCommunityId(reqJson.getString("communityId"));
            wechatMenuDto.setMenuLevel(reqJson.getString("menuLevel"));
            wechatMenuDto.setParentMenuId(reqJson.getString("parentMenuId"));
            List<WechatMenuDto> wechatMenuDtos = wechatMenuInnerServiceSMOImpl.queryWechatMenus(wechatMenuDto);
            if(wechatMenuDtos.size() > 4){
                throw new IllegalArgumentException("二级菜单最多5个");
            }
        }

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        wechatMenuBMOImpl.addWechatMenu(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeWechatMenuConstant.ADD_WECHATMENU;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
