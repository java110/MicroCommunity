package com.java110.store.bmo.contractType;

import com.java110.po.contractType.ContractTypePo;
import org.springframework.http.ResponseEntity;

public interface ISaveContractTypeBMO {


    /**
     * 添加合同类型
     * add by wuxw
     *
     * @param contractTypePo
     * @return
     */
    ResponseEntity<String> save(ContractTypePo contractTypePo);


}
