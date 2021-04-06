package com.java110.community.bmo.repairReturnVisit.impl;

import com.java110.community.bmo.repairReturnVisit.ISaveRepairReturnVisitBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.repair.RepairDto;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairReturnVisitInnerServiceSMO;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.repairReturnVisit.RepairReturnVisitPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveRepairReturnVisitBMOImpl")
public class SaveRepairReturnVisitBMOImpl implements ISaveRepairReturnVisitBMO {

    @Autowired
    private IRepairReturnVisitInnerServiceSMO repairReturnVisitInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param repairReturnVisitPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(RepairReturnVisitPo repairReturnVisitPo) {

        repairReturnVisitPo.setVisitId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_visitId));
        int flag = repairReturnVisitInnerServiceSMOImpl.saveRepairReturnVisit(repairReturnVisitPo);

        if (flag < 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        RepairPoolPo repairPoolPo = new RepairPoolPo();
        repairPoolPo.setRepairId(repairReturnVisitPo.getRepairId());
        repairPoolPo.setState(RepairDto.STATE_COMPLATE);
        flag = repairInnerServiceSMOImpl.updateRepair(repairPoolPo);

        if (flag < 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");

    }

}
