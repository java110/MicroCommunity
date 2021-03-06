package com.java110.community.bmo.roomRenovationRecord.impl;

import com.java110.community.bmo.roomRenovationRecord.ISaveRoomRenovationRecordBMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.IRoomRenovationRecordInnerServiceSMO;
import com.java110.po.roomRenovationRecord.RoomRenovationRecordPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 存储装修记录信息实现类
 *
 * @author fqz
 * @date 2021-02-23 17:00
 */
@Service("saveRoomRenovationRecordBMOImpl")
public class SaveRoomRenovationRecordBMOImpl implements ISaveRoomRenovationRecordBMO {

    @Autowired
    private IRoomRenovationRecordInnerServiceSMO roomRenovationRecordInnerServiceSMOImpl;

    /**
     * 添加装修记录
     *
     * @param roomRenovationRecordPo
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> saveRecord(RoomRenovationRecordPo roomRenovationRecordPo) {
        roomRenovationRecordPo.setRecordId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rId));
        int flag = roomRenovationRecordInnerServiceSMOImpl.saveRoomRenovationRecord(roomRenovationRecordPo);
        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
