package com.java110.store.bmo.contractTypeSpec;

import com.java110.po.contractTypeSpec.ContractTypeSpecPo;
import org.springframework.http.ResponseEntity;

public interface ISaveContractTypeSpecBMO {


    /**
     * 添加合同类型规格
     * add by wuxw
     *
     * @param contractTypeSpecPo
     * @return
     */
    ResponseEntity<String> save(ContractTypeSpecPo contractTypeSpecPo);


}
