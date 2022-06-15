package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.user.UserAttrDto;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.intf.user.IUserAttrV1InnerServiceSMO;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.userAttr.UserAttrPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "owner.refreshAppUserBindingOwnerOpenId")
public class RefreshAppUserBindingOwnerOpenIdCmd extends Cmd {

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserAttrV1InnerServiceSMO userAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "appUserId", "绑定ID不能为空");
        Assert.hasKeyAndValue(reqJson, "openId", "必填，请填写状态");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");
        if (reqJson.getString("appUserId").startsWith("-")) {
            Assert.hasKeyAndValue(reqJson, "oldAppUserId", "必填，请填写老绑定ID");
            Assert.hasKeyAndValue(reqJson, "appType", "必填，请填写appType");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String appUserId = reqJson.getString("appUserId");

        if (appUserId.startsWith("-")) {
            OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
            ownerAppUserDto.setAppUserId(reqJson.getString("oldAppUserId"));
            ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
            List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

            Assert.listOnlyOne(ownerAppUserDtos, "传入oldAppUserId错误");
            OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(ownerAppUserDtos.get(0), OwnerAppUserPo.class);
            ownerAppUserPo.setAppUserId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_userId));
            ownerAppUserPo.setAppType(reqJson.getString("appType"));
            ownerAppUserPo.setOpenId(reqJson.getString("openId"));
            ownerAppUserPo.setNickName(reqJson.getString("nickName"));
            ownerAppUserPo.setHeadImgUrl(reqJson.getString("headImgUrl"));
            ownerAppUserV1InnerServiceSMOImpl.saveOwnerAppUser(ownerAppUserPo);
            freshUserToken(reqJson, ownerAppUserDtos.get(0).getUserId());
            return;
        }

        OwnerAppUserPo ownerAppUserPo = new OwnerAppUserPo();
        ownerAppUserPo.setAppUserId(appUserId);
        ownerAppUserPo.setCommunityId(reqJson.getString("communityId"));
        ownerAppUserPo.setOpenId(reqJson.getString("openId"));
        ownerAppUserPo.setNickName(reqJson.getString("nickName"));
        ownerAppUserPo.setHeadImgUrl(reqJson.getString("headImgUrl"));
        ownerAppUserV1InnerServiceSMOImpl.updateOwnerAppUser(ownerAppUserPo);

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setAppUserId(appUserId);
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        Assert.listOnlyOne(ownerAppUserDtos, "传入appUserId错误");


        freshUserToken(reqJson, ownerAppUserDtos.get(0).getUserId());
    }

    private void freshUserToken(JSONObject reqJson, String userId) {
        //判断 用户下是否有openId 属性有修改没有添加
        if (!reqJson.containsKey("openId")) {
            return;
        }


        UserAttrDto userAttrDto = new UserAttrDto();
        userAttrDto.setUserId(userId);
        userAttrDto.setSpecCd(UserAttrDto.SPEC_OPEN_ID);
        List<UserAttrDto> userAttrDtos = userAttrV1InnerServiceSMOImpl.queryUserAttrs(userAttrDto);
        if (userAttrDtos == null || userAttrDtos.size() < 1) {
            UserAttrPo userAttrPo = new UserAttrPo();
            userAttrPo.setUserId(userId);
            userAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            userAttrPo.setSpecCd(UserAttrDto.SPEC_OPEN_ID);
            userAttrPo.setValue(reqJson.getString("openId"));
            userAttrV1InnerServiceSMOImpl.saveUserAttr(userAttrPo);
            return;
        }
        UserAttrPo userAttrPo = new UserAttrPo();
        userAttrPo.setUserId(userId);
        userAttrPo.setAttrId(userAttrDtos.get(0).getAttrId());
        userAttrPo.setSpecCd(UserAttrDto.SPEC_OPEN_ID);
        userAttrPo.setValue(reqJson.getString("openId"));
        userAttrV1InnerServiceSMOImpl.updateUserAttr(userAttrPo);

        if (!reqJson.containsKey("unionId")) {
            return;
        }

        userAttrDto = new UserAttrDto();
        userAttrDto.setUserId(userId);
        userAttrDto.setSpecCd(UserAttrDto.SPEC_UNION_ID);
        userAttrDtos = userAttrV1InnerServiceSMOImpl.queryUserAttrs(userAttrDto);
        if (userAttrDtos == null || userAttrDtos.size() < 1) {
            userAttrPo = new UserAttrPo();
            userAttrPo.setUserId(userId);
            userAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            userAttrPo.setSpecCd(UserAttrDto.SPEC_UNION_ID);
            userAttrPo.setValue(reqJson.getString("unionId"));
            userAttrV1InnerServiceSMOImpl.saveUserAttr(userAttrPo);
            return;
        }
        userAttrPo = new UserAttrPo();
        userAttrPo.setUserId(userId);
        userAttrPo.setAttrId(userAttrDtos.get(0).getAttrId());
        userAttrPo.setSpecCd(UserAttrDto.SPEC_UNION_ID);
        userAttrPo.setValue(reqJson.getString("unionId"));
        userAttrV1InnerServiceSMOImpl.updateUserAttr(userAttrPo);


    }
}
