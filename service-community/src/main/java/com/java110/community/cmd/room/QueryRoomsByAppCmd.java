package com.java110.community.cmd.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.app.AppDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "room.queryRoomsByApp")
public class QueryRoomsByAppCmd extends Cmd {

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        //Assert.jsonObjectHaveKey(reqJson, "floorId", "请求中未包含floorId信息");
        Assert.jsonObjectHaveKey(reqJson, "page", "请求报文中未包含page节点");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求报文中未包含row节点");
        Assert.hasKeyAndValue(reqJson, "unitId", "未包含单元");

        String appId = CmdContextUtils.getAppId(context);
        if (!AppDto.WECHAT_OWNER_APP_ID.equals(appId) && !AppDto.WECHAT_MINA_OWNER_APP_ID.equals(appId)) {
            throw new CmdException("此接口用户端专用");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);
        roomDto.setUserId("");

        //查询总记录数
        int total = roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
        List<RoomDto> roomDtoList = null;
        if (total > 0) {
            roomDtoList = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
        } else {
            roomDtoList = new ArrayList<>();
        }

        roomDtoList = clearRoomDatas(roomDtoList);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) total / (double) reqJson.getInteger("row")), total, roomDtoList);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    private List<RoomDto> clearRoomDatas(List<RoomDto> roomDtoList) {

        if(ListUtil.isNull(roomDtoList)){
            return roomDtoList;
        }
        List<RoomDto> roomDtos = new ArrayList<>();
        RoomDto tmpRoomDto = null;
        for(RoomDto roomDto:roomDtoList){
            tmpRoomDto = new RoomDto();
            tmpRoomDto.setRoomId(roomDto.getRoomId());
            tmpRoomDto.setRoomName(roomDto.getFloorNum()+"-"+roomDto.getUnitNum()+"-"+roomDto.getRoomNum());
            roomDtos.add(tmpRoomDto);
        }
        return roomDtos;
    }
}
