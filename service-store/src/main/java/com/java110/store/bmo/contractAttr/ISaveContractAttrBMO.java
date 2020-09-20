package com.java110.store.bmo.contractAttr;

import com.java110.po.contractAttr.ContractAttrPo;
import org.springframework.http.ResponseEntity;

public interface ISaveContractAttrBMO {


    /**
     * 添加合同属性
     * add by wuxw
     *
     * @param contractAttrPo
     * @return
     */
    ResponseEntity<String> save(ContractAttrPo contractAttrPo);


}
