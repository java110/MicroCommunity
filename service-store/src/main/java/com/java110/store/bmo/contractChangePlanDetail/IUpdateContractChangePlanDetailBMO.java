package com.java110.store.bmo.contractChangePlanDetail;
import com.java110.po.contractChangePlanDetail.ContractChangePlanDetailPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateContractChangePlanDetailBMO {


    /**
     * 修改合同变更明细
     * add by wuxw
     * @param contractChangePlanDetailPo
     * @return
     */
    ResponseEntity<String> update(ContractChangePlanDetailPo contractChangePlanDetailPo);


}
