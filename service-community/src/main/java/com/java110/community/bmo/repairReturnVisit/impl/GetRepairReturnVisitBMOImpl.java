package com.java110.community.bmo.repairReturnVisit.impl;

import com.java110.community.bmo.repairReturnVisit.IGetRepairReturnVisitBMO;
import com.java110.dto.repairReturnVisit.RepairReturnVisitDto;
import com.java110.intf.community.IRepairReturnVisitInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getRepairReturnVisitBMOImpl")
public class GetRepairReturnVisitBMOImpl implements IGetRepairReturnVisitBMO {

    @Autowired
    private IRepairReturnVisitInnerServiceSMO repairReturnVisitInnerServiceSMOImpl;

    /**
     * @param repairReturnVisitDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(RepairReturnVisitDto repairReturnVisitDto) {


        int count = repairReturnVisitInnerServiceSMOImpl.queryRepairReturnVisitsCount(repairReturnVisitDto);

        List<RepairReturnVisitDto> repairReturnVisitDtos = null;
        if (count > 0) {
            repairReturnVisitDtos = repairReturnVisitInnerServiceSMOImpl.queryRepairReturnVisits(repairReturnVisitDto);
        } else {
            repairReturnVisitDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) repairReturnVisitDto.getRow()), count, repairReturnVisitDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
