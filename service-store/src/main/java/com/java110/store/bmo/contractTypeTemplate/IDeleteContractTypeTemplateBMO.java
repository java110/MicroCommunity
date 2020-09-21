package com.java110.store.bmo.contractTypeTemplate;

import com.java110.po.contractTypeTemplate.ContractTypeTemplatePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteContractTypeTemplateBMO {


    /**
     * 修改合同属性
     * add by wuxw
     *
     * @param contractTypeTemplatePo
     * @return
     */
    ResponseEntity<String> delete(ContractTypeTemplatePo contractTypeTemplatePo);


}
