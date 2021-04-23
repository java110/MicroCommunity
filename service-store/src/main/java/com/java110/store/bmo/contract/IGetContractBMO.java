package com.java110.store.bmo.contract;

import com.java110.dto.contract.ContractDto;
import org.springframework.http.ResponseEntity;

public interface IGetContractBMO {


    /**
     * 查询合同管理
     * add by wuxw
     *
     * @param contractDto
     * @return
     */
    ResponseEntity<String> get(ContractDto contractDto);


}
