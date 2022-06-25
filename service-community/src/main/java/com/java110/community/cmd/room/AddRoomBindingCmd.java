package com.java110.community.cmd.room;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.FloorDto;
import com.java110.dto.RoomDto;
import com.java110.dto.UnitDto;
import com.java110.intf.community.IFloorV1InnerServiceSMO;
import com.java110.intf.community.IRoomAttrV1InnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.community.IUnitV1InnerServiceSMO;
import com.java110.po.floor.FloorPo;
import com.java110.po.room.RoomAttrPo;
import com.java110.po.room.RoomPo;
import com.java110.po.unit.UnitPo;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "room.addRoomBinding")
public class AddRoomBindingCmd extends Cmd {

    @Autowired
    private IUnitV1InnerServiceSMO unitV1InnerServiceSMOImpl;

    @Autowired
    private IFloorV1InnerServiceSMO floorV1InnerServiceSMOImpl;


    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IRoomAttrV1InnerServiceSMO roomAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        JSONArray infos = reqJson.getJSONArray("data");

        Assert.hasKeyByFlowData(infos, "addRoomView", "roomNum", "必填，请填写房屋编号");
        Assert.hasKeyByFlowData(infos, "addRoomView", "communityId", "必填，请填写房屋小区信息");
        Assert.hasKeyByFlowData(infos, "addRoomView", "layer", "必填，请填写房屋楼层");
        Assert.hasKeyByFlowData(infos, "addRoomView", "section", "必填，请填写房屋楼层");
        Assert.hasKeyByFlowData(infos, "addRoomView", "apartment", "必填，请选择房屋户型");
        Assert.hasKeyByFlowData(infos, "addRoomView", "builtUpArea", "必填，请填写房屋建筑面积");
        Assert.hasKeyByFlowData(infos, "addRoomView", "feeCoefficient", "必填，请填写房屋每平米单价");
        Assert.hasKeyByFlowData(infos, "addRoomView", "state", "必填，请选择房屋状态");

        JSONObject addRoomView = null;
        for (int roomIndex = 0; roomIndex < infos.size(); roomIndex++) {
            JSONObject _info = infos.getJSONObject(roomIndex);
            if (_info.containsKey("addRoomView") && _info.getString("flowComponent").equals("addRoomView")) {
                addRoomView = _info;
                break;
            }
        }

        if (addRoomView == null) {
            return;
        }

