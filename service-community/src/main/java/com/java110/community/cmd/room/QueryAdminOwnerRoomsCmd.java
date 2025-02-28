package com.java110.community.cmd.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.room.IQueryRoomStatisticsBMO;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.ApiRoomDataVo;
import com.java110.vo.api.ApiRoomVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "room.queryAdminOwnerRooms")
public class QueryAdminOwnerRoomsCmd extends Cmd {

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IQueryRoomStatisticsBMO queryRoomStatisticsBMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validateAdmin(context);
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson,"ownerId","未包含业主编号");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);
        hasRoomNum(reqJson, roomDto);

        ApiRoomVo apiRoomVo = new ApiRoomVo();
        List<RoomDto> roomDtoList = roomInnerServiceSMOImpl.queryRoomsByOwner(roomDto);
        roomDtoList = queryRoomStatisticsBMOImpl.queryRoomOweFee(roomDtoList);

        apiRoomVo.setTotal(roomDtoList.size());
        apiRoomVo.setRooms(BeanConvertUtil.covertBeanList(roomDtoList, ApiRoomDataVo.class));
        apiRoomVo.setRecords(1);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiRoomVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    /**
     * 判断是否存在房屋编号
     * @param reqJson
     * @param roomDto
     */
    private void hasRoomNum(JSONObject reqJson, RoomDto roomDto) {
        if (!reqJson.containsKey("roomNum")) {
            return;
        }
        String roomNum = reqJson.getString("roomNum");
        if (StringUtil.isEmpty(roomNum)) {
            return;
        }
        String[] roomNums = reqJson.getString("roomNum").split("-");

        if (roomNums != null && roomNums.length == 3) {
            roomDto.setFloorNum(roomNums[0]);
            roomDto.setUnitNum(roomNums[1]);
            roomDto.setRoomNum(roomNums[2]);
        }

    }
}
