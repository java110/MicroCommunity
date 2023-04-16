package com.java110.store.bmo.contractCollectionPlan.impl;

import com.java110.dto.contract.ContractCollectionPlanDto;
import com.java110.intf.store.IContractCollectionPlanInnerServiceSMO;
import com.java110.store.bmo.contractCollectionPlan.IGetContractCollectionPlanBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getContractCollectionPlanBMOImpl")
public class GetContractCollectionPlanBMOImpl implements IGetContractCollectionPlanBMO {

    @Autowired
    private IContractCollectionPlanInnerServiceSMO contractCollectionPlanInnerServiceSMOImpl;

    /**
     * @param contractCollectionPlanDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ContractCollectionPlanDto contractCollectionPlanDto) {


        int count = contractCollectionPlanInnerServiceSMOImpl.queryContractCollectionPlansCount(contractCollectionPlanDto);

        List<ContractCollectionPlanDto> contractCollectionPlanDtos = null;
        if (count > 0) {
            contractCollectionPlanDtos = contractCollectionPlanInnerServiceSMOImpl.queryContractCollectionPlans(contractCollectionPlanDto);
        } else {
            contractCollectionPlanDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractCollectionPlanDto.getRow()), count, contractCollectionPlanDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
