package com.java110.community.bmo.repairReturnVisit;

import com.java110.po.repair.RepairReturnVisitPo;
import org.springframework.http.ResponseEntity;

public interface ISaveRepairReturnVisitBMO {


    /**
     * 添加报修回访
     * add by wuxw
     *
     * @param repairReturnVisitPo
     * @return
     */
    ResponseEntity<String> save(RepairReturnVisitPo repairReturnVisitPo);


}
