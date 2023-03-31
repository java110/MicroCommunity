package com.java110.community.cmd.room;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.UnitDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.intf.community.*;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.po.room.RoomAttrPo;
import com.java110.po.room.RoomPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Java110CmdDoc(title = "修改房屋",
        description = "对应后台 修改房屋功能",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/room.updateRoom",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "room.updateRoom",
        seq = 14
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "roomNum", length = 64, remark = "房号"),
        @Java110ParamDoc(name = "layer", length = 12, remark = "层数"),
        @Java110ParamDoc(name = "unitId", length = 30, remark = "单元ID"),
        @Java110ParamDoc(name = "roomId", length = 30, remark = "单元ID"),
        @Java110ParamDoc(name = "section", length = 12, remark = "房屋个数"),
        @Java110ParamDoc(name = "apartment", length = 12, remark = "固定 10102"),
        @Java110ParamDoc(name = "builtUpArea", length = 12, remark = "建筑面积"),
        @Java110ParamDoc(name = "feeCoefficient", length = 12, remark = "算费系数 固定1"),
        @Java110ParamDoc(name = "roomSubType", length = 12, remark = "房屋类型 110\t住宅\n" +
                "120\t办公室\n" +
                "119\t宿舍"),
        @Java110ParamDoc(name = "roomArea", length = 12, remark = "室内面积"),
        @Java110ParamDoc(name = "roomRent", length = 12, remark = "办公室 或者宿舍 时租金"),
        @Java110ParamDoc(name = "remark", length = 512, remark = "备注"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody = "{\n" +
                "\t\"roomNum\": \"88488\",\n" +
                "\t\"layer\": \"1\",\n" +
                "\t\"unitId\":\"123123123\",\n" +
                "\t\"section\": \"0\",\n" +
                "\t\"apartment\": \"10102\",\n" +
                "\t\"builtUpArea\": \"110\",\n" +
                "\t\"feeCoefficient\": \"1.00\",\n" +
                "\t\"roomId\": \"123123123123\",\n" +
                "\t\"remark\": \"sdf\",\n" +
                "\t\"roomSubType\": \"110\",\n" +
                "\t\"roomArea\": \"110\",\n" +
                "\t\"roomRent\": \"0\",\n" +
                "\t\"communityId\": \"2022121921870161\",\n" +
                "}",
        resBody = "{\"code\":0,\"msg\":\"成功\"}"
)
@Java110Cmd(serviceCode = "room.updateRoom")
public class UpdateRoomCmd extends Cmd {

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IRoomAttrV1InnerServiceSMO roomAttrV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "roomId", "请求报文中未包含roomId节点");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "roomNum", "请求报文中未包含roomNum节点");
        Assert.jsonObjectHaveKey(reqJson, "layer", "请求报文中未包含layer节点");
        /*Assert.jsonObjectHaveKey(paramIn, "section", "请求报文中未包含section节点");*/
        Assert.jsonObjectHaveKey(reqJson, "builtUpArea", "请求报文中未包含builtUpArea节点");
        if (reqJson.containsKey("builtUpArea")) {
            Assert.isMoney(reqJson.getString("builtUpArea"), "建筑面积数据格式错误");
        }
        if (reqJson.containsKey("feeCoefficient")) {
            Assert.isMoney(reqJson.getString("feeCoefficient"), "算费系数数据格式错误");
        }
        //获取房屋状态
        String state = reqJson.getString("state");
        if (!StringUtil.isEmpty(state) && state.equals("2006")) { //已出租
            //获取起租时间
            Date startTime = null;
            Date endTime = null;
            if (reqJson.containsKey("startTime") && !StringUtil.isEmpty(reqJson.getString("startTime"))
                    && reqJson.containsKey("endTime") && !StringUtil.isEmpty(reqJson.getString("endTime"))) {
                try {
                    startTime = DateUtil.getDateFromString(reqJson.getString("startTime"), DateUtil.DATE_FORMATE_STRING_B);
                    endTime = DateUtil.getDateFromString(reqJson.getString("endTime"), DateUtil.DATE_FORMATE_STRING_B);
                } catch (Exception e) {
                    throw new CmdException(e.getMessage());
                }
                if (startTime.getTime() > endTime.getTime()) {
                    throw new IllegalArgumentException("起租时间不能大于截租时间！");
                }
            }
        }
        UnitDto unitDto = new UnitDto();
        unitDto.setCommunityId(reqJson.getString("communityId"));
        unitDto.setUnitId(reqJson.getString("unitId"));
        //校验小区楼ID和小区是否有对应关系
        List<UnitDto> units = unitInnerServiceSMOImpl.queryUnitsByCommunityId(unitDto);
        if (units == null || units.size() < 1) {
            throw new IllegalArgumentException("传入单元ID不是该小区的单元");
        }
        Assert.judgeAttrValue(reqJson);
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        updateShellRoom(reqJson, cmdDataFlowContext);
        String state = reqJson.getString("state");
        if (!StringUtil.isEmpty(state) && state.equals("2006")) { //已出租
            OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
            ownerRoomRelDto.setRoomId(reqJson.getString("roomId"));
            List<OwnerRoomRelDto> ownerRoomRelDtoList = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
            OwnerRoomRelPo ownerRoomRelPo = BeanConvertUtil.covertBean(ownerRoomRelDtoList.get(0), OwnerRoomRelPo.class);
            ownerRoomRelPo.setStartTime(reqJson.getString("startTime"));
            ownerRoomRelPo.setEndTime(reqJson.getString("endTime") + " 23:59:59");
            ownerRoomRelInnerServiceSMOImpl.updateOwnerRoomRels(ownerRoomRelPo);
        }
        if (!reqJson.containsKey("attrs")) {
            return;
        }
        JSONArray attrs = reqJson.getJSONArray("attrs");
        if (attrs == null || attrs.size() < 1) {
            return;
        }
        JSONObject attr = null;
        int flag = 0;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("roomId", reqJson.getString("roomId"));
            if (!attr.containsKey("attrId") || attr.getString("attrId").startsWith("-") || StringUtil.isEmpty(attr.getString("attrId"))) {
                RoomAttrPo roomAttrPo = new RoomAttrPo();
                roomAttrPo.setAttrId(GenerateCodeFactory.getAttrId());
                roomAttrPo.setRoomId(attr.getString("roomId"));
                roomAttrPo.setSpecCd(attr.getString("specCd"));
                roomAttrPo.setValue(attr.getString("value"));
                flag = roomAttrV1InnerServiceSMOImpl.saveRoomAttr(roomAttrPo);
                if (flag < 1) {
                    throw new CmdException("保存单元失败");
                }
                continue;
            }
            RoomAttrPo roomAttrPo = new RoomAttrPo();
            roomAttrPo.setAttrId(attr.getString("attrId"));
            roomAttrPo.setRoomId(attr.getString("roomId"));
            roomAttrPo.setSpecCd(attr.getString("specCd"));
            roomAttrPo.setValue(attr.getString("value"));
            flag = roomAttrV1InnerServiceSMOImpl.updateRoomAttr(roomAttrPo);
            if (flag < 1) {
                throw new CmdException("保存单元失败");
            }
        }
    }

    /**
     * 添加小区楼信息
     *
     * @param paramInJson        接口调用放传入入参
     * @param cmdDataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateShellRoom(JSONObject paramInJson, ICmdDataFlowContext cmdDataFlowContext) {
        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(paramInJson);
        RoomPo roomPo = BeanConvertUtil.covertBean(businessUnit, RoomPo.class);
        if (paramInJson.containsKey("apartment1") && paramInJson.getString("apartment1").equals("10")) {
            roomPo.setSection("1");
        } else if (paramInJson.containsKey("apartment1") && paramInJson.getString("apartment1").equals("20")) {
            roomPo.setSection("2");
        } else if (paramInJson.containsKey("apartment1") && paramInJson.getString("apartment1").equals("30")) {
            roomPo.setSection("3");
        } else if (paramInJson.containsKey("apartment1") && paramInJson.getString("apartment1").equals("40")) {
            roomPo.setSection("4");
        } else if (paramInJson.containsKey("apartment1") && paramInJson.getString("apartment1").equals("50")) {
            roomPo.setSection("5");
        } else if (paramInJson.containsKey("apartment1") && paramInJson.getString("apartment1").equals("60")) {
            roomPo.setSection("6");
        } else if (paramInJson.containsKey("apartment1") && paramInJson.getString("apartment1").equals("70")) {
            roomPo.setSection("7");
        } else if (paramInJson.containsKey("apartment1") && paramInJson.getString("apartment1").equals("80")) {
            roomPo.setSection("8");
        }
        int flag = roomV1InnerServiceSMOImpl.updateRoom(roomPo);
        if (flag < 1) {
            throw new CmdException("修改房屋失败");
        }
    }
}
