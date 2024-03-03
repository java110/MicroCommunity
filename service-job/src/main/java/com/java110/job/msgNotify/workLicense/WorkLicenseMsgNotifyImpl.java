package com.java110.job.msgNotify.workLicense;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.LogFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.mapping.Mapping;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.StaffAppAuthDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.wechat.Content;
import com.java110.dto.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.job.adapt.hcIotNew.http.ISendIot;
import com.java110.job.msgNotify.IMsgNotify;
import com.java110.job.msgNotify.IWechatTemplate;
import com.java110.job.msgNotify.MsgNotifyFactory;
import com.java110.job.msgNotify.ali.AliMsgNotifyImpl;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("workLicenseMsgNotifyImpl")
public class WorkLicenseMsgNotifyImpl implements IMsgNotify {
    private final static Logger logger = LoggerFactory.getLogger(AliMsgNotifyImpl.class);
    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private ISendIot sendIotImpl;

    public static final String PLAY_TTS_URL = "/iot/api/staff.playStaffTts";

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
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    @Override
    public ResultVo sendApplyReturnFeeMsg(String communityId, String userId, JSONObject content) {
        return null;
    }


    /**
     * 发送欠费 账单信息
     * <p>
     * 需要在阿里云 申请短信模板为
     * 尊敬的业主，您${house}的账单已生成，缴费金额：${mount}元，请及时缴费
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
        if (ListUtil.isNull(ownerAppUserDtos)) {
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

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            throw new IllegalArgumentException("员工不存在");
        }

        JSONObject paramIn = new JSONObject();
        paramIn.put("staffTel", userDtos.get(0).getTel());
        paramIn.put("ttsText", "尊敬的员工，收到一笔缴费");
        ResultVo resultVo = sendIotImpl.post(PLAY_TTS_URL, paramIn);
        return resultVo;

    }

    /**
     * 发送内容  尊敬的员工，您有一个报修单需要处理，单号为{repairId}，请您及时处理
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    repairId,
     *                    repairTypeName，
     *                    repairObjName，
     *                    repairName，
     *                    url
     *                    }
     * @return
     */
    @Override
    public ResultVo sendAddOwnerRepairMsg(String communityId, String userId, JSONObject content) {
        if (StringUtil.isEmpty(userId) || userId.startsWith("-")) {
            throw new IllegalArgumentException("员工不存在,userId = " + userId);
        }


        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            throw new IllegalArgumentException("员工不存在");
        }

        JSONObject paramIn = new JSONObject();
        paramIn.put("staffTel", userDtos.get(0).getTel());
        paramIn.put("ttsText", "尊敬的员工，您有一个报修单需要处理，单号为" + content.getString("repairId") + "，请您及时处理");
        ResultVo resultVo = sendIotImpl.post(PLAY_TTS_URL, paramIn);
        return resultVo;
    }

    /**
     * 发送内容  尊敬的员工，您有一个报修单需要处理，单号为{repairId}，请您及时处理
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    repairId,
     *                    repairName，
     *                    tel，
     *                    time，
     *                    address
     *                    }
     * @return
     */
    @Override
    public ResultVo sendDistributeRepairStaffMsg(String communityId, String userId, JSONObject content) {

        if (StringUtil.isEmpty(userId) || userId.startsWith("-")) {
            throw new IllegalArgumentException("员工不存在,userId = " + userId);
        }


        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            throw new IllegalArgumentException("员工不存在");
        }


        JSONObject paramIn = new JSONObject();
        paramIn.put("staffTel", userDtos.get(0).getTel());
        paramIn.put("ttsText", "尊敬的员工，您有一个报修单需要处理，单号为" + content.getString("repairId") + "，请您及时处理");
        ResultVo resultVo = sendIotImpl.post(PLAY_TTS_URL, paramIn);
        return resultVo;
    }

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
        if (ListUtil.isNull(ownerAppUserDtos)) {
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

    /**
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
    public ResultVo sendReturnRepairMsg(String communityId, String userId, JSONObject content) {
        if (StringUtil.isEmpty(userId) || userId.startsWith("-")) {
            throw new IllegalArgumentException("员工不存在,userId = " + userId);
        }

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            throw new IllegalArgumentException("员工不存在");
        }

        JSONObject paramIn = new JSONObject();
        paramIn.put("staffTel", userDtos.get(0).getTel());
        paramIn.put("ttsText", "尊敬的员工，您有一个报修单需要处理，单号为" + content.getString("repairId") + "，请您及时处理");
        ResultVo resultVo = sendIotImpl.post(PLAY_TTS_URL, paramIn);
        return resultVo;
    }

    /**
     * 待处理
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
        if (StringUtil.isEmpty(userId) || userId.startsWith("-")) {
            throw new IllegalArgumentException("员工不存在,userId = " + userId);
        }


        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (userDtos == null || userDtos.size() < 1) {
            throw new IllegalArgumentException("员工不存在");
        }

        JSONObject paramIn = new JSONObject();
        paramIn.put("staffTel", userDtos.get(0).getTel());
        paramIn.put("ttsText", "尊敬的员工，您有一个工单需要处理，单号为" + content.getString("orderId") + "，请您及时处理");
        ResultVo resultVo = sendIotImpl.post(PLAY_TTS_URL, paramIn);
        return resultVo;
    }

    @Override
    public ResultVo sendOaCreateStaffMsg(String communityId, String userId, JSONObject content) {

        if (StringUtil.isEmpty(userId) || userId.startsWith("-")) {
            throw new IllegalArgumentException("员工不存在,userId = " + userId);
        }


        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            throw new IllegalArgumentException("员工不存在");
        }


        JSONObject paramIn = new JSONObject();
        paramIn.put("staffTel", userDtos.get(0).getTel());
        paramIn.put("ttsText", "尊敬的员工，您有一个工单需要处理，单号为" + content.getString("orderId") + "，请您及时处理");
        ResultVo resultVo = sendIotImpl.post(PLAY_TTS_URL, paramIn);
        return resultVo;
    }

    /**
     * 投诉通知员工
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    complainName,
     *                    orderId
     *                    }
     * @return
     */
    @Override
    public ResultVo sendComplaintMsg(String communityId, String userId, JSONObject content) {
        if (StringUtil.isEmpty(userId) || userId.startsWith("-")) {
            throw new IllegalArgumentException("员工不存在,userId = " + userId);
        }


        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            throw new IllegalArgumentException("员工不存在");
        }


        JSONObject paramIn = new JSONObject();
        paramIn.put("staffTel", userDtos.get(0).getTel());
        paramIn.put("ttsText", "尊敬的员工，您有一个投诉单需要处理，单号为" + content.getString("orderId") + "，请您及时处理");
        ResultVo resultVo = sendIotImpl.post(PLAY_TTS_URL, paramIn);
        return resultVo;
    }
}
