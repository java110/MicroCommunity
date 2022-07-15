package com.java110.community.cmd.index;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.store.StoreDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.ApiIndexStatisticVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "index.queryIndexStatistic")
public class QueryIndexStatisticCmd extends Cmd {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;
    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        String storeId = context.getReqHeaders().get("store-id");
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(storeId);
        storeDto.setPage(1);
        storeDto.setRow(1);
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

        Assert.listOnlyOne(storeDtos, "商户不存在");
// 查询业主 总数量
        ApiIndexStatisticVo apiIndexStatisticVo = new ApiIndexStatisticVo();
        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);
        int ownerCount = ownerInnerServiceSMOImpl.queryOwnersCount(ownerDto);
        int noEnterRoomOwnerCount = ownerInnerServiceSMOImpl.queryNoEnterRoomOwnerCount(ownerDto);
        // 查询房屋 总数量
        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);
        roomDto.setRoomType(RoomDto.ROOM_TYPE_ROOM);
        int roomCount = roomInnerServiceSMOImpl.queryRoomsCount(roomDto);
        int freeRoomCount = roomInnerServiceSMOImpl.queryRoomsWithOutSellCount(roomDto);
        // 查询停车位 总数量
        int parkingSpaceCount = parkingSpaceInnerServiceSMOImpl.queryParkingSpacesCount(BeanConvertUtil.covertBean(reqJson, ParkingSpaceDto.class));
        ParkingSpaceDto parkingSpaceDto = BeanConvertUtil.covertBean(reqJson, ParkingSpaceDto.class);
        parkingSpaceDto.setState("F");
        int freeParkingSpaceCount = parkingSpaceInnerServiceSMOImpl.queryParkingSpacesCount(parkingSpaceDto);
        // 查询商铺 总数量
        roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);
        roomDto.setRoomType(RoomDto.ROOM_TYPE_SHOPS);
        int shopCount = roomInnerServiceSMOImpl.queryRoomsCount(roomDto);
        int freeShopCount = roomInnerServiceSMOImpl.queryRoomsWithOutSellCount(roomDto);



        apiIndexStatisticVo.setOwnerCount(ownerCount + "");
        apiIndexStatisticVo.setNoEnterRoomCount(noEnterRoomOwnerCount + "");
        apiIndexStatisticVo.setRoomCount(roomCount + "");
        apiIndexStatisticVo.setFreeRoomCount(freeRoomCount + "");
        apiIndexStatisticVo.setParkingSpaceCount(parkingSpaceCount + "");
        apiIndexStatisticVo.setFreeParkingSpaceCount(freeParkingSpaceCount + "");
        apiIndexStatisticVo.setShopCount(shopCount + "");
        apiIndexStatisticVo.setFreeShopCount(freeShopCount + "");
        apiIndexStatisticVo.setStoreTypeCd(storeDtos.get(0).getStoreTypeCd());
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiIndexStatisticVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
