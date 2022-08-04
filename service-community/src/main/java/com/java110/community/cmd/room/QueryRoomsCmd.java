package com.java110.community.cmd.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.FloorDto;
import com.java110.dto.RoomDto;
import com.java110.dto.UnitDto;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
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
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

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

        //导致业务受理搜索查询
//        if (reqJson.containsKey("flag") && reqJson.getString("flag").equals("1")) {
//            if (reqJson.containsKey("roomNumLike") && !StringUtil.isEmpty(reqJson.getString("roomNumLike"))) {
//                String[] roomNumLikes = reqJson.getString("roomNumLike").split("-");
//                roomDto.setFloorNum(roomNumLikes[0]);
//                roomDto.setUnitNum(roomNumLikes[1]);
//                roomDto.setRoomNum(roomNumLikes[2]);
//                roomDto.setRoomNumLike("");
//            }
//        }
        String roomId = "";
        String unitId = "";
        if (reqJson.containsKey("flag") && !StringUtil.isEmpty(reqJson.getString("flag")) && reqJson.getString("flag").equals("0")
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
        if (reqJson.containsKey("flag") && !StringUtil.isEmpty(reqJson.getString("flag")) && reqJson.getString("flag").equals("1")) {
            if (reqJson.containsKey("roomNum") && !StringUtil.isEmpty(reqJson.getString("roomNum"))) {
                String[] roomNums = reqJson.getString("roomNum").split("-");
                if (roomNums != null && roomNums.length == 3) {
                    roomDto.setFloorNum(roomNums[0]);
                    roomDto.setUnitNum(roomNums[1]);
                    roomDto.setRoomNum(roomNums[2]);
                } else {
                    throw new IllegalArgumentException("房屋编号错误！");
                }
            } else {
                roomDto.setUnitNum("");
                roomDto.setFloorNum("");
                roomDto.setRoomNum("");
            }
        }
        ApiRoomVo apiRoomVo = new ApiRoomVo();
        //查询总记录数
        int total = roomInnerServiceSMOImpl.queryRoomsCount(roomDto);
        apiRoomVo.setTotal(total);
        List<RoomDto> roomDtoList = null;
        if (total > 0) {
            roomDtoList = roomInnerServiceSMOImpl.queryRooms(roomDto);
            refreshRoomOwners(reqJson.getString("loginUserId"), reqJson.getString("communityId"), roomDtoList);
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
