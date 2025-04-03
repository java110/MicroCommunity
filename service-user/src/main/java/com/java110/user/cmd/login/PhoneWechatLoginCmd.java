package com.java110.user.cmd.login;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.client.RestTemplate;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.system.SystemInfoDto;
import com.java110.dto.user.LoginOwnerResDto;
import com.java110.dto.user.UserAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.intf.common.ISystemInfoV1InnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.job.IIotInnerServiceSMO;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IUserAttrV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.user.UserAttrPo;
import com.java110.po.user.UserPo;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 微信一键登陆
 */
@Java110Cmd(serviceCode = "login.phoneWechatLogin")
public class PhoneWechatLoginCmd extends Cmd {
    private final static Logger logger = LoggerFactory.getLogger(PhoneWechatLoginCmd.class);

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserAttrV1InnerServiceSMO userAttrV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;

    @Autowired
    private IIotInnerServiceSMO iotInnerServiceSMOImpl;

    @Autowired
    private ISystemInfoV1InnerServiceSMO systemInfoV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "encryptedData", "未包含encryptedData");
        Assert.hasKeyAndValue(reqJson, "iv", "未包含iv");
        Assert.hasKeyAndValue(reqJson, "code", "未包含code");
        Assert.hasKeyAndValue(reqJson, "appId", "未包含appId");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {


        //调用微信获取手机号
        String code = reqJson.getString("code");
        String encryptedData = reqJson.getString("encryptedData");
        String iv = reqJson.getString("iv");
        String appId = reqJson.getString("appId");

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setAppId(appId);
        List<SmallWeChatDto> smallWeChatDtos = smallWechatV1InnerServiceSMOImpl.querySmallWechats(smallWeChatDto);
        if (ListUtil.isNull(smallWeChatDtos)) {
            throw new CmdException("未配置小程序信息");
        }

        String sessionKey = getSessionKey(code, smallWeChatDtos.get(0));

        if (sessionKey == null) {
            throw new CmdException("获取session_key失败");
        }

        // 3. 解密手机号
        String phoneNumber = "";
        try {
            phoneNumber = decryptData(encryptedData, iv, sessionKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CmdException(e.getMessage());
        }

        if (StringUtil.isEmpty(phoneNumber)) {
            throw new CmdException("获取手机号失败");
        }

        UserDto userDto = new UserDto();
        userDto.setTel(phoneNumber);
        userDto.setLevelCd(UserDto.LEVEL_CD_USER);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            registerUser(phoneNumber);
            userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        }
        //todo 1.2 同步物业用户资料给商城
        mallInnerServiceSMOImpl.sendUserInfo(userDtos.get(0));

        //todo 1.3 同步物业用户资料给物联网
        iotInnerServiceSMOImpl.sendUserInfo(userDtos.get(0));

        String communityId = "";
        //todo 查询业主是否 认证了，如果认证了获取小区ID
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userDtos.get(0).getUserId());
        ownerAppUserDto.setLink(userDtos.get(0).getTel());
        ownerAppUserDto.setState(OwnerAppUserDto.STATE_AUDIT_SUCCESS);
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (ListUtil.isNull(ownerAppUserDtos)) {
            autoBindUserToOwner(userDtos.get(0), phoneNumber);
            communityId = smallWeChatDtos.get(0).getObjId();
        } else {
            communityId = ownerAppUserDtos.get(0).getCommunityId();
        }

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(communityId);
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listOnlyOne(communityDtos, "小区不存在，确保开发者账户配置默认小区" + communityId);


        //todo 生成 app 永久登录key
        UserDto tmpUserDto = userDtos.get(0);
        String newKey = generatorLoginKey(tmpUserDto);

        //todo 生成登录token
        String token = generatorLoginToken(tmpUserDto);
        LoginOwnerResDto loginOwnerResDto = new LoginOwnerResDto();

        loginOwnerResDto.setCommunityId(communityDtos.get(0).getCommunityId());
        loginOwnerResDto.setCommunityName(communityDtos.get(0).getName());
        loginOwnerResDto.setCommunityTel(communityDtos.get(0).getTel());
        loginOwnerResDto.setCommunityQrCode(communityDtos.get(0).getQrCode());
        loginOwnerResDto.setUserId(tmpUserDto.getUserId());
        loginOwnerResDto.setUserName(tmpUserDto.getName());
        loginOwnerResDto.setOwnerTel(tmpUserDto.getTel());
        loginOwnerResDto.setToken(token);
        loginOwnerResDto.setKey(newKey);
        context.setResponseEntity(ResultVo.createResponseEntity(loginOwnerResDto));
    }

    private void autoBindUserToOwner(UserDto userDto, String phoneNumber) {
        // todo 查询业主或成员
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(phoneNumber);
        ownerDto.setPage(1);
        ownerDto.setRow(1);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        // 说明业主不存在 直接返回跑异常
        if (ListUtil.isNull(ownerDtos)) {
            return;
        }
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(ownerDtos.get(0).getCommunityId());
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        if (ListUtil.isNull(communityDtos)) {
            return;
        }
        CommunityDto tmpCommunityDto = communityDtos.get(0);

        OwnerAppUserPo ownerAppUserPo = new OwnerAppUserPo();
        //状态类型，10000 审核中，12000 审核成功，13000 审核失败
        ownerAppUserPo.setState("12000");
        ownerAppUserPo.setAppTypeCd("10010");
        ownerAppUserPo.setAppUserId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appUserId));
        ownerAppUserPo.setMemberId(ownerDtos.get(0).getMemberId());
        ownerAppUserPo.setCommunityName(tmpCommunityDto.getName());
        ownerAppUserPo.setCommunityId(ownerDtos.get(0).getCommunityId());
        ownerAppUserPo.setAppUserName(ownerDtos.get(0).getName());
        ownerAppUserPo.setIdCard(ownerDtos.get(0).getIdCard());
        ownerAppUserPo.setAppType("WECHAT");
        ownerAppUserPo.setLink(ownerDtos.get(0).getLink());
        ownerAppUserPo.setUserId(userDto.getUserId());
        ownerAppUserPo.setOpenId("-1");
        ownerAppUserV1InnerServiceSMOImpl.saveOwnerAppUser(ownerAppUserPo);
    }

    private void registerUser(String phoneNumber) {
        // 密码就随机数
        String userPassword = AuthenticationFactory.passwdMd5(GenerateCodeFactory.getRandomCode(6));

        UserPo userPo = new UserPo();
        userPo.setUserId(GenerateCodeFactory.getGeneratorId("30"));
        userPo.setName(phoneNumber);
        userPo.setTel(phoneNumber);
        userPo.setLevelCd(UserDto.LEVEL_CD_USER);
        userPo.setPassword(AuthenticationFactory.passwdMd5(userPassword));
        int flag = userV1InnerServiceSMOImpl.saveUser(userPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }
    }

    private String getSessionKey(String code, SmallWeChatDto smallWeChatDto) {


        String secret = smallWeChatDto.getAppSecret();
        String url = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                smallWeChatDto.getAppId(), secret, code
        );
        String responseStr = outRestTemplate.getForObject(url, String.class);
        JSONObject response = JSONObject.parseObject(responseStr);

        if (response == null || response.containsKey("errcode")) {
            throw new CmdException("微信接口调用失败");
        }

        return response.getString("session_key");
    }

    public String decryptData(String encryptedData, String iv, String sessionKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(sessionKey);
        byte[] ivBytes = Base64.decodeBase64(iv);
        byte[] encryptedDataBytes = Base64.decodeBase64(encryptedData);

        // 初始化AES cipher
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        // 解密数据
        byte[] decryptedBytes = cipher.doFinal(encryptedDataBytes);
        String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);

        JSONObject data = JSONObject.parseObject(decryptedText);

        // 解析JSON获取手机号
        // 实际项目中应该使用JSON解析库
        // 这里简化为直接返回
        return data.getString("purePhoneNumber");
    }

    private String generatorLoginKey(UserDto tmpUserDto) {
        List<UserAttrDto> userAttrDtos = tmpUserDto.getUserAttrs();
        UserAttrDto userAttrDto = getCurrentUserAttrDto(userAttrDtos, UserAttrDto.SPEC_KEY);
        String newKey = UUID.randomUUID().toString();
        if (userAttrDto != null) {
            UserAttrPo userAttrPo = BeanConvertUtil.covertBean(userAttrDto, UserAttrPo.class);
            userAttrPo.setValue(newKey);
            userAttrPo.setStatusCd("0");
            userAttrV1InnerServiceSMOImpl.updateUserAttr(userAttrPo);
        } else {
            UserAttrPo userAttrPo = new UserAttrPo();
            userAttrPo.setAttrId(GenerateCodeFactory.getAttrId());
            userAttrPo.setUserId(tmpUserDto.getUserId());
            userAttrPo.setSpecCd(UserAttrDto.SPEC_KEY);
            userAttrPo.setValue(newKey);
            userAttrPo.setStatusCd("0");
            userAttrV1InnerServiceSMOImpl.saveUserAttr(userAttrPo);
        }
        return newKey;
    }

    private UserAttrDto getCurrentUserAttrDto(List<UserAttrDto> userAttrDtos, String specCd) {
        if (userAttrDtos == null) {
            return null;
        }
        for (UserAttrDto userAttrDto : userAttrDtos) {
            if (specCd.equals(userAttrDto.getSpecCd())) {
                return userAttrDto;
            }
        }

        return null;
    }

    /**
     * 生成登录key
     *
     * @param tmpUserDto
     * @return
     */
    private static String generatorLoginToken(UserDto tmpUserDto) {
        String token;
        try {
            Map userMap = new HashMap();
            userMap.put(CommonConstant.LOGIN_USER_ID, tmpUserDto.getUserId());
            userMap.put(CommonConstant.LOGIN_USER_NAME, tmpUserDto.getUserName());
            token = AuthenticationFactory.createAndSaveToken(userMap);
        } catch (Exception e) {
            logger.error("登录异常：", e);
            throw new CmdException("系统内部错误，请联系管理员");
        }
        return token;
    }
}
