package com.java110.store.bmo.contractPartya;

import com.java110.po.contractPartya.ContractPartyaPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteContractPartyaBMO {


    /**
     * 修改合同房屋
     * add by wuxw
     *
     * @param contractPartyaPo
     * @return
     */
    ResponseEntity<String> delete(ContractPartyaPo contractPartyaPo);


}
