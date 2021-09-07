package com.java110.community.bmo.applyRoomDiscountRecord.impl;

import com.java110.community.bmo.applyRoomDiscountRecord.IDeleteApplyRoomDiscountRecordBMO;
import com.java110.intf.community.IApplyRoomDiscountRecordInnerServiceSMO;
import com.java110.po.applyRoomDiscountRecord.ApplyRoomDiscountRecordPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 删除空置房验房记录
 *
 * @author fqz
 * @date 2021-09-01 10:39
 */
@Service("deleteApplyRoomDiscountRecordBMOImpl")
public class DeleteApplyRoomDiscountRecordBMOImpl implements IDeleteApplyRoomDiscountRecordBMO {

    @Autowired
    private IApplyRoomDiscountRecordInnerServiceSMO applyRoomDiscountRecordInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> delete(ApplyRoomDiscountRecordPo applyRoomDiscountRecordPo) {
        int flag = applyRoomDiscountRecordInnerServiceSMOImpl.deleteApplyRoomDiscountRecord(applyRoomDiscountRecordPo);
        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "删除成功");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "删除失败");
    }

}
