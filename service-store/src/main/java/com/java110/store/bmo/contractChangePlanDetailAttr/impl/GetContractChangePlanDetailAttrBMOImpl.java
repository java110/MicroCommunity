package com.java110.store.bmo.contractChangePlanDetailAttr.impl;

import com.java110.dto.contract.ContractChangePlanDetailAttrDto;
import com.java110.intf.store.IContractChangePlanDetailAttrInnerServiceSMO;
import com.java110.store.bmo.contractChangePlanDetailAttr.IGetContractChangePlanDetailAttrBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getContractChangePlanDetailAttrBMOImpl")
public class GetContractChangePlanDetailAttrBMOImpl implements IGetContractChangePlanDetailAttrBMO {

    @Autowired
    private IContractChangePlanDetailAttrInnerServiceSMO contractChangePlanDetailAttrInnerServiceSMOImpl;

    /**
     * @param contractChangePlanDetailAttrDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ContractChangePlanDetailAttrDto contractChangePlanDetailAttrDto) {


        int count = contractChangePlanDetailAttrInnerServiceSMOImpl.queryContractChangePlanDetailAttrsCount(contractChangePlanDetailAttrDto);

        List<ContractChangePlanDetailAttrDto> contractChangePlanDetailAttrDtos = null;
        if (count > 0) {
            contractChangePlanDetailAttrDtos = contractChangePlanDetailAttrInnerServiceSMOImpl.queryContractChangePlanDetailAttrs(contractChangePlanDetailAttrDto);
        } else {
            contractChangePlanDetailAttrDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractChangePlanDetailAttrDto.getRow()), count, contractChangePlanDetailAttrDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
