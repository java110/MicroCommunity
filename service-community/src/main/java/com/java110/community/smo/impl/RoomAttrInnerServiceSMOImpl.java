
package com.java110.community.smo.impl;


import com.java110.po.room.RoomAttrPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.community.dao.IRoomAttrServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IRoomAttrInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.RoomAttrDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 小区房屋属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class RoomAttrInnerServiceSMOImpl extends BaseServiceSMO implements IRoomAttrInnerServiceSMO {

    @Autowired
    private IRoomAttrServiceDao roomAttrServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<RoomAttrDto> queryRoomAttrs(@RequestBody RoomAttrDto roomAttrDto) {

        //校验是否传了 分页信息

        int page = roomAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            roomAttrDto.setPage((page - 1) * roomAttrDto.getRow());
        }

        List<RoomAttrDto> roomAttrs = BeanConvertUtil.covertBeanList(roomAttrServiceDaoImpl.getRoomAttrInfo(BeanConvertUtil.beanCovertMap(roomAttrDto)), RoomAttrDto.class);

        return roomAttrs;
    }



    @Override
    public int queryRoomAttrsCount(@RequestBody RoomAttrDto roomAttrDto) {
        return roomAttrServiceDaoImpl.queryRoomAttrsCount(BeanConvertUtil.beanCovertMap(roomAttrDto));
    }

    @Override
    public int saveRoomAttr(@RequestBody RoomAttrPo roomAttrPo) {
        return roomAttrServiceDaoImpl.saveRoomAttr(BeanConvertUtil.beanCovertMap(roomAttrPo));
    }

    @Override
    public int updateRoomAttrInfoInstance(@RequestBody RoomAttrPo roomAttrPo) {
        return roomAttrServiceDaoImpl.updateRoomAttrInfoInstance(BeanConvertUtil.beanCovertMap(roomAttrPo));
    }

    public IRoomAttrServiceDao getRoomAttrServiceDaoImpl() {
        return roomAttrServiceDaoImpl;
    }

    public void setRoomAttrServiceDaoImpl(IRoomAttrServiceDao roomAttrServiceDaoImpl) {
        this.roomAttrServiceDaoImpl = roomAttrServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
