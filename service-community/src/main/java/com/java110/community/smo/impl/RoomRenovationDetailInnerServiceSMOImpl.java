package com.java110.community.smo.impl;


import com.java110.community.dao.IRoomRenovationDetailServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.roomRenovationDetail.RoomRenovationDetailDto;
import com.java110.intf.community.IRoomRenovationDetailInnerServiceSMO;
import com.java110.po.roomRenovationDetail.RoomRenovationDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 装修明细内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class RoomRenovationDetailInnerServiceSMOImpl extends BaseServiceSMO implements IRoomRenovationDetailInnerServiceSMO {

    @Autowired
    private IRoomRenovationDetailServiceDao roomRenovationDetailServiceDaoImpl;


    @Override
    public int saveRoomRenovationDetail(@RequestBody RoomRenovationDetailPo roomRenovationDetailPo) {
        int saveFlag = 1;
        roomRenovationDetailServiceDaoImpl.saveRoomRenovationDetailInfo(BeanConvertUtil.beanCovertMap(roomRenovationDetailPo));
        return saveFlag;
    }

    @Override
    public int updateRoomRenovationDetail(@RequestBody RoomRenovationDetailPo roomRenovationDetailPo) {
        int saveFlag = 1;
        roomRenovationDetailServiceDaoImpl.updateRoomRenovationDetailInfo(BeanConvertUtil.beanCovertMap(roomRenovationDetailPo));
        return saveFlag;
    }

    @Override
    public int deleteRoomRenovationDetail(@RequestBody RoomRenovationDetailPo roomRenovationDetailPo) {
        int saveFlag = 1;
        roomRenovationDetailPo.setStatusCd("1");
        roomRenovationDetailServiceDaoImpl.updateRoomRenovationDetailInfo(BeanConvertUtil.beanCovertMap(roomRenovationDetailPo));
        return saveFlag;
    }

    @Override
    public List<RoomRenovationDetailDto> queryRoomRenovationDetails(@RequestBody RoomRenovationDetailDto roomRenovationDetailDto) {

        //校验是否传了 分页信息

        int page = roomRenovationDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            roomRenovationDetailDto.setPage((page - 1) * roomRenovationDetailDto.getRow());
        }

        List<RoomRenovationDetailDto> roomRenovationDetails = BeanConvertUtil.covertBeanList(roomRenovationDetailServiceDaoImpl.getRoomRenovationDetailInfo(BeanConvertUtil.beanCovertMap(roomRenovationDetailDto)), RoomRenovationDetailDto.class);

        return roomRenovationDetails;
    }


    @Override
    public int queryRoomRenovationDetailsCount(@RequestBody RoomRenovationDetailDto roomRenovationDetailDto) {
        return roomRenovationDetailServiceDaoImpl.queryRoomRenovationDetailsCount(BeanConvertUtil.beanCovertMap(roomRenovationDetailDto));
    }

    public IRoomRenovationDetailServiceDao getRoomRenovationDetailServiceDaoImpl() {
        return roomRenovationDetailServiceDaoImpl;
    }

    public void setRoomRenovationDetailServiceDaoImpl(IRoomRenovationDetailServiceDao roomRenovationDetailServiceDaoImpl) {
        this.roomRenovationDetailServiceDaoImpl = roomRenovationDetailServiceDaoImpl;
    }
}
