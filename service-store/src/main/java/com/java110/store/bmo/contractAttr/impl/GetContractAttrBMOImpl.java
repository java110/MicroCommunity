package com.java110.store.bmo.contractAttr.impl;

import com.java110.dto.contract.ContractAttrDto;
import com.java110.intf.store.IContractAttrInnerServiceSMO;
import com.java110.store.bmo.contractAttr.IGetContractAttrBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getContractAttrBMOImpl")
public class GetContractAttrBMOImpl implements IGetContractAttrBMO {

    @Autowired
    private IContractAttrInnerServiceSMO contractAttrInnerServiceSMOImpl;

    /**
     * @param contractAttrDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ContractAttrDto contractAttrDto) {


        int count = contractAttrInnerServiceSMOImpl.queryContractAttrsCount(contractAttrDto);

        List<ContractAttrDto> contractAttrDtos = null;
        if (count > 0) {
            contractAttrDtos = contractAttrInnerServiceSMOImpl.queryContractAttrs(contractAttrDto);
        } else {
            contractAttrDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractAttrDto.getRow()), count, contractAttrDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
