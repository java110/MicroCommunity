/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.store.cmd.smallWechat;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.app.AppDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.intf.store.ISmallWechatAttrV1InnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.po.smallWechat.SmallWechatPo;
import com.java110.po.smallWechatAttr.SmallWechatAttrPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类表述：保存
 * 服务编码：smallWechat.saveSmallWechat
 * 请求路劲：/app/smallWechat.SaveSmallWechat
 * add by 吴学文 at 2022-05-25 10:46:07 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "smallWeChat.saveSmallWeChat")
public class SaveSmallWechatCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveSmallWechatCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrV1InnerServiceSMO smallWechatAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name");
        Assert.hasKeyAndValue(reqJson, "appId", "请求报文中未包含appId");
        Assert.hasKeyAndValue(reqJson, "appSecret", "请求报文中未包含appSecret");
        Assert.hasKeyAndValue(reqJson, "payPassword", "请求报文中未包含payPassword");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId(小区id)");
        Assert.hasKeyAndValue(reqJson, "weChatType", "请求报文中未包含类型");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String appId = cmdDataFlowContext.getReqHeaders().get("app-id");
        if (StringUtil.isEmpty(appId)) {
            appId = cmdDataFlowContext.getReqHeaders().get("app-id");
        }

        String wechatId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_weChatId);
        reqJson.put("wechatId", wechatId);
        reqJson.put("objType", SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        SmallWechatPo smallWechatPo = BeanConvertUtil.covertBean(reqJson, SmallWechatPo.class);
        smallWechatPo.setWechatId(wechatId);
        smallWechatPo.setWechatType(reqJson.getString("weChatType"));
        int flag = smallWechatV1InnerServiceSMOImpl.saveSmallWechat(smallWechatPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        //判断是否为公众号
        if (!SmallWeChatDto.WECHAT_TYPE_PUBLIC.equals(reqJson.getString("weChatType"))) {
            return;
        }


        //添加 微信对接token
        SmallWechatAttrPo smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_TOKEN);
        smallWechatAttrPo.setValue("java110");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        //商城时 不创建
        if (AppDto.WECHAT_MALL_APP_ID.equals(appId)) {
            return;
        }

        //模板ID
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_OWE_FEE_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写物业缴费通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        //模板ID
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写物业管理通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        //业主缴费成功通知--模板ID
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_SUCCESS_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写业主缴费成功通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        //业主费用到期通知--模板ID
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_EXPIRE_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写业主费用到期通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        //空置房验房申请流程、审批流程通知--模板ID（资产调拨审批、领用审批、转赠通知-资产管理待办审批通知）
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_PROCESS_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写流程通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        //空置房验房状态（通过和不通过）、审批状态（通过和不通过） 通知--模板ID
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_ROOM_STATE_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写空置房状态通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        //报修工单提醒 通知--模板ID
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_WORK_ORDER_REMIND_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写报修工单通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        //报修工单派单和转单提醒给维修师傅 通知--模板ID
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_DISPATCH_REMIND_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写报修工单派单和转单通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        //报修工单派单和抢单提醒给业主，安排师傅维修（进度提醒） 通知--模板ID
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_SCHEDULE_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写报修工单派单和抢单提醒给业主，安排师傅维修（进度提醒）通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        //报修工单维修完成提醒给业主 通知--模板ID
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_WORK_ORDER_END_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写报修工单维修完成提醒给业主通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        //装修申请提醒给业主缴纳装修押金等信息 通知--模板ID
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_HOUSE_DECORATION_APPLY_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写装修申请提醒给业主进行缴纳装修押金和确认装修信息通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        //装修申请提醒给物业人员进行审核操作 通知--模板ID
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_HOUSE_DECORATION_CHECK_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写装修申请提醒给物业人员进行审核操作通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        //装修申请审核结果提醒给业主 通知--模板ID
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_HOUSE_DECORATION_CHECK_RESULT_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写装修申请审核结果提醒给业主,通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        //装修验收结果提醒给业主 通知--模板ID
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_HOUSE_DECORATION_COMPLETED_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写装修验收结果提醒给业主,通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        //报修通知-上门维修现场收费通知业主 通知--模板ID
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_REPAIR_CHARGE_SCENE_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写上门维修现场收费，提醒业主缴费,通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        //报修通知-上门维修现场收费通知业主 通知--模板ID
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_OA_WORKFLOW_AUDIT_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写流程待审批通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        //报修通知-上门维修现场收费通知业主 通知--模板ID
        smallWechatAttrPo = new SmallWechatAttrPo();
        smallWechatAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        smallWechatAttrPo.setCommunityId(reqJson.getString("objId"));
        smallWechatAttrPo.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_OA_WORKFLOW_AUDIT_FINISH_TEMPLATE);
        smallWechatAttrPo.setValue("这里请填写流程审批通知模板ID");
        smallWechatAttrPo.setWechatId(wechatId);
        flag = smallWechatAttrV1InnerServiceSMOImpl.saveSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }


        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
