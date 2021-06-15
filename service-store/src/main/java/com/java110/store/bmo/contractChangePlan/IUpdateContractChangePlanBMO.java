package com.java110.store.bmo.contractChangePlan;
import com.java110.po.contractChangePlan.ContractChangePlanPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateContractChangePlanBMO {


    /**
     * 修改合同变更计划
     * add by wuxw
     * @param contractChangePlanPo
     * @return
     */
    ResponseEntity<String> update(ContractChangePlanPo contractChangePlanPo);


}
