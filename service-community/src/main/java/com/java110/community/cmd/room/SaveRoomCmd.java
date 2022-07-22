package com.java110.community.cmd.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.UnitDto;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.po.room.RoomPo;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "room.saveRoom")
public class SaveRoomCmd extends Cmd {

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "unitId", "请求报文中未包含unitId节点");
        Assert.jsonObjectHaveKey(reqJson, "roomNum", "请求报文中未包含roomNum节点");
        Assert.jsonObjectHaveKey(reqJson, "layer", "请求报文中未包含layer节点");
        Assert.jsonObjectHaveKey(reqJson, "section", "请求报文中未包含section节点");
        Assert.jsonObjectHaveKey(reqJson, "apartment", "请求报文中未包含apartment节点");
        Assert.jsonObjectHaveKey(reqJson, "state", "请求报文中未包含state节点");
        Assert.jsonObjectHaveKey(reqJson, "builtUpArea", "请求报文中未包含builtUpArea节点");
        Assert.jsonObjectHaveKey(reqJson, "feeCoefficient", "请求报文中未包含feeCoefficient节点");

        Assert.isInteger(reqJson.getString("section"), "房间数不是有效数字");
        Assert.isMoney(reqJson.getString("builtUpArea"), "建筑面积数据格式错误");
        Assert.isMoney(reqJson.getString("feeCoefficient"), "房屋单价数据格式错误");

        if (!reqJson.containsKey("roomSubType")) {
            reqJson.put("roomSubType", RoomDto.ROOM_SUB_TYPE_PERSON);
        }

        if (!reqJson.containsKey("roomRent")) {
            reqJson.put("roomRent", "0");
        }

        if (!reqJson.containsKey("roomArea")) {
            reqJson.put("roomRent", reqJson.getString("builtUpArea"));
        }

        /*if (!"1010".equals(reqJson.getString("apartment")) && !"2020".equals(reqJson.getString("apartment"))) {
            throw new IllegalArgumentException("不是有效房屋户型 传入数据错误");
        }*/

        if (!"2001".equals(reqJson.getString("state"))
                && !"2002".equals(reqJson.getString("state"))
                && !"2003".equals(reqJson.getString("state"))
                && !"2004".equals(reqJson.getString("state"))) {
            throw new IllegalArgumentException("不是有效房屋状态 传入数据错误");
        }

        UnitDto unitDto = new UnitDto();
        unitDto.setCommunityId(reqJson.getString("communityId"));
        unitDto.setUnitId(reqJson.getString("unitId"));
        //校验小区楼ID和小区是否有对应关系
        List<UnitDto> units = unitInnerServiceSMOImpl.queryUnitsByCommunityId(unitDto);

        if (units == null || units.size() < 1) {
            throw new IllegalArgumentException("传入单元ID不是该小区的单元");
        }

        reqJson.put("unitNum", units.get(0).getUnitNum());
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        if ("0".equals(reqJson.getString("unitNum"))) { // 处理为商铺
            reqJson.put("roomType", RoomDto.ROOM_TYPE_SHOPS);
        } else {
            reqJson.put("roomType", RoomDto.ROOM_TYPE_ROOM);
        }
        reqJson.put("roomId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_roomId));
        reqJson.put("userId", context.getReqHeaders().get(CommonConstant.HTTP_USER_ID));
        RoomPo roomPo = BeanConvertUtil.covertBean(reqJson, RoomPo.class);
        roomV1InnerServiceSMOImpl.saveRoom(roomPo);
    }
}
