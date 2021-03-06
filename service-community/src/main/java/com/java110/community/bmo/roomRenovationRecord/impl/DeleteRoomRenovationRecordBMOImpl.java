package com.java110.community.bmo.roomRenovationRecord.impl;

import com.java110.community.bmo.roomRenovationRecord.IDeleteRoomRenovationRecordBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.community.IRoomRenovationRecordInnerServiceSMO;
import com.java110.po.roomRenovationRecord.RoomRenovationRecordPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 删除装修记录
 *
 * @author fqz
 * @date 2021-02-27 11:52
 */
@Service("deleteRoomRenovationRecordBMOImpl")
public class DeleteRoomRenovationRecordBMOImpl implements IDeleteRoomRenovationRecordBMO {

    @Autowired
    private IRoomRenovationRecordInnerServiceSMO roomRenovationRecordInnerServiceSMOImpl;

    @Java110Transactional
    public ResponseEntity<String> delete(RoomRenovationRecordPo roomRenovationRecordPo) {
        int flag = roomRenovationRecordInnerServiceSMOImpl.deleteRoomRenovationRecord(roomRenovationRecordPo);
        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
