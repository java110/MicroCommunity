package com.java110.store.cmd.propertyIndex;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.FloorDto;
import com.java110.dto.RoomDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IFloorV1InnerServiceSMO;
import com.java110.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "propertyIndex.queryPropertyAssetsIndex")
public class QueryPropertyAssetsIndexCmd extends Cmd {

    @Autowired
    private IFloorInnerServiceSMO floorV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO  ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        JSONObject paramOut = new JSONObject();
        FloorDto floorDto = new FloorDto();
        floorDto.setCommunityId(reqJson.getString("communityId"));
        //查询楼栋
        int floorCount = floorV1InnerServiceSMOImpl.queryFloorsCount(floorDto);
        paramOut.put("floorCount", floorCount);

        //查询房屋
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(reqJson.getString("communityId"));
        roomDto.setRoomType(RoomDto.ROOM_TYPE_ROOM);
        int roomCount = roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
        paramOut.put("roomCount", roomCount);

        //查询商铺
        roomDto = new RoomDto();
        roomDto.setCommunityId(reqJson.getString("communityId"));
        roomDto.setRoomType(RoomDto.ROOM_TYPE_SHOPS);
        int shopCount = roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
        paramOut.put("shopCount", shopCount);

        //查看车位

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
        int spaceCount = parkingSpaceV1InnerServiceSMOImpl.queryParkingSpacesCount(parkingSpaceDto);
        paramOut.put("spaceCount", spaceCount);

        // 查询业主
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        int ownerCount = ownerV1InnerServiceSMOImpl.queryOwnersCount(ownerDto);
        paramOut.put("ownerCount", ownerCount);

        // 查询车辆
        OwnerCarDto ownerCarDto  =  new OwnerCarDto();
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        int carCount = ownerCarV1InnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto);
        paramOut.put("carCount", carCount);


        context.setResponseEntity(ResultVo.createResponseEntity(paramOut));
    }
}
