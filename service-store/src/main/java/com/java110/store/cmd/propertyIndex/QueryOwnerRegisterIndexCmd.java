package com.java110.store.cmd.propertyIndex;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.store.IComplaintInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "propertyIndex.queryOwnerRegisterIndex")
public class QueryOwnerRegisterIndexCmd extends Cmd {

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;
    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;



    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        JSONObject paramOut = new JSONObject();

        // 未注册 住户
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setIsBind(OwnerDto.IS_BIND_N);
        int unbindCount = ownerV1InnerServiceSMOImpl.queryOwnersBindCount(ownerDto);
        paramOut.put("unbindCount", unbindCount);

        // 已注册 住户
         ownerDto = new OwnerDto();
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setIsBind(OwnerDto.IS_BIND_Y);
        int bindCount = ownerV1InnerServiceSMOImpl.queryOwnersBindCount(ownerDto);
        paramOut.put("bindCount", bindCount);

        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(reqJson.getString("communityId"));
        roomDto.setRoomType(RoomDto.ROOM_TYPE_ROOM);
        roomDto.setState(RoomDto.STATE_FREE);
        int unbindRoomCount = roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
        paramOut.put("unbindRoomCount", unbindRoomCount);


         roomDto = new RoomDto();
        roomDto.setCommunityId(reqJson.getString("communityId"));
        roomDto.setRoomType(RoomDto.ROOM_TYPE_ROOM);
        int allRoomCount = roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
        paramOut.put("bindRoomCount", allRoomCount-unbindRoomCount);

        context.setResponseEntity(ResultVo.createResponseEntity(paramOut));
    }
}
