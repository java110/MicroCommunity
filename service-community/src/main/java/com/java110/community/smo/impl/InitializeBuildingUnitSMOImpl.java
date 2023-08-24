package com.java110.community.smo.impl;


import com.java110.community.dao.IInitializeBuildingUnitDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.community.IinitializeBuildingUnitSMO;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户服务信息管理业务信息实现
 * Created by wuxw on 2017/4/5.
 */
@Service
public class InitializeBuildingUnitSMOImpl extends BaseServiceSMO implements IinitializeBuildingUnitSMO {

    private static Logger logger = LoggerFactory.getLogger(InitializeBuildingUnitSMOImpl.class);

    public static final int DEFAULT_ROW = 200;
    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IInitializeBuildingUnitDao initializeBuildingUnitDaoImpl;

    @Override
    public int deleteBuildingUnit(@RequestBody Map floorIds) {
        int deleteFlag = initializeBuildingUnitDaoImpl.deleteBuildingUnit(floorIds);
        return deleteFlag;
    }

    @Override
    public int deletefFloor(@RequestBody Map communityId) {
        int deleteFlag = initializeBuildingUnitDaoImpl.deletefFloor(communityId);
        return deleteFlag;
    }

    @Override
    public int deleteBuildingRoom(@RequestBody Map communityId) {
        int deleteFlag = initializeBuildingUnitDaoImpl.deleteBuildingRoom(communityId);

        //todo 自动解绑房屋 先注释，子查询删除报错
        // initializeBuildingUnitDaoImpl.deleteOwnerRoomRel(communityId);
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(communityId.get("communityId").toString());
        int count = roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
        int page = (int) Math.floor(count / DEFAULT_ROW);
        List<String> roomIds = null;
        for (int pageIndex = 1; pageIndex < page + 1; pageIndex++) {
            roomDto = new RoomDto();
            roomDto.setPage(pageIndex);
            roomDto.setRow(DEFAULT_ROW);
            roomDto.setCommunityId(communityId.get("communityId").toString());
            List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
            if (roomDtos == null || roomDtos.size() < 1) {
                continue;
            }
            roomIds = new ArrayList<>();
            for (RoomDto tmpRoomDto : roomDtos) {
                roomIds.add(tmpRoomDto.getRoomId());
            }
            communityId.put("roomIds", roomIds.toArray(new String[roomIds.size()]));
            initializeBuildingUnitDaoImpl.deleteOwnerRoomRel(communityId);

        }
        return deleteFlag;
    }

    @Override
    public int deleteParkingArea(@RequestBody Map communityId) {
        int deleteFlag = initializeBuildingUnitDaoImpl.deleteParkingArea(communityId);
        return deleteFlag;
    }
    @Override
    public int deleteParkingSpace(@RequestBody Map communityId) {
        int deleteFlag = initializeBuildingUnitDaoImpl.deleteParkingSpace(communityId);
        return deleteFlag;
    }

    @Override
    public int deleteOwnerCar(@RequestBody Map communityId) {
        int deleteFlag = initializeBuildingUnitDaoImpl.deleteOwnerCar(communityId);
        return deleteFlag;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        InitializeBuildingUnitSMOImpl.logger = logger;
    }

    public IInitializeBuildingUnitDao getInitializeBuildingUnitDaoImpl() {
        return initializeBuildingUnitDaoImpl;
    }

    public void setInitializeBuildingUnitDaoImpl(IInitializeBuildingUnitDao initializeBuildingUnitDaoImpl) {
        this.initializeBuildingUnitDaoImpl = initializeBuildingUnitDaoImpl;
    }
}
