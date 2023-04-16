package com.java110.store.bmo.contractTypeSpec;

import com.java110.dto.contract.ContractTypeSpecDto;
import org.springframework.http.ResponseEntity;

public interface IGetContractTypeSpecBMO {


    /**
     * 查询合同类型规格
     * add by wuxw
     *
     * @param contractTypeSpecDto
     * @return
     */
    ResponseEntity<String> get(ContractTypeSpecDto contractTypeSpecDto);


}
