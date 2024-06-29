package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.Environment;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.owner.OwnerPo;
import com.java110.user.cmd.question.ListUserQuestionAnswerCmd;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110CmdDoc(title = "删除业主成员",
        description = "第三方系统，比如招商系统删除业主信息",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/owner.deleteOwnerMember",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "owner.deleteOwnerMember",
        seq = 11
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "memberId", length = 30, remark = "业主ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody = "{\n" +
                "\t\"memberId\": 123123123,\n" +
                "\t\"communityId\": \"2022121921870161\"\n" +
                "}",
        resBody = "{\"code\":0,\"msg\":\"成功\"}"
)
@Java110Cmd(serviceCode = "owner.deleteOwnerMember")
public class DeleteOwnerMemberCmd extends Cmd {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Environment.isDevEnv();

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userId);
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (ListUtil.isNull(ownerAppUserDtos)) {
            throw new CmdException("业主不存在");
        }

        if (StringUtil.isEmpty(ownerAppUserDtos.get(0).getMemberId())) {
            throw new CmdException("业主不存在");
        }

        Assert.jsonObjectHaveKey(reqJson, "memberId", "请求报文中未包含memberId");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");


        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("memberId"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setOwnerId(ownerAppUserDtos.get(0).getMemberId());
        ownerDto.setOwnerTypeCds(new String[]{OwnerDto.OWNER_TYPE_CD_MEMBER, OwnerDto.OWNER_TYPE_CD_RENTING,
                OwnerDto.OWNER_TYPE_CD_TEMP, OwnerDto.OWNER_TYPE_CD_OTHER});

        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        Assert.listOnlyOne(ownerDtos, "业主成员不存在");


    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        JSONObject businessOwner = new JSONObject();
        businessOwner.put("memberId", reqJson.getString("memberId"));
        businessOwner.put("communityId", reqJson.getString("communityId"));

        OwnerPo ownerPo = BeanConvertUtil.covertBean(businessOwner, OwnerPo.class);
        int flag = ownerV1InnerServiceSMOImpl.deleteOwner(ownerPo);

        if (flag < 1) {
            throw new CmdException("删除失败");
        }
//        if (!OwnerDto.OWNER_TYPE_CD_OWNER.equals(reqJson.getString("ownerTypeCd"))) {
//            return;
//        }
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setMemberId(reqJson.getString("memberId"));
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        //查询app用户表
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        if (ownerAppUserDtos == null && ownerAppUserDtos.size() < 1) {
            return;
        }

        for (OwnerAppUserDto ownerAppUser : ownerAppUserDtos) {
            OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(ownerAppUser, OwnerAppUserPo.class);
            flag = ownerAppUserV1InnerServiceSMOImpl.deleteOwnerAppUser(ownerAppUserPo);
            if (flag < 1) {
                throw new CmdException("删除失败");
            }

            //todo 应该删除用户
//            UserPo userPo = new UserPo();
//            userPo.setUserId(ownerAppUser.getUserId());
//            userV1InnerServiceSMOImpl.deleteUser(userPo);
        }

    }
}
