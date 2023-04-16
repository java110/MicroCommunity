package com.java110.store.bmo.contractType.impl;

import com.java110.dto.contract.ContractTypeDto;
import com.java110.intf.store.IContractTypeInnerServiceSMO;
import com.java110.store.bmo.contractType.IGetContractTypeBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getContractTypeBMOImpl")
public class GetContractTypeBMOImpl implements IGetContractTypeBMO {

    @Autowired
    private IContractTypeInnerServiceSMO contractTypeInnerServiceSMOImpl;

    /**
     * @param contractTypeDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ContractTypeDto contractTypeDto) {


        int count = contractTypeInnerServiceSMOImpl.queryContractTypesCount(contractTypeDto);

        List<ContractTypeDto> contractTypeDtos = null;
        if (count > 0) {
            contractTypeDtos = contractTypeInnerServiceSMOImpl.queryContractTypes(contractTypeDto);
        } else {
            contractTypeDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractTypeDto.getRow()), count, contractTypeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
