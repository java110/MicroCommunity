package com.java110.job.msgNotify.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.mapping.Mapping;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.privilege.RoleCommunityDto;
import com.java110.dto.user.StaffAppAuthDto;
import com.java110.dto.wechat.Content;
import com.java110.dto.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IRoleCommunityV1InnerServiceSMO;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.job.msgNotify.IMsgNotify;
import com.java110.job.msgNotify.IWechatTemplate;
import com.java110.job.msgNotify.MsgNotifyFactory;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("wechatMsgNotifyImpl")
public class WechatMsgNotifyImpl implements IMsgNotify {
    private static final Logger logger = LoggerFactory.getLogger(WechatMsgNotifyImpl.class);

    private static final String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    public static final String SPEC_CD_TOKEN = "33001";//token
    public static final String SPEC_CD_OWE_FEE_TEMPLATE = "33002";//欠费推送模板
    public static final String SPEC_CD_WECHAT_TEMPLATE = "33003";//欠费推送模板、装修跟踪记录通知
    public static final String SPEC_CD_WECHAT_SUCCESS_TEMPLATE = "33004";//业主缴费成功推送模板
    public static final String SPEC_CD_WECHAT_EXPIRE_TEMPLATE = "33005";//业主费用到期通知推送模板
    public static final String SPEC_CD_WECHAT_PROCESS_TEMPLATE = "33006";//办公系统审批通知
    public static final String SPEC_CD_WECHAT_ROOM_STATE_TEMPLATE = "33007";//空置房验房状态（通过和不通过）、审批状态（通过和不通过）模板
    public static final String SPEC_CD_WECHAT_WORK_ORDER_REMIND_TEMPLATE = "33008";//报修工单提醒模板
    public static final String SPEC_CD_WECHAT_DISPATCH_REMIND_TEMPLATE = "33009";//报修工单派单和转单提醒给维修师傅
    public static final String SPEC_CD_WECHAT_SCHEDULE_TEMPLATE = "33010";//报修工单派单和抢单提醒给业主，安排师傅维修（进度提醒）
    public static final String SPEC_CD_WECHAT_WORK_ORDER_END_TEMPLATE = "33011";//报修工单维修完成提醒给业主
    public static final String SPEC_CD_WECHAT_HOUSE_DECORATION_APPLY_TEMPLATE = "33012";//装修申请提醒给业主
    public static final String SPEC_CD_WECHAT_HOUSE_DECORATION_CHECK_TEMPLATE = "33013";//装修审核/完成提醒给物业管理人员
    public static final String SPEC_CD_WECHAT_HOUSE_DECORATION_CHECK_RESULT_TEMPLATE = "33014";//装修审核结果提醒给业主
    public static final String SPEC_CD_WECHAT_HOUSE_DECORATION_COMPLETED_TEMPLATE = "33015";//装修验收结果提醒给业主
    public static final String SPEC_CD_WECHAT_REPAIR_CHARGE_SCENE_TEMPLATE = "33016";//报修通知-上门维修现场收费通知业主
    public static final String SPEC_CD_WECHAT_OA_WORKFLOW_AUDIT_TEMPLATE = "33017";//流程待审批通知
    public static final String SPEC_CD_WECHAT_OA_WORKFLOW_AUDIT_FINISH_TEMPLATE = "33018";//流程待审批完成通知

    private static final Map<String, String[]> templateKeys = new HashMap<>();

