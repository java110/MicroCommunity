package com.java110.api.listener.smallWeChat;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.smallWeChat.ISmallWeChatBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.po.smallWechatAttr.SmallWechatAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeSmallWeChatConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小程序配置
 */
@Java110Listener("saveSmallWeChatListener")
public class SaveSmallWeChatListener extends AbstractServiceApiPlusListener {

    @Autowired
    private ISmallWeChatBMO smallWeChatBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name");
        Assert.hasKeyAndValue(reqJson, "appId", "请求报文中未包含appId");
        Assert.hasKeyAndValue(reqJson, "appSecret", "请求报文中未包含appSecret");
        Assert.hasKeyAndValue(reqJson, "payPassword", "请求报文中未包含payPassword");
        Assert.hasKeyAndValue(reqJson, "weChatType", "请求报文中未包含weChatType");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId(小区id)");
        Assert.hasKeyAndValue(reqJson, "weChatType", "请求报文中未包含类型");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        String wechatId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_weChatId);
        reqJson.put("weChatId", wechatId);
        reqJson.put("objType", SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatBMOImpl.addSmallWeChat(reqJson, context);

        //判断是否为公众号

        if (SmallWeChatDto.WECHAT_TYPE_PUBLIC.equals(reqJson.getString("weChatType"))) {
            //添加 微信对接token
            SmallWechatAttrPo smallWechatAttrPo = new SmallWechatAttrPo();
            smallWechatAttrPo.setAttrId("-1");
            smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
            smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_TOKEN);
            smallWechatAttrPo.setValue("java110");
            smallWechatAttrPo.setWechatId(wechatId);
            super.insert(context, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);

            //模板ID
            smallWechatAttrPo = new SmallWechatAttrPo();
            smallWechatAttrPo.setAttrId("-2");
            smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
            smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_OWE_FEE_TEMPLATE);
            smallWechatAttrPo.setValue("这里请填写物业缴费通知模板ID");
            smallWechatAttrPo.setWechatId(wechatId);
            super.insert(context, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);

            //模板ID
            smallWechatAttrPo = new SmallWechatAttrPo();
            smallWechatAttrPo.setAttrId("-3");
            smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
            smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_TEMPLATE);
            smallWechatAttrPo.setValue("这里请填写物业管理通知模板ID");
            smallWechatAttrPo.setWechatId(wechatId);
            super.insert(context, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);

            //业主缴费成功通知--模板ID
            smallWechatAttrPo = new SmallWechatAttrPo();
            smallWechatAttrPo.setAttrId("-3");
            smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
            smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_SUCCESS_TEMPLATE);
            smallWechatAttrPo.setValue("这里请填写业主缴费成功通知模板ID");
            smallWechatAttrPo.setWechatId(wechatId);
            super.insert(context, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);

            //业主费用到期通知--模板ID
            smallWechatAttrPo = new SmallWechatAttrPo();
            smallWechatAttrPo.setAttrId("-3");
            smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
            smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_EXPIRE_TEMPLATE);
            smallWechatAttrPo.setValue("这里请填写业主费用到期通知模板ID");
            smallWechatAttrPo.setWechatId(wechatId);
            super.insert(context, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);

        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeSmallWeChatConstant.ADD_SMALL_WE_CHAT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


}
