package com.java110.store.bmo.contractTypeTemplate;

import com.java110.dto.contract.ContractDto;
import com.java110.dto.contract.ContractTypeSpecDto;
import com.java110.dto.contract.ContractTypeTemplateDto;
import org.springframework.http.ResponseEntity;

public interface IPrintContractTemplateBMO {


    /**
     * 查询合同属性及模板
     * add by wuxw
     *
     * @param contractTypeTemplateDto
     * @return
     */
    ResponseEntity<String> get(ContractTypeTemplateDto contractTypeTemplateDto, ContractDto contractDto, ContractTypeSpecDto contractTypeSpecDto);


}
