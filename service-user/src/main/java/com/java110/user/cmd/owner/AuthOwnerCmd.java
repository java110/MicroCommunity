package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.user.UserAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "owner.authOwner")
public class AuthOwnerCmd extends Cmd {

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IUserAttrV1InnerServiceSMO userAttrV1InnerServiceSMOImpl;

    /**
     * {"communityId":"2023052267100146","roomName":"1-1-888","roomId":"752024021967653523","link":"15509711111","ownerName":"张三","ownerTypeCd":"1001"}
     *
     * @param event   事件对象
     * @param context 请求报文数据
     * @param reqJson
     * @throws CmdException
     * @throws ParseException
     */
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "roomName", "未包含房屋");
        Assert.hasKeyAndValue(reqJson, "roomId", "未包含房屋");
        Assert.hasKeyAndValue(reqJson, "link", "未包含手机号");
        Assert.hasKeyAndValue(reqJson, "ownerName", "未包含人员名称");
        Assert.hasKeyAndValue(reqJson, "ownerTypeCd", "未包含人员类型");

        //todo 根据手机号查询 是否已经认证过，如果认证过则不能再次认证

//        OwnerDto ownerDto = new OwnerDto();
//        ownerDto.setLink(reqJson.getString("link"));
//        ownerDto.setCommunityId(reqJson.getString("communityId"));
//        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
//        if(ListUtil.isNull(ownerDtos)){
//            return;
//        }

        String userId = CmdContextUtils.getUserId(context);
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户未登录");
        if (!userDtos.get(0).getTel().equals(reqJson.getString("link"))) {
            throw new CmdException("手机号错误，不是注册时的手机号");
        }

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setLink(reqJson.getString("link"));
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        ownerAppUserDto.setStates(new String[]{
                OwnerAppUserDto.STATE_AUDITING,
                OwnerAppUserDto.STATE_AUDIT_SUCCESS
        });
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (!ListUtil.isNull(ownerAppUserDtos)) {
            throw new CmdException("已存在认证关系，请勿重复认证");
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(reqJson.getString("roomId"));
        roomDto.setCommunityId(reqJson.getString("communityId"));
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
        Assert.listOnlyOne(roomDtos, "房屋不存在");

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(roomDtos.get(0).getRoomId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if (ListUtil.isNull(ownerRoomRelDtos)) {
            throw new CmdException("房屋未销售，请联系物业");
        }


        if (!OwnerDto.OWNER_TYPE_CD_OWNER.equals(reqJson.getString("ownerTypeCd"))) {
            return;
        }

        ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setMemberId(ownerRoomRelDtos.get(0).getOwnerId());
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        ownerAppUserDto.setStates(new String[]{
                OwnerAppUserDto.STATE_AUDITING,
                OwnerAppUserDto.STATE_AUDIT_SUCCESS
        });
        ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (!ListUtil.isNull(ownerAppUserDtos)) {
            throw new CmdException("业主已经被认证");
        }

    }

    /**
     * {"communityId":"2023052267100146","roomName":"1-1-888","roomId":"752024021967653523","link":"15509711111","ownerName":"张三","ownerTypeCd":"1001"}
     *
     * @param event   事件对象
     * @param context 数据上文对象
     * @param reqJson 请求报文
     * @throws CmdException
     * @throws ParseException
     */
    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String userId = CmdContextUtils.getUserId(context);

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(reqJson.getString("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listNotNull(communityDtos, "未包含小区信息");
        CommunityDto tmpCommunityDto = communityDtos.get(0);


        OwnerAppUserPo ownerAppUserPo = new OwnerAppUserPo();
        //状态类型，10000 审核中，12000 审核成功，13000 审核失败
        ownerAppUserPo.setState(OwnerAppUserDto.STATE_AUDITING);
        ownerAppUserPo.setAppTypeCd("10010");
        ownerAppUserPo.setAppUserId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appUserId));
        ownerAppUserPo.setMemberId("-1");
        ownerAppUserPo.setCommunityName(tmpCommunityDto.getName());
        ownerAppUserPo.setCommunityId(tmpCommunityDto.getCommunityId());
        ownerAppUserPo.setAppUserName(reqJson.getString("ownerName"));
        ownerAppUserPo.setAppType("WECHAT");
        ownerAppUserPo.setLink(reqJson.getString("link"));
        ownerAppUserPo.setUserId(userId);
        ownerAppUserPo.setOpenId("-1");
        ownerAppUserPo.setRoomId(reqJson.getString("roomId"));
        ownerAppUserPo.setRoomName(reqJson.getString("roomName"));
        ownerAppUserPo.setOwnerTypeCd(reqJson.getString("ownerTypeCd"));

        UserAttrDto userAttrDto = new UserAttrDto();
        userAttrDto.setUserId(userId);
        userAttrDto.setSpecCd(UserAttrDto.SPEC_OPEN_ID);
        List<UserAttrDto> userAttrDtos = userAttrV1InnerServiceSMOImpl.queryUserAttrs(userAttrDto);
        if (!ListUtil.isNull(userAttrDtos)) {
            ownerAppUserPo.setOpenId(userAttrDtos.get(0).getValue());

        }

        ownerAppUserV1InnerServiceSMOImpl.saveOwnerAppUser(ownerAppUserPo);
    }
}
