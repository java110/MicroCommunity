package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.SendSmsFactory;
import com.java110.core.smo.IPhotoSMO;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.msg.SmsDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.ISmsInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.owner.OwnerAttrPo;
import com.java110.po.owner.OwnerPo;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.po.user.UserPo;
import com.java110.user.bmo.owner.IGeneratorOwnerUserBMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.UserLevelConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 房屋添加业主
 */
@Java110Cmd(serviceCode = "owner.saveRoomOwner")
public class SaveRoomOwnerCmd extends Cmd {


    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerAttrInnerServiceSMO ownerAttrInnerServiceSMOImpl;


    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private IPhotoSMO photoSMOImpl;


    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IGeneratorOwnerUserBMO generatorOwnerUserBMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(reqJson, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(reqJson, "roomName", "请求报文中未包含房屋");
        Assert.jsonObjectHaveKey(reqJson, "ownerTypeCd", "请求报文中未包含类型");

        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");

        //todo 校验手机号重复
        String userValidate = MappingCache.getValue("USER_VALIDATE");
        if ("ON".equals(userValidate)) {
            String link = reqJson.getString("link");
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setLink(link);
            ownerDto.setCommunityId(reqJson.getString("communityId"));
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryAllOwners(ownerDto);
            Assert.listIsNull(ownerDtos, "手机号重复，请重新输入");
        }

        //todo 属性校验
        Assert.judgeAttrValue(reqJson);

        //todo 校验房屋
        String roomName = reqJson.getString("roomName");
        String[] roomNames = roomName.split("-", 3);

        if (roomNames == null || roomNames.length != 3) {
            throw new CmdException("房屋格式错误，楼栋-单元-房屋");
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setFloorNum(roomNames[0]);
        roomDto.setUnitNum(roomNames[1]);
        roomDto.setRoomNum(roomNames[2]);
        roomDto.setCommunityId(reqJson.getString("communityId"));
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);

        if (roomDtos == null || roomDtos.size() < 1) {
            throw new CmdException("房屋不存在");
        }

        reqJson.put("roomId", roomDtos.get(0).getRoomId());

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(roomDtos.get(0).getRoomId());

        List<OwnerRoomRelDto> roomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);


        String ownerTypeCd = reqJson.getString("ownerTypeCd");
        //todo 业主 如果存在 则失败
        if (OwnerDto.OWNER_TYPE_CD_OWNER.equals(ownerTypeCd) && roomRelDtos != null && roomRelDtos.size() > 0) {
            throw new CmdException("已经存在业主");
        }

        //todo  业主直接返回
        if (OwnerDto.OWNER_TYPE_CD_OWNER.equals(ownerTypeCd)) {
            return;
        }

        //todo 不是业主，业主 不存在 则失败
        if (!OwnerDto.OWNER_TYPE_CD_OWNER.equals(ownerTypeCd) && (roomRelDtos == null || roomRelDtos.size() < 1)) {
            throw new CmdException("业主不存在 先添加业主");
        }

        reqJson.put("ownerId",roomRelDtos.get(0).getOwnerId());

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String memberId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId);
        String ownerId = memberId;
        String ownerTypeCd = reqJson.getString("ownerTypeCd");
        if (!OwnerDto.OWNER_TYPE_CD_OWNER.equals(ownerTypeCd)) {
            ownerId = reqJson.getString("ownerId");
        }
        // todo 保存业主
        OwnerPo ownerPo = BeanConvertUtil.covertBean(reqJson,OwnerPo.class);
        ownerPo.setMemberId(memberId);
        ownerPo.setOwnerId(ownerId);
        ownerPo.setState(OwnerDto.STATE_FINISH);
        ownerPo.setUserId(CmdContextUtils.getUserId(context));
        ownerPo.setAge(null);
        ownerPo.setOwnerFlag(OwnerDto.OWNER_FLAG_TRUE);

        int flag = ownerV1InnerServiceSMOImpl.saveOwner(ownerPo);
        if (flag < 1) {
            throw new CmdException("保存业主失败");
        }

        //todo 保存照片
        photoSMOImpl.savePhoto(reqJson.getString("ownerPhoto"),
                memberId,
                reqJson.getString("communityId"),
                "10000");
        //todo 保存属性
        dealOwnerAttr(reqJson, memberId);

        //todo 保存和房屋关系
        saveOwnerRoomRel(ownerPo,reqJson.getString("roomId"));

        //todo 生成登录账号
        generatorOwnerUserBMOImpl.generator(ownerPo);

    }

    /**
     * 保存 业主房屋关系
     * @param ownerPo
     * @param roomId
     */
    private void saveOwnerRoomRel(OwnerPo ownerPo, String roomId) {
        //todo 不是业主就跳过
        if(!OwnerDto.OWNER_TYPE_CD_OWNER.equals(ownerPo.getOwnerTypeCd())){
            return;
        }

        OwnerRoomRelPo ownerRoomRelPo = new OwnerRoomRelPo();
        ownerRoomRelPo.setRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
        ownerRoomRelPo.setUserId("-1");
        ownerRoomRelPo.setOwnerId(ownerPo.getOwnerId());
        ownerRoomRelPo.setRoomId(roomId);
        ownerRoomRelPo.setState("2001");
        ownerRoomRelPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        ownerRoomRelPo.setEndTime(DateUtil.getLastTime());
        ownerRoomRelV1InnerServiceSMOImpl.saveOwnerRoomRel(ownerRoomRelPo);
    }

    private void dealOwnerAttr(JSONObject paramObj, String memberId) {

        if (!paramObj.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = paramObj.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }

        int flag = 0;
        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("communityId", paramObj.getString("communityId"));
            attr.put("memberId", memberId);
            attr.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            OwnerAttrPo ownerAttrPo = BeanConvertUtil.covertBean(attr, OwnerAttrPo.class);
            flag = ownerAttrInnerServiceSMOImpl.saveOwnerAttr(ownerAttrPo);
            if (flag < 1) {
                throw new CmdException("保存业主房屋关系失败");
            }
        }

    }
}