        Assert.judgeAttrValue(addRoomView);
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        JSONArray infos = reqJson.getJSONArray("data");
        JSONObject viewFloorInfo = getObj(infos, "viewFloorInfo");
        JSONObject viewUnitInfo = getObj(infos, "viewUnitInfo");
        JSONObject addRoomView = getObj(infos, "addRoomView");
        //楼栋id
        String floorId = "";
        //单元id
        String unitId = "";
        //房屋编号
        String roomNum = "";
        int flag = 0;
        if (viewFloorInfo.containsKey("floorId") && StringUtil.isEmpty(viewFloorInfo.getString("floorId"))
                && viewUnitInfo.containsKey("unitId") && StringUtil.isEmpty(viewUnitInfo.getString("unitId"))
                && addRoomView.containsKey("roomNum") && StringUtil.isEmpty(addRoomView.getString("roomNum"))) {
            floorId = viewFloorInfo.getString("floorId");
            unitId = viewUnitInfo.getString("unitId");
            roomNum = addRoomView.getString("roomNum");
            //查询楼栋下单元信息
            UnitDto unitDto = new UnitDto();
            unitDto.setFloorId(floorId);
            unitDto.setUnitId(unitId);
            List<UnitDto> unitDtos = unitV1InnerServiceSMOImpl.queryUnits(unitDto);
            Assert.listOnlyOne(unitDtos, "查询单元信息错误！");
            RoomDto roomDto = new RoomDto();
            roomDto.setUnitId(unitDtos.get(0).getUnitId());
            roomDto.setRoomNum(roomNum);
            List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
            if (roomDtos != null && roomDtos.size() > 0) {
                throw new IllegalArgumentException("该房屋已经存在！");
            }
        }
        if (!hasKey(viewFloorInfo, "floorId")) {
            //获取楼栋编码
            String floorNum = viewFloorInfo.getString("floorNum");
            //获取小区id
            String communityId = viewFloorInfo.getString("communityId");
            //判断楼栋编号是否重复
            FloorDto floorDto = new FloorDto();
            floorDto.setFloorNum(floorNum);
            floorDto.setCommunityId(communityId);
            int floorCount = floorV1InnerServiceSMOImpl.queryFloorsCount(floorDto);
            if (floorCount > 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "楼栋已经存在");
            }
            viewFloorInfo.put("floorId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_floorId));
            viewFloorInfo.put("userId", reqJson.getString("userId"));
            FloorPo floorPo = BeanConvertUtil.covertBean(viewFloorInfo, FloorPo.class);
            flag = floorV1InnerServiceSMOImpl.saveFloor(floorPo);
            if (flag < 1) {
                throw new CmdException("保存楼栋失败");
            }
        }
        if (!hasKey(viewUnitInfo, "unitId")) {
            if (viewFloorInfo.containsKey("floorId") && !StringUtil.isEmpty(viewFloorInfo.getString("floorId"))) { //如果前端选择的楼栋，而不是新增楼栋，就判断该楼栋下单元是否重复
                //获取楼栋id
                String floorId1 = viewFloorInfo.getString("floorId");
                //获取小区id
                String communityId = viewUnitInfo.getString("communityId");
                //获取单元编号
                String unitNum = viewUnitInfo.getString("unitNum");
                UnitDto unitDto = new UnitDto();
                unitDto.setFloorId(floorId1);
                unitDto.setCommunityId(communityId);
                unitDto.setUnitNum(unitNum);
                int unitCount = unitV1InnerServiceSMOImpl.queryUnitsCount(unitDto);
                if (unitCount > 0) {
                    throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "单元已经存在");
                }
            }
            viewUnitInfo.put("unitId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_unitId));
            viewUnitInfo.put("userId", reqJson.getString("userId"));
            viewUnitInfo.put("floorId", viewFloorInfo.getString("floorId"));

            UnitPo unitPo = BeanConvertUtil.covertBean(viewUnitInfo, UnitPo.class);
            flag = unitV1InnerServiceSMOImpl.saveUnit(unitPo);
            if (flag < 1) {
                throw new CmdException("保存单元失败");
            }
        }
        if (!hasKey(addRoomView, "roomId")) {
            if (viewUnitInfo.containsKey("unitId") && !StringUtil.isEmpty(viewUnitInfo.getString("unitId"))) { //如果前端选择的单元，而不是添加的，就判断该楼栋单元下房屋是否重复
                //获取单元id
                String unitId1 = viewUnitInfo.getString("unitId");
                RoomDto roomDto = new RoomDto();
                roomDto.setUnitId(unitId1);
                roomDto.setRoomNum(addRoomView.getString("roomNum"));
                roomDto.setCommunityId(addRoomView.getString("communityId"));
                int roomCount = roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
                if (roomCount > 0) {
                    throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "房屋已经存在");
                }
            }
            addRoomView.put("roomId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_roomId));
            addRoomView.put("userId", reqJson.getString("userId"));
            addRoomView.put("unitId", viewUnitInfo.getString("unitId"));
            addRoomView.put("roomType", RoomDto.ROOM_TYPE_ROOM);
            RoomPo roomPo = BeanConvertUtil.covertBean(addRoomView, RoomPo.class);
            if (addRoomView.containsKey("apartment1") && addRoomView.getString("apartment1").equals("10")) {
                roomPo.setSection("1");
            } else if (addRoomView.containsKey("apartment1") && addRoomView.getString("apartment1").equals("20")) {
                roomPo.setSection("2");
            } else if (addRoomView.containsKey("apartment1") && addRoomView.getString("apartment1").equals("30")) {
                roomPo.setSection("3");
            } else if (addRoomView.containsKey("apartment1") && addRoomView.getString("apartment1").equals("40")) {
                roomPo.setSection("4");
            } else if (addRoomView.containsKey("apartment1") && addRoomView.getString("apartment1").equals("50")) {
                roomPo.setSection("5");
            } else if (addRoomView.containsKey("apartment1") && addRoomView.getString("apartment1").equals("60")) {
                roomPo.setSection("6");
            } else if (addRoomView.containsKey("apartment1") && addRoomView.getString("apartment1").equals("70")) {
                roomPo.setSection("7");
            } else if (addRoomView.containsKey("apartment1") && addRoomView.getString("apartment1").equals("80")) {
                roomPo.setSection("8");
            }
            flag = roomV1InnerServiceSMOImpl.saveRoom(roomPo);
            if (flag < 1) {
                throw new CmdException("保存房屋失败");
            }
            //处理房屋属性
            dealRoomAttr(addRoomView, cmdDataFlowContext);
        }

        JSONObject paramOutObj = new JSONObject();
        paramOutObj.put("floorId", viewFloorInfo.getString("floorId"));
        paramOutObj.put("unitId", viewUnitInfo.getString("unitId"));
        paramOutObj.put("roomId", addRoomView.getString("roomId"));
        ResponseEntity<String> responseEntity = null;

        responseEntity = ResultVo.createResponseEntity(paramOutObj);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }


    private void dealRoomAttr(JSONObject addRoomView, ICmdDataFlowContext cmdDataFlowContext) {

        if (!addRoomView.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = addRoomView.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }


        JSONObject attr = null;
        int flag = 0;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("roomId", addRoomView.getString("roomId"));
            RoomAttrPo roomAttrPo = new RoomAttrPo();
            roomAttrPo.setAttrId(GenerateCodeFactory.getAttrId());
            roomAttrPo.setRoomId(attr.getString("roomId"));
            roomAttrPo.setSpecCd(attr.getString("specCd"));
            roomAttrPo.setValue(attr.getString("value"));

            flag = roomAttrV1InnerServiceSMOImpl.saveRoomAttr(roomAttrPo);
            if (flag < 1) {
                throw new CmdException("保存单元失败");
            }
        }

    }


    private boolean hasKey(JSONObject info, String key) {
        if (!info.containsKey(key)
                || StringUtil.isEmpty(info.getString(key))
                || info.getString(key).startsWith("-")) {
            return false;
        }
        return true;

    }

    private JSONObject getObj(JSONArray infos, String flowComponent) {

        JSONObject serviceInfo = null;

        for (int infoIndex = 0; infoIndex < infos.size(); infoIndex++) {

            Assert.hasKeyAndValue(infos.getJSONObject(infoIndex), "flowComponent", "未包含服务流程组件名称");

            if (flowComponent.equals(infos.getJSONObject(infoIndex).getString("flowComponent"))) {
                serviceInfo = infos.getJSONObject(infoIndex);
                Assert.notNull(serviceInfo, "未包含服务信息");
                return serviceInfo;
            }
        }

        throw new IllegalArgumentException("未找到组件编码为【" + flowComponent + "】数据");
    }
}
