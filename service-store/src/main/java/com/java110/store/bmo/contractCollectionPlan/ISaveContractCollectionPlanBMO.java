package com.java110.store.bmo.contractCollectionPlan;

import com.java110.po.contractCollectionPlan.ContractCollectionPlanPo;
import org.springframework.http.ResponseEntity;
public interface ISaveContractCollectionPlanBMO {


    /**
     * 添加合同收款计划
     * add by wuxw
     * @param contractCollectionPlanPo
     * @return
     */
    ResponseEntity<String> save(ContractCollectionPlanPo contractCollectionPlanPo);


}
