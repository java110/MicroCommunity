package com.java110.community.smo.impl;

import com.java110.community.dao.IRoomAttrServiceDao;
import com.java110.community.dao.IRoomServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.PageDto;
import com.java110.dto.RoomAttrDto;
import com.java110.dto.RoomDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.user.UserDto;
import com.java110.entity.assetImport.ImportCustomCreateFeeDto;
import com.java110.entity.assetImport.ImportRoomFee;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.room.RoomPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 小区房屋内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class RoomInnerServiceSMOImpl extends BaseServiceSMO implements IRoomInnerServiceSMO {

    private static final Logger logger = LoggerFactory.getLogger(RoomInnerServiceSMOImpl.class);

    @Autowired
    private IRoomServiceDao roomServiceDaoImpl;

    @Autowired
    private IRoomAttrServiceDao roomAttrServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Override
    public List<RoomDto> queryRooms(@RequestBody RoomDto roomDto) {

        //校验是否传了 分页信息

        int page = roomDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            roomDto.setPage((page - 1) * roomDto.getRow());
        }

        List<RoomDto> rooms = BeanConvertUtil.covertBeanList(roomServiceDaoImpl.getRoomInfoByCommunityId(BeanConvertUtil.beanCovertMap(roomDto)), RoomDto.class);

        if (rooms == null || rooms.size() == 0) {
            return rooms;
        }

        if (rooms.size() > 50) {
            return rooms;
        }

        String[] roomIds = getRoomIds(rooms);
        Map attrParamInfo = new HashMap();
        attrParamInfo.put("roomIds", roomIds);
        attrParamInfo.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<RoomAttrDto> roomAttrDtos = BeanConvertUtil.covertBeanList(roomAttrServiceDaoImpl.getRoomAttrInfo(attrParamInfo), RoomAttrDto.class);
        List<UserDto> users = null;
        if (rooms.size() < 2) {
            String[] userIds = getUserIds(rooms);
            //根据 userId 查询用户信息
            users = userInnerServiceSMOImpl.getUserInfo(userIds);
        } else {
            users = new ArrayList<>();
        }

        for (RoomDto room : rooms) {
            try {
                room.setApartmentName(MappingCache.getValue(room.getApartment().substring(0, 2).toString()) + MappingCache.getValue(room.getApartment().substring(2, 5).toString()));
            } catch (Exception e) {
                logger.error("设置房屋户型失败", e);
            }
            refreshRoom(room, users, roomAttrDtos);
        }
        return rooms;
    }

    @Override
    public int updateRooms(@RequestBody RoomPo roomPo) {
        roomServiceDaoImpl.updateRoomInfoInstance(BeanConvertUtil.beanCovertMap(roomPo));
        return 1;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param room         小区小区房屋信息
     * @param users        用户列表
     * @param roomAttrDtos 房屋属性信息
     */
    private void refreshRoom(RoomDto room, List<UserDto> users, List<RoomAttrDto> roomAttrDtos) {
        for (UserDto user : users) {
            if (room.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, room);
            }
        }

        if (roomAttrDtos == null || roomAttrDtos.size() == 0) {
            return;
        }

        List<RoomAttrDto> tmpRoomAttrDtos = new ArrayList<>();
        for (RoomAttrDto roomAttrDto : roomAttrDtos) {
            if (!roomAttrDto.getRoomId().equals(room.getRoomId())) {
                continue;
            }
            if (StringUtil.isEmpty(roomAttrDto.getValueName())) {
                roomAttrDto.setValueName(roomAttrDto.getValue());
            }
            tmpRoomAttrDtos.add(roomAttrDto);
        }
        room.setRoomAttrDto(tmpRoomAttrDtos);
    }

    /**
     * 获取批量userId
     *
     * @param rooms 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<RoomDto> rooms) {
        List<String> userIds = new ArrayList<String>();
        for (RoomDto room : rooms) {
            userIds.add(room.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    /**
     * 获取roomId 信息
     *
     * @param rooms 房屋信息
     * @return roomIds
     */
    private String[] getRoomIds(List<RoomDto> rooms) {
        List<String> roomIds = new ArrayList<String>();
        for (RoomDto room : rooms) {
            roomIds.add(room.getRoomId());
        }

        return roomIds.toArray(new String[roomIds.size()]);
    }

    @Override
    public int queryRoomsCount(@RequestBody RoomDto roomDto) {
        return roomServiceDaoImpl.queryRoomsByCommunityIdCount(BeanConvertUtil.beanCovertMap(roomDto));
    }

    @Override
    public int queryRoomsWithOutSellCount(@RequestBody RoomDto roomDto) {
        return roomServiceDaoImpl.queryRoomsWithOutSellByCommunityIdCount(BeanConvertUtil.beanCovertMap(roomDto));
    }

    @Override
    public List<RoomDto> queryRoomsWithOutSell(@RequestBody RoomDto roomDto) {

        //校验是否传了 分页信息

        int page = roomDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            roomDto.setPage((page - 1) * roomDto.getRow());
        }

        List<RoomDto> rooms = BeanConvertUtil.covertBeanList(roomServiceDaoImpl.getRoomInfoWithOutSellByCommunityId(BeanConvertUtil.beanCovertMap(roomDto)), RoomDto.class);

        if (rooms == null || rooms.size() == 0) {
            return rooms;
        }

        String[] roomIds = getRoomIds(rooms);
        Map attrParamInfo = new HashMap();
        attrParamInfo.put("roomIds", roomIds);
        attrParamInfo.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<RoomAttrDto> roomAttrDtos = BeanConvertUtil.covertBeanList(roomAttrServiceDaoImpl.getRoomAttrInfo(attrParamInfo), RoomAttrDto.class);

        String[] userIds = getUserIds(rooms);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (RoomDto room : rooms) {
            try {
                room.setApartmentName(MappingCache.getValue(room.getApartment().substring(0, 2).toString()) + MappingCache.getValue(room.getApartment().substring(2, 5).toString()));
            } catch (Exception e) {
                logger.error("设置房屋户型失败", e);
            }
            refreshRoom(room, users, roomAttrDtos);
        }
        return rooms;
    }

    @Override
    public int queryRoomsWithSellCount(@RequestBody RoomDto roomDto) {
        return roomServiceDaoImpl.queryRoomsWithSellByCommunityIdCount(BeanConvertUtil.beanCovertMap(roomDto));
    }

    @Override
    public List<RoomDto> queryRoomsWithSell(@RequestBody RoomDto roomDto) {

        //校验是否传了 分页信息

        int page = roomDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            roomDto.setPage((page - 1) * roomDto.getRow());
        }

        List<RoomDto> rooms = BeanConvertUtil.covertBeanList(roomServiceDaoImpl.getRoomInfoWithSellByCommunityId(BeanConvertUtil.beanCovertMap(roomDto)), RoomDto.class);

        if (rooms == null || rooms.size() == 0) {
            return rooms;
        }

        String[] roomIds = getRoomIds(rooms);
        Map attrParamInfo = new HashMap();
        attrParamInfo.put("roomIds", roomIds);
        attrParamInfo.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<RoomAttrDto> roomAttrDtos = BeanConvertUtil.covertBeanList(roomAttrServiceDaoImpl.getRoomAttrInfo(attrParamInfo), RoomAttrDto.class);

        String[] userIds = getUserIds(rooms);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (RoomDto room : rooms) {
            //处理下户型转义问题
            try {
                room.setApartmentName(MappingCache.getValue(room.getApartment().substring(0, 2).toString()) + MappingCache.getValue(room.getApartment().substring(2, 5).toString()));
            } catch (Exception e) {
                logger.error("设置房屋户型失败", e);
            }
            refreshRoom(room, users, roomAttrDtos);
        }
        return rooms;
    }

    @Override
    public List<RoomDto> queryRoomsByOwner(@RequestBody RoomDto roomDto) {

//        List<RoomDto> rooms = BeanConvertUtil.covertBeanList(roomServiceDaoImpl.getRoomInfoByOwner(BeanConvertUtil.beanCovertMap(roomDto)),
//                RoomDto.class);
//
//        if (rooms == null || rooms.size() == 0) {
//            return rooms;
//        }

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(roomDto.getOwnerId());
        ownerRoomRelDto.setOwnerNameLike(roomDto.getOwnerNameLike());
        ownerRoomRelDto.setStatusCd(roomDto.getStatusCd());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
            return new ArrayList<>();
        }

        List<String> roomIds = new ArrayList<>();
        for (OwnerRoomRelDto tmpOwnerRoomRelDto : ownerRoomRelDtos) {
            roomIds.add(tmpOwnerRoomRelDto.getRoomId());
        }
        RoomDto tmpRoomDto = new RoomDto();
        tmpRoomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        tmpRoomDto.setRoomNum(roomDto.getRoomNum());
        tmpRoomDto.setCommunityId(roomDto.getCommunityId());
        return queryRooms(tmpRoomDto);

    }

    /**
     * 刷新roomId
     *
     * @param importRoomFees 数据对象分享
     * @return
     */
    @Override
    public List<ImportRoomFee> freshRoomIds(@RequestBody List<ImportRoomFee> importRoomFees) {
        for (ImportRoomFee importRoomFee : importRoomFees) {
            List<Map> infos = null;
            if (!StringUtil.isEmpty(importRoomFee.getRoomId()) && !importRoomFee.getRoomId().startsWith("-")) {
                Map paramIn = new HashMap();
                paramIn.put("communityId", importRoomFee.getCommunityId());
                paramIn.put("roomId", importRoomFee.getRoomId());
                infos = roomServiceDaoImpl.getRoomInfos(BeanConvertUtil.beanCovertMap(importRoomFee));
            } else {
                infos = roomServiceDaoImpl.getRoomInfos(BeanConvertUtil.beanCovertMap(importRoomFee));
            }

            if (infos == null || infos.size() < 1) {
                continue;
            }
            importRoomFee.setRoomId(infos.get(0).get("roomId").toString());
            importRoomFee.setFloorNum(infos.get(0).get("floorNum").toString());
            importRoomFee.setUnitNum(infos.get(0).get("unitNum").toString());
            importRoomFee.setRoomNum(infos.get(0).get("roomNum").toString());
            importRoomFee.setRoomName(importRoomFee.getFloorNum()+"-"+importRoomFee.getUnitNum()+"-"+importRoomFee.getRoomNum());

        }
        return importRoomFees;
    }

    public List<ImportCustomCreateFeeDto> freshRoomIdsByImportCustomCreateFee(@RequestBody List<ImportCustomCreateFeeDto> importCustomCreateFeeDtos) {
        for (ImportCustomCreateFeeDto importRoomFee : importCustomCreateFeeDtos) {
            List<Map> infos = null;
            infos = roomServiceDaoImpl.getRoomInfos(BeanConvertUtil.beanCovertMap(importRoomFee));
            if (infos == null || infos.size() < 1) {
                continue;
            }
            importRoomFee.setPayObjId(infos.get(0).get("roomId").toString());
            importRoomFee.setFloorNum(infos.get(0).get("floorNum").toString());
            importRoomFee.setUnitNum(infos.get(0).get("unitNum").toString());
            importRoomFee.setRoomNum(infos.get(0).get("roomNum").toString());
        }
        return importCustomCreateFeeDtos;
    }

    public IRoomServiceDao getRoomServiceDaoImpl() {
        return roomServiceDaoImpl;
    }

    public void setRoomServiceDaoImpl(IRoomServiceDao roomServiceDaoImpl) {
        this.roomServiceDaoImpl = roomServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }

    public IRoomAttrServiceDao getRoomAttrServiceDaoImpl() {
        return roomAttrServiceDaoImpl;
    }

    public void setRoomAttrServiceDaoImpl(IRoomAttrServiceDao roomAttrServiceDaoImpl) {
        this.roomAttrServiceDaoImpl = roomAttrServiceDaoImpl;
    }
}
