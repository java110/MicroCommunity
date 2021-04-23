package com.java110.store.bmo.contractFile;
import com.java110.po.contractFile.ContractFilePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateContractFileBMO {


    /**
     * 修改合同附件
     * add by wuxw
     * @param contractFilePo
     * @return
     */
    ResponseEntity<String> update(ContractFilePo contractFilePo);


}
