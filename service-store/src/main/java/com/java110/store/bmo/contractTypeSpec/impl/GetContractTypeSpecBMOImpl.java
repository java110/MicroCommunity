package com.java110.store.bmo.contractTypeSpec.impl;

import com.java110.dto.contract.ContractTypeSpecDto;
import com.java110.intf.store.IContractTypeSpecInnerServiceSMO;
import com.java110.store.bmo.contractTypeSpec.IGetContractTypeSpecBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getContractTypeSpecBMOImpl")
public class GetContractTypeSpecBMOImpl implements IGetContractTypeSpecBMO {

    @Autowired
    private IContractTypeSpecInnerServiceSMO contractTypeSpecInnerServiceSMOImpl;

    /**
     * @param contractTypeSpecDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ContractTypeSpecDto contractTypeSpecDto) {


        int count = contractTypeSpecInnerServiceSMOImpl.queryContractTypeSpecsCount(contractTypeSpecDto);

        List<ContractTypeSpecDto> contractTypeSpecDtos = null;
        if (count > 0) {
            contractTypeSpecDtos = contractTypeSpecInnerServiceSMOImpl.queryContractTypeSpecs(contractTypeSpecDto);
        } else {
            contractTypeSpecDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractTypeSpecDto.getRow()), count, contractTypeSpecDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
