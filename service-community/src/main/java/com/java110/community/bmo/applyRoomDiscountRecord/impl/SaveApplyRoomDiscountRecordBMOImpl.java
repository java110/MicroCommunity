package com.java110.community.bmo.applyRoomDiscountRecord.impl;

import com.java110.community.bmo.applyRoomDiscountRecord.ISaveApplyRoomDiscountRecordBMO;
import com.java110.intf.community.IApplyRoomDiscountRecordInnerServiceSMO;
import com.java110.po.applyRoomDiscountRecord.ApplyRoomDiscountRecordPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 添加空置房验房记录实现类
 *
 * @author fqz
 * @date 2021-09-01
 */
@Service("saveApplyRoomDiscountRecordBMOImpl")
public class SaveApplyRoomDiscountRecordBMOImpl implements ISaveApplyRoomDiscountRecordBMO {

    @Autowired
    private IApplyRoomDiscountRecordInnerServiceSMO applyRoomDiscountRecordInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> saveRecord(ApplyRoomDiscountRecordPo applyRoomDiscountRecordPo) {
        int flag = applyRoomDiscountRecordInnerServiceSMOImpl.saveApplyRoomDiscountRecord(applyRoomDiscountRecordPo);
        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
