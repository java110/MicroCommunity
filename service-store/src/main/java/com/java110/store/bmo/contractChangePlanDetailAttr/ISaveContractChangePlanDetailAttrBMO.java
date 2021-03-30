package com.java110.store.bmo.contractChangePlanDetailAttr;

import com.java110.po.contractChangePlanDetailAttr.ContractChangePlanDetailAttrPo;
import org.springframework.http.ResponseEntity;
public interface ISaveContractChangePlanDetailAttrBMO {


    /**
     * 添加合同变更属性
     * add by wuxw
     * @param contractChangePlanDetailAttrPo
     * @return
     */
    ResponseEntity<String> save(ContractChangePlanDetailAttrPo contractChangePlanDetailAttrPo);


}
