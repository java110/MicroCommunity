package com.java110.user.smo.impl;

import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.roomRenovation.RoomRenovationDto;
import com.java110.intf.user.IRoomRenovationsInnerServiceSMO;
import com.java110.user.dao.IRoomRenovationsServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 房屋装修
 *
 * @author fqz
 * @date 2021-02-25 8:46
 */
@RestController
public class RoomRenovationsInnerServiceImpl extends BaseServiceSMO implements IRoomRenovationsInnerServiceSMO {

    @Autowired
    private IRoomRenovationsServiceDao roomRenovationsServiceDaoImpl;

    @Override
    public List<RoomRenovationDto> queryRoomRenovations(@RequestBody RoomRenovationDto roomRenovationDto) {
        //校验是否传了 分页信息

        int page = roomRenovationDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            roomRenovationDto.setPage((page - 1) * roomRenovationDto.getRow());
        }

        List<RoomRenovationDto> roomRenovations = BeanConvertUtil.covertBeanList(roomRenovationsServiceDaoImpl.getRoomRenovationInfo(BeanConvertUtil.beanCovertMap(roomRenovationDto)), RoomRenovationDto.class);

        return roomRenovations;
    }

    @Override
    public int queryRoomRenovationsCount(@RequestBody RoomRenovationDto roomRenovationDto) {
        return roomRenovationsServiceDaoImpl.queryRoomRenovationsCount(BeanConvertUtil.beanCovertMap(roomRenovationDto));
    }
}
