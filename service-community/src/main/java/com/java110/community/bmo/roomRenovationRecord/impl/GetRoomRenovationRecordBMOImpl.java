package com.java110.community.bmo.roomRenovationRecord.impl;

import com.java110.community.bmo.roomRenovationRecord.IGetRoomRenovationRecordBMO;
import com.java110.intf.community.IRoomRenovationRecordInnerServiceSMO;
import com.java110.po.roomRenovationRecord.RoomRenovationRecordPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询装修记录信息
 *
 * @author fqz
 * @date 2021-02-23 17:02
 */
@Service("getRoomRenovationRecordBMOImpl")
public class GetRoomRenovationRecordBMOImpl implements IGetRoomRenovationRecordBMO {

    @Autowired
    private IRoomRenovationRecordInnerServiceSMO roomRenovationRecordInnerServiceSMOImpl;

    /**
     * 查询装修记录信息(与文件表关联)
     *
     * @param roomRenovationRecordPo
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(RoomRenovationRecordPo roomRenovationRecordPo) {
        int count = roomRenovationRecordInnerServiceSMOImpl.queryRoomRenovationRecordsCount(roomRenovationRecordPo);
        List<RoomRenovationRecordPo> roomRenovationRecordPos = new ArrayList<>();
        if (count > 0) {
            List<RoomRenovationRecordPo> roomRenovationRecordList = roomRenovationRecordInnerServiceSMOImpl.queryRoomRenovationRecords(roomRenovationRecordPo);
            String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");
            for (RoomRenovationRecordPo renovationRecordPo : roomRenovationRecordList) {
                if (!StringUtil.isEmpty(renovationRecordPo.getFileRealName()) && renovationRecordPo.getRelTypeCd().equals("19000")) {
                    renovationRecordPo.setUrl(imgUrl +
                            renovationRecordPo.getFileRealName());
                } else if (!StringUtil.isEmpty(renovationRecordPo.getFileRealName()) && renovationRecordPo.getRelTypeCd().equals("21000")) {
                    renovationRecordPo.setUrl(imgUrl +
                            renovationRecordPo.getFileRealName());
                }
                roomRenovationRecordPos.add(renovationRecordPo);
            }
        } else {
            roomRenovationRecordPos = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) roomRenovationRecordPo.getRow()), count, roomRenovationRecordPos);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }

    /**
     * 查询装修记录
     *
     * @param roomRenovationRecordPo
     * @return
     */
    @Override
    public ResponseEntity<String> getRoomRenovationRecord(RoomRenovationRecordPo roomRenovationRecordPo) {
        int count = roomRenovationRecordInnerServiceSMOImpl.getRoomRenovationRecordsCount(roomRenovationRecordPo);
        List<RoomRenovationRecordPo> roomRenovationRecordPos = null;
        if (count > 0) {
            roomRenovationRecordPos = roomRenovationRecordInnerServiceSMOImpl.getRoomRenovationRecords(roomRenovationRecordPo);
        } else {
            roomRenovationRecordPos = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) roomRenovationRecordPo.getRow()), count, roomRenovationRecordPos);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }
}