    static {
        templateKeys.put(SPEC_CD_OWE_FEE_TEMPLATE, new String[]{"缴费类型", "房号", "总金额", "缴费周期"});
        templateKeys.put(SPEC_CD_WECHAT_PROCESS_TEMPLATE, new String[]{"流程名称", "发起时间", "发起人"});
        templateKeys.put(SPEC_CD_WECHAT_SUCCESS_TEMPLATE, new String[]{"缴费房间", "费用类型", "费用周期", "缴费金额"});
        templateKeys.put(SPEC_CD_WECHAT_WORK_ORDER_REMIND_TEMPLATE, new String[]{"报修类型", "报修地址", "报修问题"});
        templateKeys.put(SPEC_CD_WECHAT_DISPATCH_REMIND_TEMPLATE, new String[]{"联系人", "手机号", "报修时间", "维修地址"});
        templateKeys.put(SPEC_CD_WECHAT_SCHEDULE_TEMPLATE, new String[]{"平台受理人", "联系电话", "受理时间"});
        templateKeys.put(SPEC_CD_WECHAT_WORK_ORDER_END_TEMPLATE, new String[]{"房屋地址", "维修工程师", "维修完成时间"});
        templateKeys.put(SPEC_CD_WECHAT_OA_WORKFLOW_AUDIT_TEMPLATE, new String[]{"流程名称", "发起人", "发起时间"});
        templateKeys.put(SPEC_CD_WECHAT_OA_WORKFLOW_AUDIT_FINISH_TEMPLATE, new String[]{"流程名称", "流程状态"});


    }

    @Autowired
    private IWechatTemplate wechatTemplateImpl;

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;



    @Autowired
    private RestTemplate outRestTemplate;

