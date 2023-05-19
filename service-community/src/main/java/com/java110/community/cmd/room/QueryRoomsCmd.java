package com.java110.community.cmd.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.room.IQueryRoomStatisticsBMO;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.FloorDto;
import com.java110.dto.RoomDto;
import com.java110.dto.UnitDto;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.data.DataPrivilegeStaffDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.community.*;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.ApiRoomDataVo;
import com.java110.vo.api.ApiRoomVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Java110CmdDoc(title = "查询房屋",
        description = "查询房屋信息",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/room.queryRooms",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "room.queryRooms",
        seq = 16
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "page", type = "int",length = 11, remark = "页数"),
        @Java110ParamDoc(name = "row", type = "int",length = 11, remark = "行数"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "roomId", length = 30, remark = "房屋ID"),
        @Java110ParamDoc(name = "floorId", length = 30, remark = "楼栋ID"),
        @Java110ParamDoc(name = "unitId", length = 30, remark = "单元ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "records", type = "int", length = 11,  remark = "总页数"),
                @Java110ParamDoc(name = "total", type = "int", length = 11, remark = "总数据"),
                @Java110ParamDoc(name = "rooms", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "rooms",name = "roomName", type = "String", remark = "房屋名称"),
                @Java110ParamDoc(parentNodeName = "rooms",name = "roomId", type = "String", remark = "房屋编号"),
        }
)

@Java110ExampleDoc(
        reqBody="http://{ip}:{port}/app/room.queryRooms?floorId=&floorName=&unitId=&roomNum=&roomId=&state=&section=&roomType=1010301&roomSubType=&flag=0&page=1&row=10&communityId=2022081539020475",
        resBody="{\"page\":0,\"records\":1,\"rooms\":[{\"apartment\":\"10101\",\"apartmentName\":\"一室一厅\",\"builtUpArea\":\"11.00\",\"endTime\":\"2037-01-01 00:00:00\",\"feeCoefficient\":\"1.00\",\"floorId\":\"732022081690440002\",\"floorNum\":\"D\",\"idCard\":\"\",\"layer\":\"1\",\"link\":\"18909711447\",\"ownerId\":\"772022082070860017\",\"ownerName\":\"张杰\",\"remark\":\"11\",\"roomArea\":\"11.00\",\"roomAttrDto\":[{\"attrId\":\"112022082081600012\",\"listShow\":\"Y\",\"page\":-1,\"records\":0,\"roomId\":\"752022082030880010\",\"row\":0,\"specCd\":\"9035007248\",\"specName\":\"精装修\",\"statusCd\":\"0\",\"total\":0,\"value\":\"20\",\"valueName\":\"20\"}],\"roomId\":\"752022082030880010\",\"roomName\":\"D-1-1001\",\"roomNum\":\"1001\",\"roomRent\":\"0.00\",\"roomSubType\":\"110\",\"roomSubTypeName\":\"住宅\",\"roomType\":\"1010301\",\"section\":\"1\",\"startTime\":\"2022-09-03 18:50:53\",\"state\":\"2001\",\"stateName\":\"已入住\",\"unitId\":\"742022082058950007\",\"unitNum\":\"1\"}],\"rows\":0,\"total\":2}"
)
@Java110Cmd(serviceCode = "room.queryRooms")
public class QueryRoomsCmd extends Cmd {

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IDataPrivilegeUnitV1InnerServiceSMO dataPrivilegeUnitV1InnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Autowired
    private IQueryRoomStatisticsBMO queryRoomStatisticsBMOImpl;

    protected static final int MAX_ROW = 10000;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        //Assert.jsonObjectHaveKey(reqJson, "floorId", "请求中未包含floorId信息");
        Assert.jsonObjectHaveKey(reqJson, "page", "请求报文中未包含page节点");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求报文中未包含row节点");

        Assert.isInteger(reqJson.getString("page"), "page不是数字");
        Assert.isInteger(reqJson.getString("row"), "row不是数字");
        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        int row = Integer.parseInt(reqJson.getString("row"));


