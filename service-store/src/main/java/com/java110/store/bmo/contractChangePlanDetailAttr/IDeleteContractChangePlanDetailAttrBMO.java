package com.java110.store.bmo.contractChangePlanDetailAttr;

import com.java110.po.contractChangePlanDetailAttr.ContractChangePlanDetailAttrPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteContractChangePlanDetailAttrBMO {


    /**
     * 修改合同变更属性
     * add by wuxw
     *
     * @param contractChangePlanDetailAttrPo
     * @return
     */
    ResponseEntity<String> delete(ContractChangePlanDetailAttrPo contractChangePlanDetailAttrPo);


}
