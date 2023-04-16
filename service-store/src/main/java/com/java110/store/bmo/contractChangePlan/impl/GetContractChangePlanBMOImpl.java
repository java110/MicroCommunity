package com.java110.store.bmo.contractChangePlan.impl;

import com.java110.dto.contract.ContractChangePlanDto;
import com.java110.intf.store.IContractChangePlanInnerServiceSMO;
import com.java110.store.bmo.contractChangePlan.IGetContractChangePlanBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getContractChangePlanBMOImpl")
public class GetContractChangePlanBMOImpl implements IGetContractChangePlanBMO {

    @Autowired
    private IContractChangePlanInnerServiceSMO contractChangePlanInnerServiceSMOImpl;

    /**
     * @param contractChangePlanDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ContractChangePlanDto contractChangePlanDto) {


        int count = contractChangePlanInnerServiceSMOImpl.queryContractChangePlansCount(contractChangePlanDto);

        List<ContractChangePlanDto> contractChangePlanDtos = null;
        if (count > 0) {
            contractChangePlanDtos = contractChangePlanInnerServiceSMOImpl.queryContractChangePlans(contractChangePlanDto);
        } else {
            contractChangePlanDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractChangePlanDto.getRow()), count, contractChangePlanDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