        if (row > MAX_ROW) {
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "row 数量不能大于50");
        }
        //校验小区楼ID和小区是否有对应关系
        int total = floorInnerServiceSMOImpl.queryFloorsCount(BeanConvertUtil.covertBean(reqJson, FloorDto.class));

        if (total < 1) {
            throw new IllegalArgumentException("传入小区楼ID不是该小区的楼");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);



        //员工数据权限
        String staffId = cmdDataFlowContext.getReqHeaders().get("user-id");
        DataPrivilegeStaffDto dataPrivilegeStaffDto = new DataPrivilegeStaffDto();
        dataPrivilegeStaffDto.setStaffId(staffId);
        String[] unitIds = dataPrivilegeUnitV1InnerServiceSMOImpl.queryDataPrivilegeUnitsByStaff(dataPrivilegeStaffDto);



        String roomId = "";
        String unitId = "";
        if (reqJson.containsKey("flag") && "0".equals(reqJson.getString("flag"))
                && reqJson.containsKey("floorNum") && !StringUtil.isEmpty(reqJson.getString("floorNum"))
                && reqJson.containsKey("unitNum") && !StringUtil.isEmpty(reqJson.getString("unitNum"))
                && reqJson.containsKey("roomNum") && !StringUtil.isEmpty(reqJson.getString("roomNum"))) {
            FloorDto floorDto = new FloorDto();
            floorDto.setFloorNum(reqJson.getString("floorNum"));
            floorDto.setCommunityId(reqJson.getString("communityId"));
            List<FloorDto> floorDtos = floorInnerServiceSMOImpl.queryFloors(floorDto);
            if (floorDtos != null && floorDtos.size() > 0) {
                for (FloorDto floor : floorDtos) {
                    UnitDto unitDto = new UnitDto();
                    unitDto.setFloorId(floor.getFloorId());
                    unitDto.setUnitNum(reqJson.getString("unitNum"));
                    if(unitIds != null && unitIds.length>0){
                        unitDto.setUnitIds(unitIds);
                    }
                    List<UnitDto> unitDtos = unitInnerServiceSMOImpl.queryUnits(unitDto);
                    if (unitDtos != null && unitDtos.size() > 0) {
                        for (UnitDto unit : unitDtos) {
                            RoomDto room = new RoomDto();
                            room.setUnitId(unit.getUnitId());
                            room.setRoomNum(reqJson.getString("roomNum"));
                            room.setCommunityId(reqJson.getString("communityId"));
                            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(room);
                            if (roomDtos != null && roomDtos.size() == 1) {
                                unitId = roomDtos.get(0).getUnitId();
                                roomId = roomDtos.get(0).getRoomId();
                            } else {
                                continue;
                            }
                        }
                    }
                }
            }
            roomDto.setRoomId(roomId);
            roomDto.setUnitId(unitId);
        }
        if (reqJson.containsKey("flag") && "1".equals(reqJson.getString("flag"))) {
            if (reqJson.containsKey("roomNum") && !StringUtil.isEmpty(reqJson.getString("roomNum"))) {
                String[] roomNums = reqJson.getString("roomNum").split("-", 3);
                if (roomNums != null && roomNums.length == 3) {
                    roomDto.setFloorNum(roomNums[0]);
                    roomDto.setUnitNum(roomNums[1]);
                    roomDto.setRoomNum(roomNums[2]);
                } else {
                    roomDto.setRoomNum(reqJson.getString("roomNum"));
                }
            } else {
                roomDto.setUnitNum("");
                roomDto.setFloorNum("");
                roomDto.setRoomNum("");
            }
        }

        //add by wuxw 商铺 两个短线方式处理
        if(reqJson.containsKey("roomType") && "2020602".equals(reqJson.getString("roomType"))){
            if (reqJson.containsKey("roomNum") && !StringUtil.isEmpty(reqJson.getString("roomNum"))) {
                String[] roomNums = reqJson.getString("roomNum").split("-", 2);
                if (roomNums != null && roomNums.length == 2) {
                    roomDto.setFloorNum(roomNums[0]);
                    roomDto.setUnitNum("0");
                    roomDto.setRoomNum(roomNums[1]);
                } else {
                    roomDto.setRoomNum(reqJson.getString("roomNum"));
                }
            } else {
                roomDto.setUnitNum("");
                roomDto.setFloorNum("");
                roomDto.setRoomNum("");
            }
        }
        ApiRoomVo apiRoomVo = new ApiRoomVo();

        //员工是否 有权限查询
        if(unitIds != null && unitIds.length>0){
            roomDto.setUnitIds(unitIds);
        }
        //查询总记录数
        int total = roomInnerServiceSMOImpl.queryRoomsCount(roomDto);
        apiRoomVo.setTotal(total);
        List<RoomDto> roomDtoList = null;
        if (total > 0) {
            roomDtoList = roomInnerServiceSMOImpl.queryRooms(roomDto);
            refreshRoomOwners(reqJson.getString("loginUserId"), reqJson.getString("communityId"), roomDtoList);

            // 查询房屋统计数据
            roomDtoList = queryRoomStatisticsBMOImpl.query(roomDtoList);
        } else {
            roomDtoList = new ArrayList<>();
        }
        apiRoomVo.setRooms(BeanConvertUtil.covertBeanList(roomDtoList, ApiRoomDataVo.class));
        int row = reqJson.getInteger("row");
        apiRoomVo.setRecords((int) Math.ceil((double) total / (double) row));

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiRoomVo), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }


    /**
     * 刷入房屋业主信息
     *
     * @param roomDtos
     */
    private void refreshRoomOwners(String userId, String communityId, List<RoomDto> roomDtos) {

        /**
         * 量太大时 查询 会比较慢，如果其他地方有bug 切换 查询报表去，不能靠这个接口查询大量数据
         */
        if (roomDtos == null || roomDtos.size() > 20) {
            return;
        }
        List<String> roomIds = new ArrayList<>();
        for (RoomDto roomDto : roomDtos) {
            roomIds.add(roomDto.getRoomId());
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<Map> mark = getPrivilegeOwnerList("/roomCreateFee", userId);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);
        for (RoomDto roomDto : roomDtos) {
            for (OwnerDto tmpOwnerDto : ownerDtos) {
                if (!roomDto.getRoomId().equals(tmpOwnerDto.getRoomId())) {
                    continue;
                }
                try {
                    roomDto.setStartTime(DateUtil.getDateFromString(tmpOwnerDto.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
                    roomDto.setEndTime(DateUtil.getDateFromString(tmpOwnerDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
                } catch (Exception e) {
                    //
                }
                roomDto.setOwnerId(tmpOwnerDto.getOwnerId());
                roomDto.setOwnerName(tmpOwnerDto.getName());
                //对业主身份证号隐藏处理
                String idCard = tmpOwnerDto.getIdCard();
                if (mark.size() == 0 && idCard != null && !idCard.equals("") && idCard.length() > 15) {
                    idCard = idCard.substring(0, 6) + "**********" + idCard.substring(16);
                }
                //对业主手机号隐藏处理
                String link = tmpOwnerDto.getLink();
                roomDto.setOwnerTel(link); //程序用 主要用以查询 报修等统计信息
                if (mark.size() == 0 && link != null && !link.equals("") && link.length() > 10) {
                    link = link.substring(0, 3) + "****" + link.substring(7);
                }
                roomDto.setIdCard(idCard);
                roomDto.setLink(link);

                //商铺类型查询起租时间
//                if (roomDto.getRoomType().equals(RoomDto.ROOM_TYPE_SHOPS)) {
//                    OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
//                    ownerRoomRelDto.setRoomId(roomDto.getRoomId());
//                    ownerRoomRelDto.setStatusCd("0");
//                    List<OwnerRoomRelDto> ownerRoomRelDtoList = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
//                    if (ownerRoomRelDtoList != null && ownerRoomRelDtoList.size() == 1) {
//                        roomDto.setStartTime(ownerRoomRelDtoList.get(0).getStartTime());
//                        roomDto.setEndTime(ownerRoomRelDtoList.get(0).getEndTime());
//                    }
//                }
            }
        }
    }

    /**
     * 脱敏处理
     *
     * @return
     */
    public List<Map> getPrivilegeOwnerList(String resource, String userId) {
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource(resource);
        basePrivilegeDto.setUserId(userId);
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        return privileges;
    }
}
