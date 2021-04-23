package com.java110.store.bmo.contractTypeTemplate;

import com.java110.dto.contractTypeTemplate.ContractTypeTemplateDto;
import org.springframework.http.ResponseEntity;

public interface IGetContractTypeTemplateBMO {


    /**
     * 查询合同属性
     * add by wuxw
     *
     * @param contractTypeTemplateDto
     * @return
     */
    ResponseEntity<String> get(ContractTypeTemplateDto contractTypeTemplateDto);


}
