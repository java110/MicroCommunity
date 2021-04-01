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

            //空置房验房申请流程、审批流程通知--模板ID
            smallWechatAttrPo = new SmallWechatAttrPo();
            smallWechatAttrPo.setAttrId("-3");
            smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
            smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_PROCESS_TEMPLATE);
            smallWechatAttrPo.setValue("这里请填写流程通知模板ID");
            smallWechatAttrPo.setWechatId(wechatId);
            super.insert(context, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);

            //空置房验房状态（通过和不通过）、审批状态（通过和不通过） 通知--模板ID
            smallWechatAttrPo = new SmallWechatAttrPo();
            smallWechatAttrPo.setAttrId("-3");
            smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
            smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_ROOM_STATE_TEMPLATE);
            smallWechatAttrPo.setValue("这里请填写空置房状态通知模板ID");
            smallWechatAttrPo.setWechatId(wechatId);
            super.insert(context, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);

            //报修工单提醒 通知--模板ID
            smallWechatAttrPo = new SmallWechatAttrPo();
            smallWechatAttrPo.setAttrId("-3");
            smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
            smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_WORK_ORDER_REMIND_TEMPLATE);
            smallWechatAttrPo.setValue("这里请填写报修工单通知模板ID");
            smallWechatAttrPo.setWechatId(wechatId);
            super.insert(context, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);

            //报修工单派单和转单提醒给维修师傅 通知--模板ID
            smallWechatAttrPo = new SmallWechatAttrPo();
            smallWechatAttrPo.setAttrId("-3");
            smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
            smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_DISPATCH_REMIND_TEMPLATE);
            smallWechatAttrPo.setValue("这里请填写报修工单派单和转单通知模板ID");
            smallWechatAttrPo.setWechatId(wechatId);
            super.insert(context, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);

            //报修工单派单和抢单提醒给业主，安排师傅维修（进度提醒） 通知--模板ID
            smallWechatAttrPo = new SmallWechatAttrPo();
            smallWechatAttrPo.setAttrId("-3");
            smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
            smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_SCHEDULE_TEMPLATE);
            smallWechatAttrPo.setValue("这里请填写报修工单派单和抢单提醒给业主，安排师傅维修（进度提醒）通知模板ID");
            smallWechatAttrPo.setWechatId(wechatId);
            super.insert(context, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);

            //报修工单维修完成提醒给业主 通知--模板ID
            smallWechatAttrPo = new SmallWechatAttrPo();
            smallWechatAttrPo.setAttrId("-3");
            smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
            smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_WORK_ORDER_END_TEMPLATE);
            smallWechatAttrPo.setValue("这里请填写报修工单维修完成提醒给业主通知模板ID");
            smallWechatAttrPo.setWechatId(wechatId);
            super.insert(context, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);

            //装修申请提醒给业主缴纳装修押金等信息 通知--模板ID
            smallWechatAttrPo = new SmallWechatAttrPo();
            smallWechatAttrPo.setAttrId("-3");
            smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
            smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_HOUSE_DECORATION_APPLY_TEMPLATE);
            smallWechatAttrPo.setValue("这里请填写装修申请提醒给业主进行缴纳装修押金和确认装修信息通知模板ID");
            smallWechatAttrPo.setWechatId(wechatId);
            super.insert(context, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);

            //装修申请提醒给物业人员进行审核操作 通知--模板ID
            smallWechatAttrPo = new SmallWechatAttrPo();
            smallWechatAttrPo.setAttrId("-3");
            smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
            smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_HOUSE_DECORATION_CHECK_TEMPLATE);
            smallWechatAttrPo.setValue("这里请填写装修申请提醒给物业人员进行审核操作通知模板ID");
            smallWechatAttrPo.setWechatId(wechatId);
            super.insert(context, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);

            //装修申请审核结果提醒给业主 通知--模板ID
            smallWechatAttrPo = new SmallWechatAttrPo();
            smallWechatAttrPo.setAttrId("-3");
            smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
            smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_HOUSE_DECORATION_CHECK_RESULT_TEMPLATE);
            smallWechatAttrPo.setValue("这里请填写装修申请审核结果提醒给业主,通知模板ID");
            smallWechatAttrPo.setWechatId(wechatId);
            super.insert(context, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);

            //装修验收结果提醒给业主 通知--模板ID
            smallWechatAttrPo = new SmallWechatAttrPo();
            smallWechatAttrPo.setAttrId("-3");
            smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
            smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_HOUSE_DECORATION_COMPLETED_TEMPLATE);
            smallWechatAttrPo.setValue("这里请填写装修验收结果提醒给业主,通知模板ID");
            smallWechatAttrPo.setWechatId(wechatId);
            super.insert(context, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);


            //报修通知-上门维修现场收费通知业主 通知--模板ID
            smallWechatAttrPo = new SmallWechatAttrPo();
            smallWechatAttrPo.setAttrId("-3");
            smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
            smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_REPAIR_CHARGE_SCENE_TEMPLATE);
            smallWechatAttrPo.setValue("这里请填写上门维修现场收费，提醒业主缴费,通知模板ID");
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
