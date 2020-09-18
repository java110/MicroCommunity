package com.java110.store.bmo.contractType;
import com.java110.po.contractType.ContractTypePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteContractTypeBMO {


    /**
     * 修改合同类型
     * add by wuxw
     * @param contractTypePo
     * @return
     */
    ResponseEntity<String> delete(ContractTypePo contractTypePo);


}
