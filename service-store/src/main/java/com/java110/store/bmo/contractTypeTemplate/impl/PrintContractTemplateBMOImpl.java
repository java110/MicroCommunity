package com.java110.store.bmo.contractTypeTemplate.impl;

import com.java110.dto.contract.ContractDto;
import com.java110.dto.contract.ContractAttrDto;
import com.java110.dto.contract.ContractTypeSpecDto;
import com.java110.dto.contract.ContractTypeTemplateDto;
import com.java110.entity.mapping.Mapping;
import com.java110.intf.store.IContractAttrInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.store.IContractTypeSpecInnerServiceSMO;
import com.java110.intf.store.IContractTypeTemplateInnerServiceSMO;
import com.java110.store.bmo.contractTypeTemplate.IPrintContractTemplateBMO;
import com.java110.utils.cache.MappingCache;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("printContractTemplateBMOImpl")
public class PrintContractTemplateBMOImpl implements IPrintContractTemplateBMO {
    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IContractAttrInnerServiceSMO contractAttrInnerServiceSMOImpl;

    @Autowired
    private IContractTypeTemplateInnerServiceSMO contractTypeTemplateInnerServiceSMOImpl;

    @Autowired
    private IContractTypeSpecInnerServiceSMO contractTypeSpecInnerServiceSMOImpl;
    //域
    public static final String STORE_CONTRACT = "STORE.CONTRACT";

    /**
     * @param contractTypeTemplateDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ContractTypeTemplateDto contractTypeTemplateDto, ContractDto contractDto, ContractTypeSpecDto contractTypeSpecDto ) {
        int count = contractInnerServiceSMOImpl.queryContractsCount(contractDto);
        Map<String, Object> result = new HashMap<String, Object>();
        List<ContractDto> contractDtos = null;
        if (count > 0) {
            contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);
            refreshAttr(contractDtos);
        } else {
            contractDtos = new ArrayList<>();
        }
        result.put("contract",contractDtos);
        int count2 = contractTypeTemplateInnerServiceSMOImpl.queryContractTypeTemplatesCount(contractTypeTemplateDto);
        List<ContractTypeTemplateDto> contractTypeTemplateDtos = null;
        if (count2 > 0) {
            contractTypeTemplateDtos = contractTypeTemplateInnerServiceSMOImpl.queryContractTypeTemplates(contractTypeTemplateDto);
        } else {
            contractTypeTemplateDtos = new ArrayList<>();
        }
        result.put("contractTypeTemplate",contractTypeTemplateDtos);
        int count3 = contractTypeSpecInnerServiceSMOImpl.queryContractTypeSpecsCount(contractTypeSpecDto);
        List<ContractTypeSpecDto> contractTypeSpecDtos = null;
        if (count3 > 0) {
            contractTypeSpecDtos = contractTypeSpecInnerServiceSMOImpl.queryContractTypeSpecs(contractTypeSpecDto);
        } else {
            contractTypeSpecDtos = new ArrayList<>();
        }
        result.put("contractTypeSpec",contractTypeSpecDtos);
        List<Mapping> baseRepalce = MappingCache.getValueByDomain(STORE_CONTRACT);
        result.put("baseRepalce",baseRepalce);
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractTypeTemplateDto.getRow()), count, result);
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
