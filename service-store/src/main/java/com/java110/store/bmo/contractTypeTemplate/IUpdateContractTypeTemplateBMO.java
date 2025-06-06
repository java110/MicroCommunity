package com.java110.store.bmo.contractTypeTemplate;

import com.java110.po.contract.ContractTypeTemplatePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateContractTypeTemplateBMO {


    /**
     * 修改合同属性
     * add by wuxw
     *
     * @param contractTypeTemplatePo
     * @return
     */
    ResponseEntity<String> update(ContractTypeTemplatePo contractTypeTemplatePo);


}
