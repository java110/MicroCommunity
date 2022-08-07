package com.java110.community.cmd.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.UnitDto;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.intf.community.IUnitV1InnerServiceSMO;
import com.java110.po.room.RoomPo;
import com.java110.po.unit.UnitPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "room.saveShops")
public class SaveShopsCmd extends Cmd {

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IUnitV1InnerServiceSMO unitV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "floorId", "请求报文中未包含楼栋信息");
        Assert.jsonObjectHaveKey(reqJson, "roomNum", "请求报文中未包含roomNum节点");
        Assert.jsonObjectHaveKey(reqJson, "layer", "请求报文中未包含layer节点");
        Assert.jsonObjectHaveKey(reqJson, "builtUpArea", "请求报文中未包含section节点");
        Assert.jsonObjectHaveKey(reqJson, "feeCoefficient", "请求报文中未包含apartment节点");

        Assert.isMoney(reqJson.getString("builtUpArea"), "建筑面积数据格式错误");
        Assert.isMoney(reqJson.getString("feeCoefficient"), "房屋单价数据格式错误");

        if (!reqJson.containsKey("roomSubType")) {
            reqJson.put("roomSubType", RoomDto.ROOM_SUB_TYPE_WORK);
        }

        if (!reqJson.containsKey("roomRent")) {
            reqJson.put("roomRent", "0");
        }

        if (!reqJson.containsKey("roomArea")) {
            reqJson.put("roomRent", reqJson.getString("builtUpArea"));
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        UnitDto unitDto = new UnitDto();
        unitDto.setCommunityId(reqJson.getString("communityId"));
        unitDto.setFloorId(reqJson.getString("floorId"));
        unitDto.setUnitNum("0");
        //校验小区楼ID和小区是否有对应关系
        List<UnitDto> units = unitInnerServiceSMOImpl.queryUnitsByCommunityId(unitDto);
        String unitId = "";
        if (units == null || units.size() < 1) {
            unitId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_unitId);
            JSONObject unit = new JSONObject();
            unit.put("floorId", reqJson.getString("floorId"));
            unit.put("layerCount", "31");
            unit.put("unitId", unitId);
            unit.put("unitNum", "0");
            unit.put("lift", "0");
            unit.put("remark", "系统创建");
            unit.put("unitArea", "1.00");
            addUnit(unit);
        } else {
            unitId = units.get(0).getUnitId();
        }
        reqJson.put("unitId", unitId);
        reqJson.put("section", "1");
        reqJson.put("apartment", "10101");
        reqJson.put("state", RoomDto.STATE_SHOP_FREE);
        reqJson.put("roomType", RoomDto.ROOM_TYPE_SHOPS);
        addRoom(reqJson);
    }

    public void addUnit(JSONObject paramInJson) {

        JSONObject businessUnit = new JSONObject();
        businessUnit.put("floorId", paramInJson.getString("floorId"));
        businessUnit.put("layerCount", paramInJson.getString("layerCount"));
        businessUnit.put("unitId", !paramInJson.containsKey("unitId") ? GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_unitId)
                : paramInJson.getString("unitId"));
        businessUnit.put("unitNum", paramInJson.getString("unitNum"));
        businessUnit.put("lift", paramInJson.getString("lift"));
        businessUnit.put("remark", paramInJson.getString("remark"));
        businessUnit.put("unitArea", paramInJson.getString("unitArea"));
        businessUnit.put("userId", "-1");
        UnitPo unitPo = BeanConvertUtil.covertBean(businessUnit, UnitPo.class);

        int flag = unitV1InnerServiceSMOImpl.saveUnit(unitPo);
        if (flag < 1) {
            throw new CmdException("保存单元失败");
        }
    }

    /**
     * 添加小区楼信息
     *
     * @param paramInJson     接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addRoom(JSONObject paramInJson) {

        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(paramInJson);
        businessUnit.put("roomId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_roomId));
        businessUnit.put("userId", "-1");
        RoomPo roomPo = BeanConvertUtil.covertBean(businessUnit, RoomPo.class);
        int flag = roomV1InnerServiceSMOImpl.saveRoom(roomPo);
        if (flag < 1) {
            throw new CmdException("保存单元失败");
        }
    }
}
