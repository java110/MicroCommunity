package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.account.AccountPo;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.owner.OwnerPo;
import com.java110.po.user.UserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 业主房屋认证审核
 */
@Java110Cmd(serviceCode = "owner.auditAuthOwner")
public class AuditAuthOwnerCmd extends Cmd {

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "appUserId", "绑定ID不能为空");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写状态");
        Assert.hasKeyAndValue(reqJson, "remark", "必填，请填写审核说明");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setAppUserId(reqJson.getString("appUserId"));
        ownerAppUserDto.setState(OwnerAppUserDto.STATE_AUDITING);
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        Assert.listOnlyOne(ownerAppUserDtos, "审核记录不存在");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(context);

        String state = "";
        // todo 审核拒绝
        if ("1200".equals(reqJson.getString("state"))) {
            OwnerAppUserPo ownerAppUserPo = new OwnerAppUserPo();
            ownerAppUserPo.setAppUserId(reqJson.getString("appUserId"));
            ownerAppUserPo.setState(OwnerAppUserDto.STATE_AUDIT_ERROR);
            ownerAppUserPo.setRemark(reqJson.getString("remark"));
            int flag = ownerAppUserV1InnerServiceSMOImpl.updateOwnerAppUser(ownerAppUserPo);
            if (flag < 1) {
                throw new CmdException("修改绑定失败");
            }
            return;
        }

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setAppUserId(reqJson.getString("appUserId"));
        ownerAppUserDto.setState(OwnerAppUserDto.STATE_AUDITING);
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        String memberId = "";
        // todo 如果是业主
        if(OwnerDto.OWNER_TYPE_CD_OWNER.equals(ownerAppUserDtos.get(0).getOwnerTypeCd())){
            memberId = bindOwner(ownerAppUserDtos.get(0));
        }else {
            //todo 如果是成员
            memberId = bindOwnerMember(ownerAppUserDtos.get(0),userId);
        }


        UserPo userPo = new UserPo();
        userPo.setName(ownerAppUserDtos.get(0).getAppUserName());
        userPo.setUserId(ownerAppUserDtos.get(0).getUserId());
        userV1InnerServiceSMOImpl.updateUser(userPo);

        OwnerAppUserPo ownerAppUserPo = new OwnerAppUserPo();
        ownerAppUserPo.setAppUserId(reqJson.getString("appUserId"));
        ownerAppUserPo.setState(OwnerAppUserDto.STATE_AUDIT_SUCCESS);
        ownerAppUserPo.setRemark(reqJson.getString("remark"));
        ownerAppUserPo.setMemberId(memberId);
        int flag = ownerAppUserV1InnerServiceSMOImpl.updateOwnerAppUser(ownerAppUserPo);
        if (flag < 1) {
            throw new CmdException("修改绑定失败");
        }

    }

    /**
     * 绑定成员
     * @param ownerAppUserDto
     * @return
     */
    private String bindOwnerMember(OwnerAppUserDto ownerAppUserDto,String userId) {
        // todo 查看 房屋是否有业主，如果没有添加业主
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(ownerAppUserDto.getRoomId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if(ListUtil.isNull(ownerRoomRelDtos)){
            throw new CmdException("房屋未绑定业主");
        }

        //todo 根据手机号和小区查询 家庭成员是否存在

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(ownerRoomRelDtos.get(0).getOwnerId());
        ownerDto.setLink(ownerAppUserDto.getLink());
        ownerDto.setCommunityId(ownerAppUserDto.getCommunityId());
        ownerDto.setOwnerTypeCds(new String[]{
                OwnerDto.OWNER_TYPE_CD_MEMBER,
                OwnerDto.OWNER_TYPE_CD_RENTING,
                OwnerDto.OWNER_TYPE_CD_OTHER,
                OwnerDto.OWNER_TYPE_CD_TEMP
        });
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        if(!ListUtil.isNull(ownerDtos)){
            return ownerDtos.get(0).getMemberId();
        }

        OwnerPo ownerPo = new OwnerPo();
        ownerPo.setMemberId(GenerateCodeFactory.getGeneratorId("11"));
        ownerPo.setOwnerId(ownerRoomRelDtos.get(0).getOwnerId());
        ownerPo.setName(ownerAppUserDto.getAppUserName());
        ownerPo.setSex("0");
        ownerPo.setLink(ownerAppUserDto.getLink());
        ownerPo.setUserId(userId);
        ownerPo.setRemark("业主认证，审核添加");
        ownerPo.setOwnerTypeCd(ownerAppUserDto.getOwnerTypeCd());
        ownerPo.setCommunityId(ownerAppUserDto.getCommunityId());
        ownerPo.setIdCard(ownerAppUserDto.getIdCard());
        ownerPo.setState(OwnerDto.STATE_FINISH);
        ownerPo.setOwnerFlag(OwnerDto.OWNER_FLAG_TRUE);
        ownerPo.setAddress("无");
        ownerV1InnerServiceSMOImpl.saveOwner(ownerPo);

        return ownerPo.getMemberId();
    }

    /**
     * 绑定业主
     * @param ownerAppUserDto
     * @return
     */
    private String bindOwner(OwnerAppUserDto ownerAppUserDto) {

        // todo 查看 房屋是否有业主，如果没有添加业主
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(ownerAppUserDto.getRoomId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if(ListUtil.isNull(ownerRoomRelDtos)){
            throw new CmdException("房屋未绑定业主");
        }

        OwnerPo ownerPo = new OwnerPo();
        ownerPo.setLink(ownerAppUserDto.getLink());
       // ownerPo.setName(ownerAppUserDto.getAppUserName());
        ownerPo.setMemberId(ownerRoomRelDtos.get(0).getOwnerId());
        ownerPo.setOwnerId(ownerRoomRelDtos.get(0).getOwnerId());
        ownerV1InnerServiceSMOImpl.updateOwner(ownerPo);

        AccountDto accountDto = new AccountDto();
        accountDto.setObjId(ownerRoomRelDtos.get(0).getOwnerId());
        accountDto.setPartId(ownerRoomRelDtos.get(0).getCommunityId());
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
        if (ListUtil.isNull(accountDtos)) {
            return ownerRoomRelDtos.get(0).getOwnerId();
        }

        AccountPo accountPo = new AccountPo();
        accountPo.setoLink(ownerAppUserDto.getLink());
        accountPo.setAcctId(accountDtos.get(0).getAcctId());
        accountInnerServiceSMOImpl.updateAccount(accountPo);


        return ownerRoomRelDtos.get(0).getOwnerId();

    }
}
