package com.java110.store.bmo.contractChangePlanDetail;

import com.java110.po.contractChangePlanDetail.ContractChangePlanDetailPo;
import org.springframework.http.ResponseEntity;
public interface ISaveContractChangePlanDetailBMO {


    /**
     * 添加合同变更明细
     * add by wuxw
     * @param contractChangePlanDetailPo
     * @return
     */
    ResponseEntity<String> save(ContractChangePlanDetailPo contractChangePlanDetailPo);


}
