package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.ApiRoomDataVo;
import com.java110.vo.api.ApiRoomVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "fee.listRoomsWhereFeeSet")
public class ListRoomsWhereFeeSetCmd extends Cmd {

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        ApiRoomVo apiRoomVo = new ApiRoomVo();

        String ownerName = reqJson.getString("ownerName");
        String idCard = reqJson.getString("idCard");
        String ownerNameLike = reqJson.getString("ownerNameLike");
        //这里优化 userId 问题
        reqJson.put("userId","");
        //根据 业主来定位房屋信息
        if (!StringUtil.isEmpty(ownerName) || !StringUtil.isEmpty(idCard) || !StringUtil.isEmpty(ownerNameLike)) {
            queryRoomByOwnerInfo(apiRoomVo, reqJson, cmdDataFlowContext);
            return;
        }

        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);

        //查询总记录数
        int total = roomInnerServiceSMOImpl.queryRoomsCount(BeanConvertUtil.covertBean(reqJson, RoomDto.class));
        apiRoomVo.setTotal(total);
        if (total > 0) {
            List<RoomDto> roomDtoList = roomInnerServiceSMOImpl.queryRooms(roomDto);
            //获取手机号、身份证号加密标识
            String flag = reqJson.getString("flag");
            refreshRoomOwners(reqJson.getString("loginUserId"), reqJson.getString("communityId"), roomDtoList, flag);

            apiRoomVo.setRooms(BeanConvertUtil.covertBeanList(roomDtoList, ApiRoomDataVo.class));
        } else {
            throw new IllegalArgumentException("查询业主房屋错误！");
        }
        int row = reqJson.getInteger("row");
        apiRoomVo.setRecords((int) Math.ceil((double) total / (double) row));

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiRoomVo), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);

    }


    /**
     * 根据业主查询 房屋信息
     *
     * @param apiRoomVo
     * @param reqJson
     */
    private void queryRoomByOwnerInfo(ApiRoomVo apiRoomVo, JSONObject reqJson, ICmdDataFlowContext cmdDataFlowContext) {

        OwnerRoomRelDto ownerRoomRelDto = BeanConvertUtil.covertBean(reqJson, OwnerRoomRelDto.class);
        ownerRoomRelDto.setByOwnerInfo(true);
        ownerRoomRelDto.setCommunityId(reqJson.getString("communityId"));
        int total = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRelsCount(ownerRoomRelDto);

        apiRoomVo.setTotal(total);
        if (total > 0) {
            List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
            List<RoomDto> roomDtoList = null;

            roomDtoList = refreshOwnerRooms(reqJson.getString("communityId"), ownerRoomRelDtos);

            apiRoomVo.setRooms(BeanConvertUtil.covertBeanList(roomDtoList, ApiRoomDataVo.class));
        }
        int row = reqJson.getInteger("row");
        apiRoomVo.setRecords((int) Math.ceil((double) apiRoomVo.getRooms().size() / (double) row));

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiRoomVo), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private List<RoomDto> refreshOwnerRooms(String communityId, List<OwnerRoomRelDto> ownerRoomRelDtos) {

        List<String> roomIds = new ArrayList<>();

        for (OwnerRoomRelDto ownerRoomRelDto : ownerRoomRelDtos) {
            roomIds.add(ownerRoomRelDto.getRoomId());
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(communityId);
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        for (RoomDto tmpRoomDto : roomDtos) {
            for (OwnerRoomRelDto ownerRoomRelDto : ownerRoomRelDtos) {
                if (tmpRoomDto.getRoomId().equals(ownerRoomRelDto.getRoomId())) {
                    tmpRoomDto.setOwnerId(ownerRoomRelDto.getOwnerId());
                    tmpRoomDto.setOwnerName(ownerRoomRelDto.getOwnerName());
                    tmpRoomDto.setIdCard(ownerRoomRelDto.getIdCard());
                    tmpRoomDto.setLink(ownerRoomRelDto.getLink());
                }
            }
        }
        return roomDtos;
    }

    /**
     * 刷入房屋业主信息
     *
     * @param roomDtos
     */
    private void refreshRoomOwners(String userId, String communityId, List<RoomDto> roomDtos, String flag) {
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
                if (roomDto.getRoomId().equals(tmpOwnerDto.getRoomId())) {
                    roomDto.setOwnerId(tmpOwnerDto.getOwnerId());
                    roomDto.setOwnerName(tmpOwnerDto.getName());
                    //对业主身份证号隐藏处理
                    String idCard = tmpOwnerDto.getIdCard();
                    if (mark.size() == 0 && idCard != null && !idCard.equals("") && StringUtil.isEmpty(flag)) {
                        idCard = idCard.substring(0, 6) + "**********" + idCard.substring(16);
                    }
                    //对业主手机号隐藏处理
                    String link = tmpOwnerDto.getLink();
                    if (mark.size() == 0 && link != null && !link.equals("") && StringUtil.isEmpty(flag)) {
                        link = link.substring(0, 3) + "****" + link.substring(7);
                    }
                    roomDto.setIdCard(idCard);
                    roomDto.setLink(link);
                }
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
