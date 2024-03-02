package com.java110.job.msgNotify.ali;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.java110.core.factory.AliSendMessageFactory;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.LogFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.StaffAppAuthDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.job.msgNotify.IMsgNotify;
import com.java110.utils.cache.MappingCache;
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
import java.util.List;

@Service("aliMsgNotifyImpl")
public class AliMsgNotifyImpl implements IMsgNotify {
    private final static Logger logger = LoggerFactory.getLogger(AliMsgNotifyImpl.class);
    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

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


        if (StringUtil.isEmpty(ownerId) || ownerId.startsWith("-")) {
            throw new IllegalArgumentException("业主不存在,ownerId = " + ownerId);
        }


        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerId);
        ownerDto.setCommunityId(communityId);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        Assert.listOnlyOne(ownerDtos, "业主不存在,ownerId = " + ownerId);

        String accessKeyId = CommunitySettingFactory.getValue(communityId, "ALI_ACCESS_KEY_ID");
        String accessSecret = CommunitySettingFactory.getValue(communityId, "ALI_ACCESS_SECRET");
        String region = CommunitySettingFactory.getValue(communityId, "ALI_REGION");
        String signName = CommunitySettingFactory.getValue(communityId, "ALI_SIGN_NAME");
        String templateCode = CommunitySettingFactory.getValue(communityId, "ALI_OWE_TEMPLATE_CODE");
        DefaultProfile profile = DefaultProfile.getProfile(region,
                accessKeyId,
                accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", region);
        request.putQueryParameter("PhoneNumbers", ownerDtos.get(0).getLink());
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);

        BigDecimal oweFee = new BigDecimal(0);

        Date startDate = null;
        Date endDate = null;
        Date tmpStartDate = null;
        Date tmpEndDate = null;
        for (JSONObject content : contents) {
            oweFee = oweFee.add(new BigDecimal(content.getDouble("billAmountOwed"))).setScale(2, BigDecimal.ROUND_HALF_UP);
            tmpStartDate = DateUtil.getDateFromStringB(content.getString("date").split("~")[0]);
            tmpEndDate = DateUtil.getDateFromStringB(content.getString("date").split("~")[1]);
            if (startDate == null) {
                startDate = tmpStartDate;
                endDate = tmpEndDate;
                continue;
            }
            if (startDate.getTime() > tmpStartDate.getTime()) {
                startDate = tmpStartDate;
            }
            if (endDate.getTime() < tmpEndDate.getTime()) {
                endDate = tmpEndDate;
            }
        }

        JSONObject param = new JSONObject();
        param.put("house", contents.get(0).getString("payerObjName"));
