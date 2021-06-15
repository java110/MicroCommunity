package com.java110.community.bmo.repairReturnVisit.impl;

import com.java110.community.bmo.repairReturnVisit.IDeleteRepairReturnVisitBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.community.IRepairReturnVisitInnerServiceSMO;
import com.java110.po.repairReturnVisit.RepairReturnVisitPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteRepairReturnVisitBMOImpl")
public class DeleteRepairReturnVisitBMOImpl implements IDeleteRepairReturnVisitBMO {

    @Autowired
    private IRepairReturnVisitInnerServiceSMO repairReturnVisitInnerServiceSMOImpl;

    /**
     * @param repairReturnVisitPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(RepairReturnVisitPo repairReturnVisitPo) {

        int flag = repairReturnVisitInnerServiceSMOImpl.deleteRepairReturnVisit(repairReturnVisitPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