    /**
     * 发送退费申请
     *
     * @param communityId
     * @param userId
     * @param content
     * @return
     */
    @Override
    public ResultVo sendApplyReturnFeeMsg(String communityId, String userId, JSONObject content) {

        String accessToken = wechatTemplateImpl.getAccessToken(communityId);

        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setStaffId(userId);
        staffAppAuthDto.setAppType("WECHAT");
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuths(staffAppAuthDto);
        if (staffAppAuthDtos == null || staffAppAuthDtos.size() < 1) {
            throw new IllegalArgumentException("员工未认证，没有获取到微信openId");
        }
        String openId = staffAppAuthDtos.get(0).getOpenId();
        Mapping mapping = MappingCache.getMapping(MappingConstant.WECHAT_DOMAIN, SPEC_CD_WECHAT_PROCESS_TEMPLATE);

        if (mapping == null) {
            throw new IllegalArgumentException("开发者账户编码映射未配置域为=" + MappingConstant.WECHAT_DOMAIN + ",键为=" + SPEC_CD_WECHAT_PROCESS_TEMPLATE);
        }
        String templateId = wechatTemplateImpl.getTemplateId(communityId, mapping.getValue(), mapping.getName(), templateKeys.get(SPEC_CD_WECHAT_PROCESS_TEMPLATE));

        String url = sendMsgUrl + accessToken;

        JSONObject data = new JSONObject();
        PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
        templateMessage.setTemplate_id(templateId);
        templateMessage.setTouser(openId);
        data.put("thing2", new Content("退费申请审批"));
        data.put("time10", new Content(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B)));
        data.put("thing9", new Content(content.getString("name")));
        templateMessage.setData(data);
        //获取员工公众号地址
        String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN, "STAFF_WECHAT_URL");
        templateMessage.setUrl(wechatUrl);
        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        return new ResultVo(paramOut.getIntValue("errcode"), paramOut.getString("errmsg"));
    }

    /**
     * 欠费通知
     *
     * @param communityId 小区
     * @param userId      用户
     * @param contents    [{
     *                    "feeTypeName",
     *                    "payerObjName",
     *                    "billAmountOwed",
     *                    "date",
     *                    url
     *                    }]
     * @return
     */
    @Override
    public ResultVo sendOweFeeMsg(String communityId, String userId, String ownerId, List<JSONObject> contents) {

        if (StringUtil.isEmpty(userId) || userId.startsWith("-")) {
            throw new IllegalArgumentException("业主未绑定，没有获取到微信openId");
        }

        String accessToken = wechatTemplateImpl.getAccessToken(communityId);

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setCommunityId(communityId);
        ownerAppUserDto.setAppType(OwnerAppUserDto.APP_TYPE_WECHAT);
        ownerAppUserDto.setUserId(userId);
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        if (ownerAppUserDtos == null || ownerAppUserDtos.isEmpty()) {
            throw new IllegalArgumentException("业主未绑定，没有获取到微信openId");
        }

        String openId = ownerAppUserDtos.get(0).getOpenId();

        if (StringUtil.isEmpty(openId) || openId.startsWith("-")) {
            throw new IllegalArgumentException("没有获取到微信openId");
        }
        Mapping mapping = MappingCache.getMapping(MappingConstant.WECHAT_DOMAIN, SPEC_CD_OWE_FEE_TEMPLATE);

        if (mapping == null) {
            throw new IllegalArgumentException("开发者账户编码映射未配置域为=" + MappingConstant.WECHAT_DOMAIN + ",键为=" + SPEC_CD_OWE_FEE_TEMPLATE);
        }
        String templateId = wechatTemplateImpl.getTemplateId(communityId, mapping.getValue(), mapping.getName(), templateKeys.get(SPEC_CD_OWE_FEE_TEMPLATE));

        String url = sendMsgUrl + accessToken;
        JSONObject paramOut = null;
        for (JSONObject content : contents) {
            JSONObject data = new JSONObject();
            PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
            templateMessage.setTemplate_id(templateId);
            templateMessage.setTouser(openId);
            data.put("thing2", new Content(content.getString("feeTypeName")));
            String payerObjName = content.getString("payerObjName");
            if (!StringUtil.isEmpty(payerObjName) && payerObjName.length() > 20) {
                payerObjName = payerObjName.substring(0, 20);
            }
            data.put("thing12", new Content(payerObjName));
            data.put("amount3", new Content(content.getString("billAmountOwed")));
            data.put("time19", new Content(content.getString("date")));
            templateMessage.setData(data);
            templateMessage.setUrl(content.getString("url") + "&wAppId=" + wechatTemplateImpl.getAppId(communityId));
            logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
            ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
            logger.info("微信模板返回内容:{}", responseEntity);
            paramOut = JSONObject.parseObject(responseEntity.getBody());
            if (paramOut.getIntValue("errcode") != 0) {
                return new ResultVo(paramOut.getIntValue("errcode"), paramOut.getString("errmsg"));
            }
        }
        return new ResultVo(ResultVo.CODE_OK, "成功");
    }

    @Override
    public ResultVo sendPayFeeMsg(String communityId, String userId, JSONObject content, String role) {

        String accessToken = wechatTemplateImpl.getAccessToken(communityId);
        String url = sendMsgUrl + accessToken;
        Mapping mapping = MappingCache.getMapping(MappingConstant.WECHAT_DOMAIN, SPEC_CD_WECHAT_SUCCESS_TEMPLATE);

        if (mapping == null) {
            throw new IllegalArgumentException("开发者账户编码映射未配置域为=" + MappingConstant.WECHAT_DOMAIN + ",键为=" + SPEC_CD_WECHAT_SUCCESS_TEMPLATE);
        }
        String templateId = wechatTemplateImpl.getTemplateId(communityId, mapping.getValue(), mapping.getName(), templateKeys.get(SPEC_CD_WECHAT_SUCCESS_TEMPLATE));
        String openId = "";
        if (MsgNotifyFactory.ROLE_OWNER.equals(role)) {
            OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
            ownerAppUserDto.setCommunityId(communityId);
            ownerAppUserDto.setAppType(OwnerAppUserDto.APP_TYPE_WECHAT);
            ownerAppUserDto.setUserId(userId);
            List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
            if (ownerAppUserDtos == null || ownerAppUserDtos.size() < 1) {
                throw new IllegalArgumentException("业主未绑定，没有获取到微信openId");
            }
            openId = ownerAppUserDtos.get(0).getOpenId();
        } else {
            StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
            staffAppAuthDto.setStaffId(userId);
            staffAppAuthDto.setAppType("WECHAT");
            List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuths(staffAppAuthDto);
            if (staffAppAuthDtos == null || staffAppAuthDtos.size() < 1) {
                throw new IllegalArgumentException("员工未认证，没有获取到微信openId");
            }




            openId = staffAppAuthDtos.get(0).getOpenId();
        }
        JSONObject data = new JSONObject();
        PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
        templateMessage.setTemplate_id(templateId);
        templateMessage.setTouser(openId);
        // data.put("thing2", new Content(content.getString("feeTypeCdName")));
        data.put("thing2", new Content(content.getString("feeName")));
        data.put("thing10", new Content(content.getString("payFeeRoom")));
        data.put("time18", new Content(content.getString("payFeeTime")));
        data.put("amount6", new Content(content.getString("receivedAmount")));
        templateMessage.setData(data);
        templateMessage.setUrl(content.getString("url"));
        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        return new ResultVo(paramOut.getIntValue("errcode"), paramOut.getString("errmsg"));


    }

    /**
     * 业主报修时
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    repairTypeName，
     *                    repairObjName，
     *                    repairName，
     *                    url
     *                    }
     * @return
     */
    @Override
    public ResultVo sendAddOwnerRepairMsg(String communityId, String userId, JSONObject content) {
        String accessToken = wechatTemplateImpl.getAccessToken(communityId);

        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setStaffId(userId);
        staffAppAuthDto.setAppType("WECHAT");
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuths(staffAppAuthDto);
        if (ListUtil.isNull(staffAppAuthDtos)) {
            throw new IllegalArgumentException("员工未认证，没有获取到微信openId");
        }
        String openId = staffAppAuthDtos.get(0).getOpenId();

        Mapping mapping = MappingCache.getMapping(MappingConstant.WECHAT_DOMAIN, SPEC_CD_WECHAT_WORK_ORDER_REMIND_TEMPLATE);

        if (mapping == null) {
            throw new IllegalArgumentException("开发者账户编码映射未配置域为=" + MappingConstant.WECHAT_DOMAIN + ",键为=" + SPEC_CD_WECHAT_WORK_ORDER_REMIND_TEMPLATE);
        }
        String templateId = wechatTemplateImpl.getTemplateId(communityId, mapping.getValue(), mapping.getName(), templateKeys.get(SPEC_CD_WECHAT_WORK_ORDER_REMIND_TEMPLATE));

        String url = sendMsgUrl + accessToken;
        JSONObject data = new JSONObject();
        PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
        templateMessage.setTemplate_id(templateId);
        templateMessage.setTouser(openId);
        data.put("thing8", new Content(content.getString("repairTypeName")));
        data.put("thing11", new Content(content.getString("repairObjName")));
        String context = content.getString("context");
        if (!StringUtil.isEmpty(context) && context.length() > 20) {
            context = context.substring(0, 20);
        }
        data.put("thing10", new Content(context));
        templateMessage.setData(data);
        templateMessage.setUrl(content.getString("url"));
        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        return new ResultVo(paramOut.getIntValue("errcode"), paramOut.getString("errmsg"));
    }

    /**
     * 派单给维修师傅
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    repairName，
     *                    tel，
     *                    time，
     *                    address
     *                    }
     * @return
     */
    @Override
    public ResultVo sendDistributeRepairStaffMsg(String communityId, String userId, JSONObject content) {
        String accessToken = wechatTemplateImpl.getAccessToken(communityId);

        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setStaffId(userId);
        staffAppAuthDto.setAppType("WECHAT");
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuths(staffAppAuthDto);
        if (staffAppAuthDtos == null || staffAppAuthDtos.size() < 1) {
            throw new IllegalArgumentException("员工未认证，没有获取到微信openId");
        }
        String openId = staffAppAuthDtos.get(0).getOpenId();

        Mapping mapping = MappingCache.getMapping(MappingConstant.WECHAT_DOMAIN, SPEC_CD_WECHAT_DISPATCH_REMIND_TEMPLATE);

        if (mapping == null) {
            throw new IllegalArgumentException("开发者账户编码映射未配置域为=" + MappingConstant.WECHAT_DOMAIN + ",键为=" + SPEC_CD_WECHAT_DISPATCH_REMIND_TEMPLATE);
        }
        String templateId = wechatTemplateImpl.getTemplateId(communityId, mapping.getValue(), mapping.getName(), templateKeys.get(SPEC_CD_WECHAT_DISPATCH_REMIND_TEMPLATE));

        String url = sendMsgUrl + accessToken;
        JSONObject data = new JSONObject();
        PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
        templateMessage.setTemplate_id(templateId);
        templateMessage.setTouser(openId);
        data.put("thing7", new Content(content.getString("repairName")));
        data.put("phone_number3", new Content(content.getString("tel")));
        data.put("time13", new Content(content.getString("time")));
        data.put("thing9", new Content(content.getString("address")));
        templateMessage.setData(data);
        templateMessage.setUrl(content.getString("url"));
        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        return new ResultVo(paramOut.getIntValue("errcode"), paramOut.getString("errmsg"));
    }

    /**
     * 派单给业主通知
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    name，
     *                    tel，
     *                    time，
     *                    url
     *                    }
     * @return
     */
    @Override
    public ResultVo sendDistributeRepairOwnerMsg(String communityId, String userId, JSONObject content) {
        String accessToken = wechatTemplateImpl.getAccessToken(communityId);
        String url = sendMsgUrl + accessToken;
        Mapping mapping = MappingCache.getMapping(MappingConstant.WECHAT_DOMAIN, SPEC_CD_WECHAT_SCHEDULE_TEMPLATE);

        if (mapping == null) {
            throw new IllegalArgumentException("开发者账户编码映射未配置域为=" + MappingConstant.WECHAT_DOMAIN + ",键为=" + SPEC_CD_WECHAT_SCHEDULE_TEMPLATE);
        }
        String templateId = wechatTemplateImpl.getTemplateId(communityId, mapping.getValue(), mapping.getName(), templateKeys.get(SPEC_CD_WECHAT_SCHEDULE_TEMPLATE));
        String openId = "";

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setCommunityId(communityId);
        ownerAppUserDto.setAppType(OwnerAppUserDto.APP_TYPE_WECHAT);
        ownerAppUserDto.setUserId(userId);
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        if (ownerAppUserDtos == null || ownerAppUserDtos.size() < 1) {
            throw new IllegalArgumentException("业主未绑定，没有获取到微信openId");
        }
        openId = ownerAppUserDtos.get(0).getOpenId();

        JSONObject data = new JSONObject();
        PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
        templateMessage.setTemplate_id(templateId);
        templateMessage.setTouser(openId);
        data.put("thing6", new Content(content.getString("name")));
        data.put("phone_number9", new Content(content.getString("tel")));
        data.put("time3", new Content(content.getString("time")));
        templateMessage.setData(data);
        templateMessage.setUrl(content.getString("url"));
        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        return new ResultVo(paramOut.getIntValue("errcode"), paramOut.getString("errmsg"));
    }


    /**
     * 报修完成给业主通知
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    repairObjName，
     *                    staffName，
     *                    time，
     *                    url
     *                    }
     * @return
     */
    @Override
    public ResultVo sendFinishRepairOwnerMsg(String communityId, String userId, JSONObject content) {
        String accessToken = wechatTemplateImpl.getAccessToken(communityId);
        String url = sendMsgUrl + accessToken;
        Mapping mapping = MappingCache.getMapping(MappingConstant.WECHAT_DOMAIN, SPEC_CD_WECHAT_WORK_ORDER_END_TEMPLATE);

        if (mapping == null) {
            throw new IllegalArgumentException("开发者账户编码映射未配置域为=" + MappingConstant.WECHAT_DOMAIN + ",键为=" + SPEC_CD_WECHAT_WORK_ORDER_END_TEMPLATE);
        }
        String templateId = wechatTemplateImpl.getTemplateId(communityId, mapping.getValue(), mapping.getName(), templateKeys.get(SPEC_CD_WECHAT_WORK_ORDER_END_TEMPLATE));
        String openId = "";

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setCommunityId(communityId);
        ownerAppUserDto.setAppType(OwnerAppUserDto.APP_TYPE_WECHAT);
        ownerAppUserDto.setUserId(userId);
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        if (ownerAppUserDtos == null || ownerAppUserDtos.size() < 1) {
            throw new IllegalArgumentException("业主未绑定，没有获取到微信openId");
        }
        openId = ownerAppUserDtos.get(0).getOpenId();

        JSONObject data = new JSONObject();
        PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
        templateMessage.setTemplate_id(templateId);
        templateMessage.setTouser(openId);
        data.put("thing9", new Content(content.getString("repairObjName")));
        data.put("thing10", new Content(content.getString("staffName")));
        data.put("time5", new Content(content.getString("time")));
        templateMessage.setData(data);
        templateMessage.setUrl(content.getString("url"));
        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        return new ResultVo(paramOut.getIntValue("errcode"), paramOut.getString("errmsg"));
    }

    @Override
    public ResultVo sendReturnRepairMsg(String communityId, String userId, JSONObject content) {
        String accessToken = wechatTemplateImpl.getAccessToken(communityId);

        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setStaffId(userId);
        staffAppAuthDto.setAppType("WECHAT");
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuths(staffAppAuthDto);
        if (staffAppAuthDtos == null || staffAppAuthDtos.size() < 1) {
            throw new IllegalArgumentException("员工未认证，没有获取到微信openId");
        }
        String openId = staffAppAuthDtos.get(0).getOpenId();

        Mapping mapping = MappingCache.getMapping(MappingConstant.WECHAT_DOMAIN, SPEC_CD_WECHAT_WORK_ORDER_REMIND_TEMPLATE);

        if (mapping == null) {
            throw new IllegalArgumentException("开发者账户编码映射未配置域为=" + MappingConstant.WECHAT_DOMAIN + ",键为=" + SPEC_CD_WECHAT_WORK_ORDER_REMIND_TEMPLATE);
        }
        String templateId = wechatTemplateImpl.getTemplateId(communityId, mapping.getValue(), mapping.getName(), templateKeys.get(SPEC_CD_WECHAT_WORK_ORDER_REMIND_TEMPLATE));

        String url = sendMsgUrl + accessToken;
        JSONObject data = new JSONObject();
        PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
        templateMessage.setTemplate_id(templateId);
        templateMessage.setTouser(openId);
        data.put("thing8", new Content(content.getString("repairTypeName")));
        data.put("thing11", new Content(content.getString("repairObjName")));
        data.put("thing10", new Content(content.getString("repairName")));
        templateMessage.setData(data);
        templateMessage.setUrl(content.getString("url"));
        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        return new ResultVo(paramOut.getIntValue("errcode"), paramOut.getString("errmsg"));
    }

    /**
     * oa 流程待审批通知
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    flowName，
     *                    create_user_name，
     *                    create_time，
     *                    url
     *                    }
     * @return
     */
    @Override
    public ResultVo sendOaDistributeMsg(String communityId, String userId, JSONObject content) {
        String accessToken = wechatTemplateImpl.getAccessToken(communityId);

        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setStaffId(userId);
        staffAppAuthDto.setAppType("WECHAT");
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuths(staffAppAuthDto);
        if (ListUtil.isNull(staffAppAuthDtos)) {
            throw new IllegalArgumentException("员工未认证，没有获取到微信openId");
        }
        String openId = staffAppAuthDtos.get(0).getOpenId();

        Mapping mapping = MappingCache.getMapping(MappingConstant.WECHAT_DOMAIN, SPEC_CD_WECHAT_OA_WORKFLOW_AUDIT_TEMPLATE);

        if (mapping == null) {
            throw new IllegalArgumentException("开发者账户编码映射未配置域为=" + MappingConstant.WECHAT_DOMAIN + ",键为=" + SPEC_CD_WECHAT_OA_WORKFLOW_AUDIT_TEMPLATE);
        }
        String templateId = wechatTemplateImpl.getTemplateId(communityId, mapping.getValue(), mapping.getName(), templateKeys.get(SPEC_CD_WECHAT_OA_WORKFLOW_AUDIT_TEMPLATE));

        String url = sendMsgUrl + accessToken;
        JSONObject data = new JSONObject();
        PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
        templateMessage.setTemplate_id(templateId);
        templateMessage.setTouser(openId);
        data.put("thing2", new Content(content.getString("flowName")));
        data.put("thing9", new Content(content.getString("create_user_name")));
        data.put("time10", new Content(content.getString("create_time")));
        templateMessage.setData(data);
        templateMessage.setUrl(content.getString("url"));
        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        return new ResultVo(paramOut.getIntValue("errcode"), paramOut.getString("errmsg"));
    }

    /**
     * oa 流程通知发起人
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    flowName，
     *                    staffName，
     *                    url
     *                    }
     * @return
     */
    @Override
    public ResultVo sendOaCreateStaffMsg(String communityId, String userId, JSONObject content) {
        String accessToken = wechatTemplateImpl.getAccessToken(communityId);

        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setStaffId(userId);
        staffAppAuthDto.setAppType("WECHAT");
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuths(staffAppAuthDto);
        if (staffAppAuthDtos == null || staffAppAuthDtos.size() < 1) {
            throw new IllegalArgumentException("员工未认证，没有获取到微信openId");
        }
        String openId = staffAppAuthDtos.get(0).getOpenId();

        Mapping mapping = MappingCache.getMapping(MappingConstant.WECHAT_DOMAIN, SPEC_CD_WECHAT_OA_WORKFLOW_AUDIT_FINISH_TEMPLATE);

        if (mapping == null) {
            throw new IllegalArgumentException("开发者账户编码映射未配置域为=" + MappingConstant.WECHAT_DOMAIN + ",键为=" + SPEC_CD_WECHAT_OA_WORKFLOW_AUDIT_FINISH_TEMPLATE);
        }
        String templateId = wechatTemplateImpl.getTemplateId(communityId, mapping.getValue(), mapping.getName(), templateKeys.get(SPEC_CD_WECHAT_OA_WORKFLOW_AUDIT_FINISH_TEMPLATE));

        String url = sendMsgUrl + accessToken;
        JSONObject data = new JSONObject();
        PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
        templateMessage.setTemplate_id(templateId);
        templateMessage.setTouser(openId);
        data.put("thing2", new Content(content.getString("flowName")));
        data.put("thing6", new Content(content.getString("staffName")));
        templateMessage.setData(data);
        templateMessage.setUrl(content.getString("url"));
        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        return new ResultVo(paramOut.getIntValue("errcode"), paramOut.getString("errmsg"));
    }

    /**
     * 投诉通知员工
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    complainName
     *                    }
     * @return
     */
    @Override
    public ResultVo sendComplaintMsg(String communityId, String userId, JSONObject content) {
        String accessToken = wechatTemplateImpl.getAccessToken(communityId);

        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setStaffId(userId);
        staffAppAuthDto.setAppType("WECHAT");
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuths(staffAppAuthDto);
        if (ListUtil.isNull(staffAppAuthDtos)) {
            throw new IllegalArgumentException("员工未认证，没有获取到微信openId");
        }
        String openId = staffAppAuthDtos.get(0).getOpenId();
        Mapping mapping = MappingCache.getMapping(MappingConstant.WECHAT_DOMAIN, SPEC_CD_WECHAT_PROCESS_TEMPLATE);

        if (mapping == null) {
            throw new IllegalArgumentException("开发者账户编码映射未配置域为=" + MappingConstant.WECHAT_DOMAIN + ",键为=" + SPEC_CD_WECHAT_PROCESS_TEMPLATE);
        }
        String templateId = wechatTemplateImpl.getTemplateId(communityId, mapping.getValue(), mapping.getName(), templateKeys.get(SPEC_CD_WECHAT_PROCESS_TEMPLATE));

        String url = sendMsgUrl + accessToken;

        JSONObject data = new JSONObject();
        PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
        templateMessage.setTemplate_id(templateId);
        templateMessage.setTouser(openId);
        data.put("thing2", new Content("投诉单处理流程"));
        data.put("time10", new Content(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B)));
        data.put("thing9", new Content(content.getString("complaintName")));
        templateMessage.setData(data);
        //获取员工公众号地址
        String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN, "STAFF_WECHAT_URL");
        templateMessage.setUrl(wechatUrl);
        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        return new ResultVo(paramOut.getIntValue("errcode"), paramOut.getString("errmsg"));
    }
}
