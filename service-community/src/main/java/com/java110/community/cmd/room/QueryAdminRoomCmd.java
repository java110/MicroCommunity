package com.java110.community.cmd.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.room.IQueryRoomStatisticsBMO;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "room.queryAdminRoom")
public class QueryAdminRoomCmd extends Cmd {

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IQueryRoomStatisticsBMO queryRoomStatisticsBMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validateAdmin(context);
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);

        // 计算根据业主查询
        computeOwnerRoomIds(reqJson, roomDto);


        //todo 计算楼栋单元房屋编号
        computeFloorUnitRoomNum(reqJson, roomDto);

        int count = roomInnerServiceSMOImpl.queryRoomsCount(roomDto);
        List<RoomDto> roomDtos = null;
        if (count > 0) {
            roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
            // todo 查询房屋统计数据
            roomDtos = queryRoomStatisticsBMOImpl.querySimple(roomDtos);
        } else {
            roomDtos = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, roomDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    private void computeOwnerRoomIds(JSONObject reqJson, RoomDto roomDto) {
        String ownerNameLike = reqJson.getString("ownerNameLike");
        String ownerTel = reqJson.getString("ownerTel");

        if (StringUtil.isEmpty(ownerNameLike) && StringUtil.isEmpty(ownerTel)) {
            return;
        }

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerNameLike(reqJson.getString("ownerNameLike"));
        ownerRoomRelDto.setLink(ownerTel);
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if (ListUtil.isNull(ownerRoomRelDtos)) { // 表示后续查不到
            roomDto.setRoomId("-1");
            return;
        }
        List<String> roomIds = new ArrayList<>();
        for (OwnerRoomRelDto tOwnerRoomRelDto : ownerRoomRelDtos) {
            roomIds.add(tOwnerRoomRelDto.getRoomId());
        }
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
    }

    /**
     * 计算 楼栋单元房屋编号
     *
     * @param reqJson
     * @param roomDto
     */
    private static void computeFloorUnitRoomNum(JSONObject reqJson, RoomDto roomDto) {

        if (!reqJson.containsKey("roomNum") || StringUtil.isEmpty(reqJson.getString("roomNum"))) {
            roomDto.setUnitNum("");
            roomDto.setFloorNum("");
            roomDto.setRoomNum("");
            return;
        }
        String[] roomNums = reqJson.getString("roomNum").split("-", 3);
        if (roomNums.length == 3) {
            roomDto.setFloorNum(roomNums[0]);
            roomDto.setUnitNum(roomNums[1]);
            roomDto.setRoomNum(roomNums[2]);
            return;
        }
        roomDto.setRoomNum(reqJson.getString("roomNum"));
    }
}
