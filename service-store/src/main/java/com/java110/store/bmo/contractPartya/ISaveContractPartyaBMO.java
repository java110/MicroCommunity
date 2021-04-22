package com.java110.store.bmo.contractPartya;

import com.java110.po.contractPartya.ContractPartyaPo;
import org.springframework.http.ResponseEntity;
public interface ISaveContractPartyaBMO {


    /**
     * 添加合同房屋
     * add by wuxw
     * @param contractPartyaPo
     * @return
     */
    ResponseEntity<String> save(ContractPartyaPo contractPartyaPo);


}
