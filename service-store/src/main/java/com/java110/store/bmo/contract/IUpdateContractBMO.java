package com.java110.store.bmo.contract;
import com.java110.po.contract.ContractPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateContractBMO {


    /**
     * 修改合同管理
     * add by wuxw
     * @param contractPo
     * @return
     */
    ResponseEntity<String> update(ContractPo contractPo);


}
