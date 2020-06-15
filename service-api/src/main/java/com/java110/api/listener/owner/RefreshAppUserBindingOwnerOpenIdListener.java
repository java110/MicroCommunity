package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.owner.IOwnerBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.user.IOwnerAppUserInnerServiceSMO;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 刷入最新token
 * add by wuxw 2019-06-30
 */
@Java110Listener("refreshAppUserBindingOwnerTokenListener")
public class RefreshAppUserBindingOwnerOpenIdListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IOwnerBMO ownerBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "appUserId", "绑定ID不能为空");
        Assert.hasKeyAndValue(reqJson, "openId", "必填，请填写状态");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");
        if (reqJson.getString("appUserId").startsWith("-")) {
            Assert.hasKeyAndValue(reqJson, "oldAppUserId", "必填，请填写老绑定ID");
            Assert.hasKeyAndValue(reqJson, "appType", "必填，请填写appType");
        }

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        //ownerBMOImpl.updateAuditAppUserBindingOwner(reqJson, context);

        String appUserId = reqJson.getString("appUserId");

        if (appUserId.startsWith("-")) {
            OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
            ownerAppUserDto.setAppUserId(reqJson.getString("oldAppUserId"));
            ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
            List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

            Assert.listOnlyOne(ownerAppUserDtos, "传入oldAppUserId错误");
            OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(ownerAppUserDtos.get(0), OwnerAppUserPo.class);
            ownerAppUserPo.setAppUserId("-1");
            ownerAppUserPo.setAppType(reqJson.getString("appType"));
            ownerAppUserPo.setOpenId(reqJson.getString("openId"));
            super.insert(context, ownerAppUserPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_APP_USER);

            return;
        }

        OwnerAppUserPo ownerAppUserPo = new OwnerAppUserPo();
        ownerAppUserPo.setAppUserId(appUserId);
        ownerAppUserPo.setCommunityId(reqJson.getString("communityId"));
        ownerAppUserPo.setOpenId(reqJson.getString("openId"));
        super.update(context, ownerAppUserPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_APP_USER);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.REFRESH_APP_USER_BINDING_OWNER_OPEN_ID;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IOwnerAppUserInnerServiceSMO getOwnerAppUserInnerServiceSMOImpl() {
        return ownerAppUserInnerServiceSMOImpl;
    }

    public void setOwnerAppUserInnerServiceSMOImpl(IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl) {
        this.ownerAppUserInnerServiceSMOImpl = ownerAppUserInnerServiceSMOImpl;
    }
}
