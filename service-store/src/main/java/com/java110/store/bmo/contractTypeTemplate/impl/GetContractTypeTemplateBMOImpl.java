package com.java110.store.bmo.contractTypeTemplate.impl;

import com.java110.dto.contract.ContractTypeTemplateDto;
import com.java110.intf.store.IContractTypeTemplateInnerServiceSMO;
import com.java110.store.bmo.contractTypeTemplate.IGetContractTypeTemplateBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getContractTypeTemplateBMOImpl")
public class GetContractTypeTemplateBMOImpl implements IGetContractTypeTemplateBMO {

    @Autowired
    private IContractTypeTemplateInnerServiceSMO contractTypeTemplateInnerServiceSMOImpl;

    /**
     * @param contractTypeTemplateDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ContractTypeTemplateDto contractTypeTemplateDto) {


        int count = contractTypeTemplateInnerServiceSMOImpl.queryContractTypeTemplatesCount(contractTypeTemplateDto);

        List<ContractTypeTemplateDto> contractTypeTemplateDtos = null;
        if (count > 0) {
            contractTypeTemplateDtos = contractTypeTemplateInnerServiceSMOImpl.queryContractTypeTemplates(contractTypeTemplateDto);
        } else {
            contractTypeTemplateDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractTypeTemplateDto.getRow()), count, contractTypeTemplateDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
