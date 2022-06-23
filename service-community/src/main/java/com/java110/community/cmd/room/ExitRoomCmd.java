package com.java110.community.cmd.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.po.room.RoomPo;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "room.exitRoom")
public class ExitRoomCmd extends Cmd {


    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "ownerId", "请求报文中未包含ownerId节点");
        Assert.jsonObjectHaveKey(reqJson, "roomId", "请求报文中未包含roomId节点");
        Assert.jsonObjectHaveKey(reqJson, "storeId", "请求报文中未包含storeId节点");


        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(reqJson.getString("ownerId"), "ownerId不能为空");
        Assert.hasLength(reqJson.getString("roomId"), "roomId不能为空");
        Assert.hasLength(reqJson.getString("storeId"), "storeId不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //根据ownerId 和 roomId 查询relId 删除
        OwnerRoomRelDto ownerRoomRelDto = BeanConvertUtil.covertBean(reqJson, OwnerRoomRelDto.class);
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "数据存在问题，业主和房屋对应关系不是一条");
        }


        JSONObject businessUnit = new JSONObject();
        //businessUnit.putAll(paramInJson);
        businessUnit.put("relId", ownerRoomRelDtos.get(0).getRelId());
        //businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        OwnerRoomRelPo roomPo = BeanConvertUtil.covertBean(businessUnit, OwnerRoomRelPo.class);

        int flag = ownerRoomRelV1InnerServiceSMOImpl.deleteOwnerRoomRel(roomPo);

        if (flag < 1) {
            throw new IllegalArgumentException("删除业主房屋关系失败");
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(reqJson.getString("roomId"));
        roomDto.setCommunityId(reqJson.getString("communityId"));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "房屋或商铺不存在");
        if (RoomDto.ROOM_TYPE_SHOPS.equals(roomDtos.get(0).getRoomType())) {
            reqJson.put("state", RoomDto.STATE_SHOP_FREE);
        } else {
            reqJson.put("state", "2002");
        }


        Assert.listOnlyOne(roomDtos, "存在" + roomDtos.size() + "条房屋信息");

        businessUnit = new JSONObject();
        businessUnit.putAll(BeanConvertUtil.beanCovertMap(roomDtos.get(0)));
        businessUnit.putAll(reqJson);
        RoomPo tmpRoomPo = BeanConvertUtil.covertBean(businessUnit, RoomPo.class);
        flag = roomV1InnerServiceSMOImpl.updateRoom(tmpRoomPo);

        if (flag < 1) {
            throw new IllegalArgumentException("更新房屋状态");
        }

    }
}
