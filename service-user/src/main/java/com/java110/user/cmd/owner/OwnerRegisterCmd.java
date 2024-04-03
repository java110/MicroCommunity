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
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.user.UserAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.ISmsInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.user.UserPo;
import com.java110.po.user.UserAttrPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.UserLevelConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
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

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "link", "未包含联系电话");
        Assert.hasKeyAndValue(reqJson, "msgCode", "未包含联系电话验证码");
        Assert.hasKeyAndValue(reqJson, "password", "未包含密码");

        UserDto userDto = new UserDto();
        userDto.setTel(reqJson.getString("link"));
        userDto.setLevelCd(UserDto.LEVEL_CD_USER);
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        if (!ListUtil.isNull(userDtos)) {
            throw new CmdException("手机号已存在，请登陆");
        }

        SmsDto smsDto = new SmsDto();
        smsDto.setTel(reqJson.getString("link"));
        smsDto.setCode(reqJson.getString("msgCode"));
        smsDto = smsInnerServiceSMOImpl.validateCode(smsDto);

        if (!smsDto.isSuccess() && "ON".equals(MappingCache.getValue(MappingConstant.SMS_DOMAIN, SendSmsFactory.SMS_SEND_SWITCH))) {
            throw new IllegalArgumentException(smsDto.getMsg());
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {


        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(reqJson.getString("link"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        //设置默认密码
        String userPassword = reqJson.getString("password");
        userPassword = AuthenticationFactory.passwdMd5(userPassword);
        String name = reqJson.getString("link");
        if (!ListUtil.isNull(ownerDtos)) {
            name = ownerDtos.get(0).getName();
        }
        //todo 注册用户
        UserPo userPo = new UserPo();
        userPo.setAddress("无");
        userPo.setUserId(GenerateCodeFactory.getUserId());
        userPo.setLevelCd(UserLevelConstant.USER_LEVEL_ORDINARY);
        userPo.setName(name);
        userPo.setTel(reqJson.getString("link"));
        userPo.setPassword(userPassword);

        int flag = userV1InnerServiceSMOImpl.saveUser(userPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }
        //todo 保存openId
        String openId = reqJson.getString("openId");
        if (!StringUtil.isEmpty(openId)) {
            UserAttrPo userAttrPo = new UserAttrPo();
            userAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            userAttrPo.setSpecCd(UserAttrDto.SPEC_OPEN_ID);
            userAttrPo.setUserId(userPo.getUserId());
            userAttrPo.setValue(openId);
            flag = userAttrV1InnerServiceSMOImpl.saveUserAttr(userAttrPo);
            if (flag < 1) {
                throw new CmdException("注册失败");
            }
        }

        //todo 根据手机号未关联到业主直接返回成功，后续通过认证房屋的方式操作
        if (ListUtil.isNull(ownerDtos)) {
            return;
        }
        String appId = cmdDataFlowContext.getReqHeaders().get("app-id");
        String appType = "";
        if (AppDto.WECHAT_OWNER_APP_ID.equals(appId)) { //公众号
            appType = OwnerAppUserDto.APP_TYPE_WECHAT;
        } else if (AppDto.WECHAT_MINA_OWNER_APP_ID.equals(appId)) { //小程序
            appType = OwnerAppUserDto.APP_TYPE_WECHAT_MINA;
        } else {//app
            appType = OwnerAppUserDto.APP_TYPE_APP;
        }

        OwnerAppUserPo ownerAppUserPo = null;

        List<CommunityDto> communityDtos = null;
        for (OwnerDto tmpOwnerDto : ownerDtos) {
            CommunityDto communityDto = new CommunityDto();
            communityDto.setState("1100");
            communityDto.setCommunityId(tmpOwnerDto.getCommunityId());
            communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
            if (ListUtil.isNull(communityDtos)) {
                continue;
            }
            communityDto = communityDtos.get(0);
            ownerAppUserPo = new OwnerAppUserPo();
            ownerAppUserPo.setAppUserId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appUserId));
            ownerAppUserPo.setMemberId(tmpOwnerDto.getMemberId());
            ownerAppUserPo.setCommunityId(tmpOwnerDto.getCommunityId());
            ownerAppUserPo.setCommunityName(communityDto.getName());
            ownerAppUserPo.setAppUserName(tmpOwnerDto.getName());
            ownerAppUserPo.setIdCard(tmpOwnerDto.getIdCard());
            ownerAppUserPo.setLink(tmpOwnerDto.getLink());
            ownerAppUserPo.setOpenId("-1");
            ownerAppUserPo.setAppTypeCd("10010");
            ownerAppUserPo.setState(OwnerAppUserDto.STATE_AUDIT_SUCCESS);
            ownerAppUserPo.setRemark("注册自动关联");
            ownerAppUserPo.setUserId(userPo.getUserId());
            ownerAppUserPo.setAppType(appType);
            ownerAppUserPo.setOwnerTypeCd(tmpOwnerDto.getOwnerTypeCd());
            queryOwnerRoom(tmpOwnerDto, ownerAppUserPo);
            flag = ownerAppUserV1InnerServiceSMOImpl.saveOwnerAppUser(ownerAppUserPo);
            if (flag < 1) {
                throw new CmdException("添加用户业主关系失败");
            }
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    private void queryOwnerRoom(OwnerDto ownerDto, OwnerAppUserPo ownerAppUserPo) {


        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(ownerDto.getOwnerId());

        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if (ListUtil.isNull(ownerRoomRelDtos)) {
            return;
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(ownerRoomRelDtos.get(0).getRoomId());
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        if (ListUtil.isNull(roomDtos)) {
            return;
        }

        ownerAppUserPo.setRoomId(roomDtos.get(0).getRoomId());
        ownerAppUserPo.setRoomName(roomDtos.get(0).getFloorNum() + "-" + roomDtos.get(0).getUnitNum() + "-" + roomDtos.get(0).getRoomNum());
    }


}
