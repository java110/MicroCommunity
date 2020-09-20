package com.java110.store.bmo.contract.impl;

import com.java110.dto.contract.ContractDto;
import com.java110.dto.contractAttr.ContractAttrDto;
import com.java110.intf.store.IContractAttrInnerServiceSMO;
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

    @Autowired
    private IContractAttrInnerServiceSMO contractAttrInnerServiceSMOImpl;

    /**
     * @param contractDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ContractDto contractDto) {


        int count = contractInnerServiceSMOImpl.queryContractsCount(contractDto);

        List<ContractDto> contractDtos = null;
        if (count > 0) {
            contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);
            refreshAttr(contractDtos);
        } else {
            contractDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractDto.getRow()), count, contractDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    /**
     * 属性属性
     *
     * @param contractDtos
     */
    private void refreshAttr(List<ContractDto> contractDtos) {
        List<String> contractIds = new ArrayList<>();
        for (ContractDto contractDto : contractDtos) {
            contractIds.add(contractDto.getContractId());
        }


        if (contractIds.size() < 1) {
            return;
        }

        ContractAttrDto contractAttrDto = new ContractAttrDto();
        contractAttrDto.setContractIds(contractIds.toArray(new String[contractIds.size()]));
        List<ContractAttrDto> contractAttrDtos = contractAttrInnerServiceSMOImpl.queryContractAttrs(contractAttrDto);

        List<ContractAttrDto> attrs = null;
        for (ContractDto contractDto : contractDtos) {
            attrs = new ArrayList<>();
            for (ContractAttrDto tmpContractAttrDto : contractAttrDtos) {
                if (contractDto.getContractId().equals(tmpContractAttrDto.getContractId())) {
                    attrs.add(tmpContractAttrDto);
                }
            }
            contractDto.setAttrs(attrs);
        }


    }

}
