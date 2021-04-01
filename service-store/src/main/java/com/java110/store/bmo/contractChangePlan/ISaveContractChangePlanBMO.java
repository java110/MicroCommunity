package com.java110.store.bmo.contractChangePlan;

import com.java110.po.contractChangePlan.ContractChangePlanPo;
import com.java110.po.contractChangePlanDetail.ContractChangePlanDetailPo;
import org.springframework.http.ResponseEntity;
public interface ISaveContractChangePlanBMO {


    /**
     * 添加合同变更计划
     * add by wuxw
     * @param contractChangePlanPo
     * @return
     */
    ResponseEntity<String> save(ContractChangePlanPo contractChangePlanPo, ContractChangePlanDetailPo contractChangePlanDetailPo);


}
