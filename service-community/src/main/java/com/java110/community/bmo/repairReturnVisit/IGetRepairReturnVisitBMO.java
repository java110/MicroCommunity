package com.java110.community.bmo.repairReturnVisit;
import com.java110.dto.repair.RepairReturnVisitDto;
import org.springframework.http.ResponseEntity;
public interface IGetRepairReturnVisitBMO {


    /**
     * 查询报修回访
     * add by wuxw
     * @param  repairReturnVisitDto
     * @return
     */
    ResponseEntity<String> get(RepairReturnVisitDto repairReturnVisitDto);


}
