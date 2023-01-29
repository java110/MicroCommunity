package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.SendSmsFactory;
import com.java110.dto.app.AppDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.msg.SmsDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.common.ISmsInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.user.UserPo;
import com.java110.po.userAttr.UserAttrPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.UserLevelConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 服务注册功能迁移
 */
@Java110Cmd(serviceCode = "owner.ownerRegister")
public class OwnerRegisterCmd extends Cmd {
    private final static Logger logger = LoggerFactory.getLogger(OwnerRegisterCmd.class);
    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IUserAttrV1InnerServiceSMO userAttrV1InnerServiceSMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private ISmsInnerServiceSMO smsInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "link", "未包含联系电话");
        Assert.hasKeyAndValue(reqJson, "msgCode", "未包含联系电话验证码");
        Assert.hasKeyAndValue(reqJson, "password", "未包含密码");

        SmsDto smsDto = new SmsDto();
        smsDto.setTel(reqJson.getString("link"));
        smsDto.setCode(reqJson.getString("msgCode"));
        smsDto = smsInnerServiceSMOImpl.validateCode(smsDto);

        if (!smsDto.isSuccess() && "ON".equals(MappingCache.getValue(MappingConstant.SMS_DOMAIN,SendSmsFactory.SMS_SEND_SWITCH))) {
            throw new IllegalArgumentException(smsDto.getMsg());
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        OwnerAppUserDto ownerAppUserDto = BeanConvertUtil.covertBean(reqJson, OwnerAppUserDto.class);
        ownerAppUserDto.setStates(new String[]{"10000", "12000"});

        //是否已经注册过
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (ownerAppUserDtos != null && ownerAppUserDtos.size() > 0) {
            throw new IllegalArgumentException("已经注册过用户");
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(reqJson.getString("link"));

        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        String appId = cmdDataFlowContext.getReqHeaders().get("app-id");
        if (AppDto.WECHAT_OWNER_APP_ID.equals(appId)) { //公众号
            reqJson.put("appType", OwnerAppUserDto.APP_TYPE_WECHAT);
        } else if (AppDto.WECHAT_MINA_OWNER_APP_ID.equals(appId)) { //小程序
            reqJson.put("appType", OwnerAppUserDto.APP_TYPE_WECHAT_MINA);
        } else {//app
            reqJson.put("appType", OwnerAppUserDto.APP_TYPE_APP);
        }
        reqJson.put("userId", GenerateCodeFactory.getUserId());
        if (reqJson.containsKey("openId")) {
            reqJson.put("openId", reqJson.getString("openId"));
        } else {
            reqJson.put("openId", "-1");
        }
        //添加小区楼
        addOwnerAppUser(reqJson, ownerDtos);
        registerUser(reqJson, ownerDtos);

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    private void addOwnerAppUser(JSONObject paramInJson, List<OwnerDto> ownerDtos) {
        List<CommunityDto> communityDtos = null;
        CommunityDto tmpCommunityDto = null;
        String communityName = "无";

        if (ownerDtos == null || ownerDtos.size() < 1) {
            CommunityDto communityDto = new CommunityDto();
            communityDto.setState("1100");
            communityDto.setCommunityId(paramInJson.getString("defaultCommunityId"));
            communityDto.setPage(1);
            communityDto.setRow(1);
            communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
            if (communityDtos != null && communityDtos.size() > 0) {
                communityName = communityDtos.get(0).getName();
            }
            OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(paramInJson, OwnerAppUserPo.class);
            //状态类型，10000 审核中，12000 审核成功，13000 审核失败
            ownerAppUserPo.setState("12000");
            ownerAppUserPo.setAppTypeCd("10010");
            ownerAppUserPo.setAppUserId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appUserId));
            ownerAppUserPo.setMemberId("-1");
            ownerAppUserPo.setCommunityName(communityName);
            ownerAppUserPo.setCommunityId(paramInJson.getString("defaultCommunityId"));
            ownerAppUserPo.setAppUserName("游客");
            ownerAppUserPo.setIdCard("无");

            int flag = ownerAppUserV1InnerServiceSMOImpl.saveOwnerAppUser(ownerAppUserPo);
            if (flag < 1) {
                throw new CmdException("添加用户业主关系失败");
            }
            return;
        }

        OwnerAppUserPo ownerAppUserPo = null;
        for (OwnerDto ownerDto : ownerDtos) {
            CommunityDto communityDto = new CommunityDto();
            communityDto.setState("1100");
            communityDto.setCommunityId(ownerDto.getCommunityId());
            communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
            if (communityDtos == null || communityDtos.size() < 1) {
                continue;
            }
            tmpCommunityDto = communityDtos.get(0);
            ownerAppUserPo = BeanConvertUtil.covertBean(paramInJson, OwnerAppUserPo.class);
            //状态类型，10000 审核中，12000 审核成功，13000 审核失败
            ownerAppUserPo.setState("12000");
            ownerAppUserPo.setAppTypeCd("10010");
            ownerAppUserPo.setAppUserId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appUserId));
            ownerAppUserPo.setMemberId(ownerDto.getMemberId());
            ownerAppUserPo.setCommunityName(tmpCommunityDto.getName());
            ownerAppUserPo.setCommunityId(tmpCommunityDto.getCommunityId());
            ownerAppUserPo.setAppUserName(ownerDto.getName());
            ownerAppUserPo.setIdCard(ownerDto.getIdCard());

            int flag = ownerAppUserV1InnerServiceSMOImpl.saveOwnerAppUser(ownerAppUserPo);
            if (flag < 1) {
                throw new CmdException("添加用户业主关系失败");
            }
        }
    }

    /**
     * 注册用户
     *
     * @param paramObj
     */
    public void registerUser(JSONObject paramObj, List<OwnerDto> ownerDtos) {

        if (paramObj.containsKey("email") && !StringUtil.isEmpty(paramObj.getString("email"))) {
            Assert.isEmail(paramObj, "email", "不是有效的邮箱格式");
        }

        paramObj.put("levelCd", UserLevelConstant.USER_LEVEL_ORDINARY);
        //设置默认密码
        String userPassword = paramObj.getString("password");
        userPassword = AuthenticationFactory.passwdMd5(userPassword);
        paramObj.put("password", userPassword);

        String tel = paramObj.getString("link");
        String name = tel;
        if (ownerDtos != null && ownerDtos.size() > 0) {
            name = ownerDtos.get(0).getName();
        }
        UserPo userPo = BeanConvertUtil.covertBean(paramObj, UserPo.class);
        userPo.setName(name);
        userPo.setTel(tel);
        int flag = userV1InnerServiceSMOImpl.saveUser(userPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

        if (!paramObj.containsKey("openId") || "-1".equals(paramObj.getString("openId"))) {
            return;
        }
        JSONObject userAttrObj = new JSONObject();
        userAttrObj.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        userAttrObj.put("specCd", "100201911001");
        userAttrObj.put("value", paramObj.getString("openId"));
        UserAttrPo userAttrPo = BeanConvertUtil.covertBean(userAttrObj, UserAttrPo.class);
        userAttrPo.setUserId(userPo.getUserId());
        flag = userAttrV1InnerServiceSMOImpl.saveUserAttr(userAttrPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

    }
}
