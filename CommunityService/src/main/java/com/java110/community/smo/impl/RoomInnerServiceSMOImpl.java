package com.java110.community.smo.impl;


import com.java110.common.util.BeanConvertUtil;
import com.java110.community.dao.IRoomServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.room.IRoomInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.RoomDto;
import com.java110.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private IRoomServiceDao roomServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<RoomDto> queryRooms(@RequestBody RoomDto roomDto) {

        //校验是否传了 分页信息

        int page = roomDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            roomDto.setPage((page - 1) * roomDto.getRow());
            roomDto.setRow(page * roomDto.getRow());
        }

        List<RoomDto> rooms = BeanConvertUtil.covertBeanList(roomServiceDaoImpl.getRoomInfo(BeanConvertUtil.beanCovertMap(roomDto)), RoomDto.class);

        if (rooms == null || rooms.size() == 0) {
            return rooms;
        }

        String[] userIds = getUserIds(rooms);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (RoomDto room : rooms) {
            refreshRoom(room, users);
        }
        return rooms;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param room  小区小区房屋信息
     * @param users 用户列表
     */
    private void refreshRoom(RoomDto room, List<UserDto> users) {
        for (UserDto user : users) {
            if (room.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, room);
            }
        }
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

    @Override
    public int queryRoomsCount(@RequestBody RoomDto roomDto) {
        return roomServiceDaoImpl.queryRoomsCount(BeanConvertUtil.beanCovertMap(roomDto));
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
}
