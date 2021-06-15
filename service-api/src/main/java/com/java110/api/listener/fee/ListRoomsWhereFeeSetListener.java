package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeFeeConfigConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.ApiRoomDataVo;
import com.java110.vo.api.ApiRoomVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 查询需要设置费用的房屋
 */
@Java110Listener("listRoomsWhereFeeSetListener")
public class ListRoomsWhereFeeSetListener extends AbstractServiceApiListener {

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeFeeConfigConstant.LIST_ROOMS_WHERE_FEE_SET;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ApiRoomVo apiRoomVo = new ApiRoomVo();
        //根据 业主来定位房屋信息
        if (reqJson.containsKey("ownerName") || reqJson.containsKey("idCard")) {
            queryRoomByOwnerInfo(apiRoomVo, reqJson, context);
            return;
        }

        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);

        //查询总记录数
        int total = roomInnerServiceSMOImpl.queryRoomsCount(BeanConvertUtil.covertBean(reqJson, RoomDto.class));
        apiRoomVo.setTotal(total);
        if (total > 0) {
            List<RoomDto> roomDtoList = roomInnerServiceSMOImpl.queryRooms(roomDto);
            String userId = context.getUserId();
            refreshRoomOwners(userId, reqJson.getString("communityId"), roomDtoList);

            apiRoomVo.setRooms(BeanConvertUtil.covertBeanList(roomDtoList, ApiRoomDataVo.class));
        }
        int row = reqJson.getInteger("row");
        apiRoomVo.setRecords((int) Math.ceil((double) total / (double) row));

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiRoomVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);

    }

    /**
     * 根据业主查询 房屋信息
     *
     * @param apiRoomVo
     * @param reqJson
     */
    private void queryRoomByOwnerInfo(ApiRoomVo apiRoomVo, JSONObject reqJson, DataFlowContext context) {

        OwnerRoomRelDto ownerRoomRelDto = BeanConvertUtil.covertBean(reqJson, OwnerRoomRelDto.class);
        ownerRoomRelDto.setByOwnerInfo(true);
        int total = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRelsCount(ownerRoomRelDto);

        apiRoomVo.setTotal(total);
        if (total > 0) {
            List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
            List<RoomDto> roomDtoList = null;

            roomDtoList = refreshOwnerRooms(reqJson.getString("communityId"), ownerRoomRelDtos);

            apiRoomVo.setRooms(BeanConvertUtil.covertBeanList(roomDtoList, ApiRoomDataVo.class));
        }
        int row = reqJson.getInteger("row");
        apiRoomVo.setRecords((int) Math.ceil((double) total / (double) row));

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiRoomVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
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
    private void refreshRoomOwners(String userId, String communityId, List<RoomDto> roomDtos) {
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
                    if (mark.size() == 0 && idCard != null && !idCard.equals("")) {
                        idCard = idCard.substring(0, 6) + "**********" + idCard.substring(16);
                    }
                    //对业主手机号隐藏处理
                    String link = tmpOwnerDto.getLink();
                    if (mark.size() == 0 && link != null && !link.equals("")) {
                        link = link.substring(0, 3) + "****" + link.substring(7);
                    }
                    roomDto.setIdCard(idCard);
                    roomDto.setLink(link);
                }
            }
        }
    }

    public IRoomInnerServiceSMO getRoomInnerServiceSMOImpl() {
        return roomInnerServiceSMOImpl;
    }

    public void setRoomInnerServiceSMOImpl(IRoomInnerServiceSMO roomInnerServiceSMOImpl) {
        this.roomInnerServiceSMOImpl = roomInnerServiceSMOImpl;
    }

    public IOwnerInnerServiceSMO getOwnerInnerServiceSMOImpl() {
        return ownerInnerServiceSMOImpl;
    }

    public void setOwnerInnerServiceSMOImpl(IOwnerInnerServiceSMO ownerInnerServiceSMOImpl) {
        this.ownerInnerServiceSMOImpl = ownerInnerServiceSMOImpl;
    }

    public IOwnerRoomRelInnerServiceSMO getOwnerRoomRelInnerServiceSMOImpl() {
        return ownerRoomRelInnerServiceSMOImpl;
    }

    public void setOwnerRoomRelInnerServiceSMOImpl(IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl) {
        this.ownerRoomRelInnerServiceSMOImpl = ownerRoomRelInnerServiceSMOImpl;
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
