package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.po.room.RoomPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "owner.ownerExitRoom")
public class OwnerExitRoomCmd extends Cmd {

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;


    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "ownerId", "请求报文中未包含业主");

        JSONArray selectRooms = reqJson.getJSONArray("selectRooms");

        if(selectRooms == null || selectRooms.size() < 1){
            throw new CmdException("未选择房屋");
        }

        PayFeeDto payFeeDto = null;
        List<PayFeeDto> payFeeDtos = null;
        for(int selectIndex = 0; selectIndex < selectRooms.size(); selectIndex++){
            payFeeDto = new PayFeeDto();
            payFeeDto.setPayerObjId(selectRooms.getString(selectIndex));
            payFeeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            payFeeDto.setState(FeeDto.STATE_DOING);
            payFeeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(payFeeDto);
            if(payFeeDtos != null && payFeeDtos.size()>0){
                RoomDto roomDto = new RoomDto();
                roomDto.setRoomId(selectRooms.getString(selectIndex));
                List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
                String roomName = "";
                if(roomDtos != null && roomDtos.size()> 0){
                    roomName = roomDtos.get(0).getFloorNum()+"-"+roomDtos.get(0).getUnitNum()+"-"+roomDtos.get(0).getRoomNum();
                }
                throw new CmdException(roomName+"存在费用");
            }

        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        JSONArray rooms = reqJson.getJSONArray("selectRooms");
        OwnerRoomRelPo ownerRoomRelPo = null;
        int flag = 0;
        OwnerRoomRelDto ownerRoomRelDto = null;
        List<OwnerRoomRelDto> ownerRoomRelDtos = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            ownerRoomRelDto = new OwnerRoomRelDto();
            ownerRoomRelDto.setOwnerId(reqJson.getString("ownerId"));
            ownerRoomRelDto.setRoomId(rooms.getString(roomIndex));
            ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
            if(ownerRoomRelDtos != null && ownerRoomRelDtos.size()>0){
                for(OwnerRoomRelDto tmpOwnerRoomRelDto : ownerRoomRelDtos) {
                    ownerRoomRelPo = new OwnerRoomRelPo();
                    ownerRoomRelPo.setRelId(tmpOwnerRoomRelDto.getRelId());
                    flag = ownerRoomRelV1InnerServiceSMOImpl.deleteOwnerRoomRel(ownerRoomRelPo);
                    if (flag < 1) {
                        throw new CmdException("删除业主房屋失败");
                    }
                }
            }

            RoomPo roomPo = new RoomPo();
            roomPo.setRoomId(rooms.getString(roomIndex));
            roomPo.setCommunityId(reqJson.getString("communityId"));
            roomPo.setState(RoomDto.STATE_FREE);
            flag = roomV1InnerServiceSMOImpl.updateRoom(roomPo);
            if (flag < 1) {
                throw new CmdException("操作业主失败");
            }
        }
    }
}
