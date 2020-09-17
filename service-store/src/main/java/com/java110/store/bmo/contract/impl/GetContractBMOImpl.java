package com.java110.store.bmo.contract.impl;

import com.java110.dto.contract.ContractDto;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.store.bmo.contract.IGetContractBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getContractBMOImpl")
public class GetContractBMOImpl implements IGetContractBMO {

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    /**
     * @param contractDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ContractDto contractDto) {


        int count = contractInnerServiceSMOImpl.queryContractsCount(contractDto);

        List<ContractDto> contractDtos = null;
        if (count > 0) {
            contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);
        } else {
            contractDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractDto.getRow()), count, contractDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
