package com.java110.store.bmo.contractTypeSpec;

import com.java110.po.contractTypeSpec.ContractTypeSpecPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateContractTypeSpecBMO {


    /**
     * 修改合同类型规格
     * add by wuxw
     *
     * @param contractTypeSpecPo
     * @return
     */
    ResponseEntity<String> update(ContractTypeSpecPo contractTypeSpecPo);


}
