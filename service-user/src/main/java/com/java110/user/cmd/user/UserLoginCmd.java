package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.app.AppDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.msg.SmsDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.UserAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.ISmsInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.user.UserPo;
import com.java110.po.userAttr.UserAttrPo;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.UserLevelConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.utils.util.ValidatorUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户登录
 */
@Java110Cmd(serviceCode = "user.userLogin")
public class UserLoginCmd extends Cmd {

    private final static Logger logger = LoggerFactory.getLogger(UserLoginCmd.class);


    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IUserAttrV1InnerServiceSMO userAttrV1InnerServiceSMOImpl;

    @Autowired
    private ISmsInnerServiceSMO smsInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        if (!reqJson.containsKey("userName")) {
            Assert.hasKeyAndValue(reqJson, "key", "未包含key");
        }

        if (!reqJson.containsKey("key")) {
            Assert.hasKeyAndValue(reqJson, "userName", "未包含userName");
            Assert.hasKeyAndValue(reqJson, "password", "未包含password");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //1.0 优先用 手机号登录
        UserDto userDto = new UserDto();
        String errorInfo = "";
        if (reqJson.containsKey("levelCd")) {
            userDto.setLevelCd(reqJson.getString("levelCd"));
        }
        if (reqJson.containsKey("userName")) {
            if (ValidatorUtil.isMobile(reqJson.getString("userName"))) {//用户临时秘钥登录
                userDto.setTel(reqJson.getString("userName"));
            } else {
                userDto.setUserName(reqJson.getString("userName"));
            }
            //验证码登录
            if (reqJson.containsKey("loginByPhone") && reqJson.getBoolean("loginByPhone")) {
                SmsDto smsDto = new SmsDto();
                smsDto.setTel(reqJson.getString("userName"));
                smsDto.setCode(reqJson.getString("password"));
                smsDto = smsInnerServiceSMOImpl.validateCode(smsDto);
                if (!smsDto.isSuccess()) {
                    throw new SMOException("验证码错误");
                }

            } else {
                userDto.setPassword(AuthenticationFactory.passwdMd5(reqJson.getString("password")));
            }
            errorInfo = "用户名或密码错误";
        } else {
            userDto.setKey(reqJson.getString("key"));
            errorInfo = "临时票据错误";
        }

        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        //todo 验证码登录，判断是否是否业主，并且是否绑定关系 如果没有 自动绑定关系
        if (userDtos == null || userDtos.size() < 1) {
            userDtos = ifOwnerLoginByPhone(reqJson, context);
        }
        if (userDtos == null || userDtos.size() < 1) {
            throw new SMOException(errorInfo);
        }


        //表名登录成功
        UserDto tmpUserDto = userDtos.get(0);

        List<UserAttrDto> userAttrDtos = tmpUserDto.getUserAttrs();

        UserAttrDto userAttrDto = getCurrentUserAttrDto(userAttrDtos, UserAttrDto.SPEC_KEY);
        String newKey = UUID.randomUUID().toString();
        if (userAttrDto != null) {
            UserAttrPo userAttrPo = BeanConvertUtil.covertBean(userAttrDto, UserAttrPo.class);
            userAttrPo.setValue(newKey);
            userAttrPo.setStatusCd("0");
            //super.update(context, userAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_USER_ATTR_INFO);
            userAttrV1InnerServiceSMOImpl.updateUserAttr(userAttrPo);
        } else {
            UserAttrPo userAttrPo = new UserAttrPo();
            userAttrPo.setAttrId("-1");
            userAttrPo.setUserId(tmpUserDto.getUserId());
            userAttrPo.setSpecCd(UserAttrDto.SPEC_KEY);
            userAttrPo.setValue(newKey);
            userAttrPo.setStatusCd("0");
            //super.insert(context, userAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_USER_ATTR_INFO);
            userAttrV1InnerServiceSMOImpl.saveUserAttr(userAttrPo);
        }


        try {
            Map userMap = new HashMap();
            userMap.put(CommonConstant.LOGIN_USER_ID, tmpUserDto.getUserId());
            userMap.put(CommonConstant.LOGIN_USER_NAME, tmpUserDto.getUserName());
            String token = AuthenticationFactory.createAndSaveToken(userMap);
            tmpUserDto.setPassword("");
            tmpUserDto.setToken(token);
            tmpUserDto.setKey(newKey);
            context.setResponseEntity(ResultVo.createResponseEntity(tmpUserDto));
        } catch (Exception e) {
            logger.error("登录异常：", e);
            throw new SMOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "系统内部错误，请联系管理员");
        }
    }

    /**
     * 验证码登录，判断是否是否业主，并且是否绑定关系 如果没有 自动绑定关系
     *
     * @param reqJson
     * @param context
     */
    private List<UserDto> ifOwnerLoginByPhone(JSONObject reqJson, ICmdDataFlowContext context) {
        if (!reqJson.containsKey("loginByPhone") || !reqJson.getBoolean("loginByPhone")) {
            return null;
        }
        String appId = context.getReqHeaders().get("app-id");

        //todo 业主手机端
        if (!AppDto.WECHAT_OWNER_APP_ID.equals(appId)
                && !AppDto.WECHAT_MINA_OWNER_APP_ID.equals(appId)
                && !AppDto.OWNER_APP_APP_ID.equals(appId)) {
            return null;
        }

        if (StringUtil.isEmpty(reqJson.getString("userName"))) {
            return null;
        }

        // todo 查询业主或成员
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(reqJson.getString("userName"));
        ownerDto.setPage(1);
        ownerDto.setRow(1);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        // 说明业主不存在 直接返回跑异常
        if (ownerDtos == null || ownerDtos.size() < 1) {
            return null;
        }

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(ownerDtos.get(0).getCommunityId());
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listNotNull(communityDtos, "未包含小区信息");
        CommunityDto tmpCommunityDto = communityDtos.get(0);

        UserPo userPo = new UserPo();
        userPo.setUserId(GenerateCodeFactory.getUserId());
        userPo.setName(ownerDtos.get(0).getName());
        userPo.setTel(ownerDtos.get(0).getLink());
        userPo.setPassword(AuthenticationFactory.passwdMd5(reqJson.getString("password")));
        userPo.setLevelCd(UserLevelConstant.USER_LEVEL_ORDINARY);
        userPo.setAge(ownerDtos.get(0).getAge());
        userPo.setAddress(ownerDtos.get(0).getAddress());
        userPo.setSex(ownerDtos.get(0).getSex());
        int flag = userV1InnerServiceSMOImpl.saveUser(userPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

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
        ownerAppUserPo.setUserId(userPo.getUserId());
        ownerAppUserPo.setOpenId("-1");

        flag = ownerAppUserV1InnerServiceSMOImpl.saveOwnerAppUser(ownerAppUserPo);
        if (flag < 1) {
            throw new CmdException("添加用户业主关系失败");
        }

        UserDto userDto = new UserDto();
        userDto.setUserId(userPo.getUserId());
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);
        return userDtos;
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
}
