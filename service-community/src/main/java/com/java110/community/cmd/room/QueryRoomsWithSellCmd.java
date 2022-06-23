package com.java110.community.cmd.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.FloorDto;
import com.java110.dto.RoomDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.ApiRoomDataVo;
import com.java110.vo.api.ApiRoomVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "room.queryRoomsWithSell")
public class QueryRoomsWithSellCmd extends Cmd {

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.jsonObjectHaveKey(reqJson, "page", "请求报文中未包含page节点");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求报文中未包含row节点");

        Assert.isInteger(reqJson.getString("page"), "page不是数字");
        Assert.isInteger(reqJson.getString("row"), "row不是数字");
        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        int row = Integer.parseInt(reqJson.getString("row"));


        if (row > MAX_ROW) {
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "row 数量不能大于50");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //将小区楼ID刷入到 请求参数中
        freshFloorIdToParam(reqJson);

        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);

        ApiRoomVo apiRoomVo = new ApiRoomVo();
        //查询总记录数
        int total = roomInnerServiceSMOImpl.queryRoomsWithSellCount(BeanConvertUtil.covertBean(reqJson, RoomDto.class));
        apiRoomVo.setTotal(total);
        if (total > 0) {
            List<RoomDto> roomDtoList = roomInnerServiceSMOImpl.queryRoomsWithSell(roomDto);
            List<RoomDto> rooms = new ArrayList<>();
            for (RoomDto room : roomDtoList) {
                room.setFloorId(reqJson.getString("floorId"));
                room.setFloorNum(reqJson.getString("floorNum"));
                rooms.add(room);
            }
            apiRoomVo.setRooms(BeanConvertUtil.covertBeanList(rooms, ApiRoomDataVo.class));
        }
        int row = reqJson.getInteger("row");
        apiRoomVo.setRecords((int) Math.ceil((double) total / (double) row));

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiRoomVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    /**
     * 将floorNum 转化为 floorId 刷入到入参对象中
     *
     * @param reqJson 入参对象
     */
    private void freshFloorIdToParam(JSONObject reqJson) {

        FloorDto floorDto = BeanConvertUtil.covertBean(reqJson, FloorDto.class);
        String floorId = "001";
        floorDto.setPage(1);//解决分页bug
        //检查 请求报文中是否有floorNum 小区楼编号，如果没有就随机选一个
        try {
            //if (!reqJson.containsKey("floorNum") || StringUtils.isEmpty(reqJson.getString("floorNum"))) {

            List<FloorDto> floorDtos = floorInnerServiceSMOImpl.queryFloors(floorDto);

            if (floorDtos.size() == 0) {
                return;
            }

            floorId = floorDtos.get(0).getFloorId();
            //}
        } finally {
            reqJson.put("floorId", floorId);
        }
    }
}
