package com.java110.community.smo.impl;

import com.java110.community.dao.IRoomRenovationRecordServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.intf.community.IRoomRenovationRecordInnerServiceSMO;
import com.java110.po.roomRenovationRecord.RoomRenovationRecordPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoomRenovationRecordInnerServiceSMOImpl extends BaseServiceSMO implements IRoomRenovationRecordInnerServiceSMO {

    @Autowired
    private IRoomRenovationRecordServiceDao roomRenovationRecordServiceDaoImpl;

    @Override
    public int saveRoomRenovationRecord(RoomRenovationRecordPo roomRenovationRecordPo) {
        int saveFlag = 1;
        roomRenovationRecordServiceDaoImpl.saveRoomRenovationRecordInfo(BeanConvertUtil.beanCovertMap(roomRenovationRecordPo));
        return saveFlag;
    }

    @Override
    public List<RoomRenovationRecordPo> queryRoomRenovationRecords(RoomRenovationRecordPo roomRenovationRecordPo) {
        //校验是否传了 分页信息
        int page = roomRenovationRecordPo.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            roomRenovationRecordPo.setPage((page - 1) * roomRenovationRecordPo.getRow());
        }
        List<RoomRenovationRecordPo> roomRenovationRecordPos = BeanConvertUtil.covertBeanList(roomRenovationRecordServiceDaoImpl.getRoomRenovationRecordsInfo(BeanConvertUtil.beanCovertMap(roomRenovationRecordPo)), RoomRenovationRecordPo.class);
        return roomRenovationRecordPos;
    }

    @Override
    public List<RoomRenovationRecordPo> getRoomRenovationRecords(RoomRenovationRecordPo roomRenovationRecordPo) {
        //校验是否传了 分页信息
        int page = roomRenovationRecordPo.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            roomRenovationRecordPo.setPage((page - 1) * roomRenovationRecordPo.getRow());
        }
        List<RoomRenovationRecordPo> roomRenovationRecordPos = BeanConvertUtil.covertBeanList(roomRenovationRecordServiceDaoImpl.findRoomRenovationRecordsInfo(BeanConvertUtil.beanCovertMap(roomRenovationRecordPo)), RoomRenovationRecordPo.class);
        return roomRenovationRecordPos;
    }

    @Override
    public int queryRoomRenovationRecordsCount(RoomRenovationRecordPo roomRenovationRecordPo) {
        return roomRenovationRecordServiceDaoImpl.queryRoomRenovationRecordsCount(BeanConvertUtil.beanCovertMap(roomRenovationRecordPo));
    }

    @Override
    public int getRoomRenovationRecordsCount(RoomRenovationRecordPo roomRenovationRecordPo) {
        return roomRenovationRecordServiceDaoImpl.getRoomRenovationRecordsCount(BeanConvertUtil.beanCovertMap(roomRenovationRecordPo));
    }

    @Override
    public int deleteRoomRenovationRecord(RoomRenovationRecordPo roomRenovationRecordPo) {
        int saveFlag = 1;
        roomRenovationRecordPo.setStatusCd("1");
        roomRenovationRecordServiceDaoImpl.updateRoomRenovationRecordInfo(BeanConvertUtil.beanCovertMap(roomRenovationRecordPo));
        return saveFlag;
    }

}
