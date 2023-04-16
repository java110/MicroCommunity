package com.java110.store.bmo.contractChangePlanDetailAttr;
import com.java110.dto.contract.ContractChangePlanDetailAttrDto;
import org.springframework.http.ResponseEntity;
public interface IGetContractChangePlanDetailAttrBMO {


    /**
     * 查询合同变更属性
     * add by wuxw
     * @param  contractChangePlanDetailAttrDto
     * @return
     */
    ResponseEntity<String> get(ContractChangePlanDetailAttrDto contractChangePlanDetailAttrDto);


}
