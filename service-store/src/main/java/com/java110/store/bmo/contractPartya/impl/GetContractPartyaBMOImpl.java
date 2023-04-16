package com.java110.store.bmo.contractPartya.impl;

import com.java110.dto.contract.ContractPartyaDto;
import com.java110.intf.store.IContractPartyaInnerServiceSMO;
import com.java110.store.bmo.contractPartya.IGetContractPartyaBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getContractPartyaBMOImpl")
public class GetContractPartyaBMOImpl implements IGetContractPartyaBMO {

    @Autowired
    private IContractPartyaInnerServiceSMO contractPartyaInnerServiceSMOImpl;

    /**
     * @param contractPartyaDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ContractPartyaDto contractPartyaDto) {


        int count = contractPartyaInnerServiceSMOImpl.queryContractPartyasCount(contractPartyaDto);

        List<ContractPartyaDto> contractPartyaDtos = null;
        if (count > 0) {
            contractPartyaDtos = contractPartyaInnerServiceSMOImpl.queryContractPartyas(contractPartyaDto);
        } else {
            contractPartyaDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractPartyaDto.getRow()), count, contractPartyaDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