//        param.put("feeType", content.getString("feeTypeName"));
        param.put("date", DateUtil.getFormatTimeStringB(startDate));
        param.put("date2", DateUtil.getFormatTimeStringB(endDate));
        param.put("mount", oweFee.doubleValue());
        request.putQueryParameter("TemplateParam", param.toString());

        String resParam = "";
        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.debug("发送验证码信息：{}", response.getData());
            resParam = response.getData();
        } catch (Exception e) {
            e.printStackTrace();
            resParam = e.getMessage();
            throw new IllegalArgumentException("短信催缴失败" + e.getMessage());
        } finally {
            LogFactory.saveOutLog("SMS", param.toString(), new ResponseEntity(resParam, HttpStatus.OK));
        }
        return new ResultVo(ResultVo.CODE_OK, "成功");
    }

    @Override
    public ResultVo sendPayFeeMsg(String communityId, String userId, JSONObject content, String role) {
        return null;
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
        if (userDtos == null || userDtos.size() < 1) {
            throw new IllegalArgumentException("员工不存在");
        }


        String accessKeyId = CommunitySettingFactory.getValue(communityId, "ALI_ACCESS_KEY_ID");
        String accessSecret = CommunitySettingFactory.getValue(communityId, "ALI_ACCESS_SECRET");
        String region = CommunitySettingFactory.getValue(communityId, "ALI_REGION");
        String signName = CommunitySettingFactory.getValue(communityId, "ALI_SIGN_NAME");
        String templateCode = CommunitySettingFactory.getValue(communityId, "ALI_REPAIR_TEMPLATE_CODE");
        DefaultProfile profile = DefaultProfile.getProfile(region,
                accessKeyId,
                accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", region);
        request.putQueryParameter("PhoneNumbers", userDtos.get(0).getTel());
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);



        JSONObject param = new JSONObject();
        param.put("repairId", content.getString("repairId"));
        request.putQueryParameter("TemplateParam", param.toString());

        String resParam = "";
        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.debug("发送验证码信息：{}", response.getData());
            resParam = response.getData();
        } catch (Exception e) {
            e.printStackTrace();
            resParam = e.getMessage();
            throw new IllegalArgumentException("短信工单失败" + e.getMessage());
        } finally {
            LogFactory.saveOutLog("SMS", param.toString(), new ResponseEntity(resParam, HttpStatus.OK));
        }
        return new ResultVo(ResultVo.CODE_OK, "成功");
    }

    /**
     *  发送内容  尊敬的员工，您有一个报修单需要处理，单号为{repairId}，请您及时处理
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
        if (userDtos == null || userDtos.size() < 1) {
            throw new IllegalArgumentException("员工不存在");
        }


        String accessKeyId = CommunitySettingFactory.getValue(communityId, "ALI_ACCESS_KEY_ID");
        String accessSecret = CommunitySettingFactory.getValue(communityId, "ALI_ACCESS_SECRET");
        String region = CommunitySettingFactory.getValue(communityId, "ALI_REGION");
        String signName = CommunitySettingFactory.getValue(communityId, "ALI_SIGN_NAME");
        String templateCode = CommunitySettingFactory.getValue(communityId, "ALI_REPAIR_TEMPLATE_CODE");
        DefaultProfile profile = DefaultProfile.getProfile(region,
                accessKeyId,
                accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", region);
        request.putQueryParameter("PhoneNumbers", userDtos.get(0).getTel());
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);

        JSONObject param = new JSONObject();
        param.put("repairId", content.getString("repairId"));
        request.putQueryParameter("TemplateParam", param.toString());

        String resParam = "";
        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.debug("发送验证码信息：{}", response.getData());
            resParam = response.getData();
        } catch (Exception e) {
            e.printStackTrace();
            resParam = e.getMessage();
            throw new IllegalArgumentException("短信工单失败" + e.getMessage());
        } finally {
            LogFactory.saveOutLog("SMS", param.toString(), new ResponseEntity(resParam, HttpStatus.OK));
        }
        return new ResultVo(ResultVo.CODE_OK, "成功");
    }

    @Override
    public ResultVo sendDistributeRepairOwnerMsg(String communityId, String userId, JSONObject content) {

        return new ResultVo(ResultVo.CODE_OK,"成功");

    }

    @Override
    public ResultVo sendFinishRepairOwnerMsg(String communityId, String userId, JSONObject content) {

        return new ResultVo(ResultVo.CODE_OK,"成功");
    }

    /**
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
    public ResultVo sendReturnRepairMsg(String communityId, String userId, JSONObject content) {
        if (StringUtil.isEmpty(userId) || userId.startsWith("-")) {
            throw new IllegalArgumentException("员工不存在,userId = " + userId);
        }

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (userDtos == null || userDtos.size() < 1) {
            throw new IllegalArgumentException("员工不存在");
        }


        String accessKeyId = CommunitySettingFactory.getValue(communityId, "ALI_ACCESS_KEY_ID");
        String accessSecret = CommunitySettingFactory.getValue(communityId, "ALI_ACCESS_SECRET");
        String region = CommunitySettingFactory.getValue(communityId, "ALI_REGION");
        String signName = CommunitySettingFactory.getValue(communityId, "ALI_SIGN_NAME");
        String templateCode = CommunitySettingFactory.getValue(communityId, "ALI_REPAIR_TEMPLATE_CODE");
        DefaultProfile profile = DefaultProfile.getProfile(region,
                accessKeyId,
                accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", region);
        request.putQueryParameter("PhoneNumbers", userDtos.get(0).getTel());
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);


        JSONObject param = new JSONObject();
        param.put("repairId", content.getString("repairId"));
        request.putQueryParameter("TemplateParam", param.toString());

        String resParam = "";
        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.debug("发送验证码信息：{}", response.getData());
            resParam = response.getData();
        } catch (Exception e) {
            e.printStackTrace();
            resParam = e.getMessage();
            throw new IllegalArgumentException("短信工单失败" + e.getMessage());
        } finally {
            LogFactory.saveOutLog("SMS", param.toString(), new ResponseEntity(resParam, HttpStatus.OK));
        }
        return new ResultVo(ResultVo.CODE_OK, "成功");
    }

    /**
     * 待处理
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


        String accessKeyId = CommunitySettingFactory.getValue(communityId, "ALI_ACCESS_KEY_ID");
        String accessSecret = CommunitySettingFactory.getValue(communityId, "ALI_ACCESS_SECRET");
        String region = CommunitySettingFactory.getValue(communityId, "ALI_REGION");
        String signName = CommunitySettingFactory.getValue(communityId, "ALI_SIGN_NAME");
        String templateCode = CommunitySettingFactory.getValue(communityId, "ALI_OA_UNDO_TEMPLATE_CODE");
        DefaultProfile profile = DefaultProfile.getProfile(region,
                accessKeyId,
                accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", region);
        request.putQueryParameter("PhoneNumbers", userDtos.get(0).getTel());
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);


        JSONObject param = new JSONObject();
        param.put("orderId", content.getString("orderId"));
        request.putQueryParameter("TemplateParam", param.toString());

        String resParam = "";
        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.debug("发送验证码信息：{}", response.getData());
            resParam = response.getData();
        } catch (Exception e) {
            e.printStackTrace();
            resParam = e.getMessage();
            throw new IllegalArgumentException("短信工单失败" + e.getMessage());
        } finally {
            LogFactory.saveOutLog("SMS", param.toString(), new ResponseEntity(resParam, HttpStatus.OK));
        }
        return new ResultVo(ResultVo.CODE_OK, "成功");
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


        String accessKeyId = CommunitySettingFactory.getValue(communityId, "ALI_ACCESS_KEY_ID");
        String accessSecret = CommunitySettingFactory.getValue(communityId, "ALI_ACCESS_SECRET");
        String region = CommunitySettingFactory.getValue(communityId, "ALI_REGION");
        String signName = CommunitySettingFactory.getValue(communityId, "ALI_SIGN_NAME");
        String templateCode = CommunitySettingFactory.getValue(communityId, "ALI_OA_FINISH_TEMPLATE_CODE");
        DefaultProfile profile = DefaultProfile.getProfile(region,
                accessKeyId,
                accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", region);
        request.putQueryParameter("PhoneNumbers", userDtos.get(0).getTel());
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);


        JSONObject param = new JSONObject();
        param.put("orderId", content.getString("orderId"));
        request.putQueryParameter("TemplateParam", param.toString());

        String resParam = "";
        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.debug("发送验证码信息：{}", response.getData());
            resParam = response.getData();
        } catch (Exception e) {
            e.printStackTrace();
            resParam = e.getMessage();
            throw new IllegalArgumentException("短信工单失败" + e.getMessage());
        } finally {
            LogFactory.saveOutLog("SMS", param.toString(), new ResponseEntity(resParam, HttpStatus.OK));
        }
        return new ResultVo(ResultVo.CODE_OK, "成功");
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


        String accessKeyId = CommunitySettingFactory.getValue(communityId, "ALI_ACCESS_KEY_ID");
        String accessSecret = CommunitySettingFactory.getValue(communityId, "ALI_ACCESS_SECRET");
        String region = CommunitySettingFactory.getValue(communityId, "ALI_REGION");
        String signName = CommunitySettingFactory.getValue(communityId, "ALI_SIGN_NAME");
        String templateCode = CommunitySettingFactory.getValue(communityId, "ALI_COMPLAINT_TEMPLATE_CODE");
        DefaultProfile profile = DefaultProfile.getProfile(region,
                accessKeyId,
                accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", region);
        request.putQueryParameter("PhoneNumbers", userDtos.get(0).getTel());
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);


        JSONObject param = new JSONObject();
        param.put("orderId", content.getString("orderId"));
        request.putQueryParameter("TemplateParam", param.toString());

        String resParam = "";
        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.debug("发送验证码信息：{}", response.getData());
            resParam = response.getData();
        } catch (Exception e) {
            e.printStackTrace();
            resParam = e.getMessage();
            throw new IllegalArgumentException("短信工单失败" + e.getMessage());
        } finally {
            LogFactory.saveOutLog("SMS", param.toString(), new ResponseEntity(resParam, HttpStatus.OK));
        }
        return new ResultVo(ResultVo.CODE_OK, "成功");
    }
}
