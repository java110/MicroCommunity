package com.java110.store.bmo.contract;

import com.java110.dto.contract.ContractDto;
import com.java110.entity.audit.AuditUser;
import org.springframework.http.ResponseEntity;

public interface IGetContractBMO {


    /**
     * 查询合同管理
     * add by wuxw
     *
     * @param contractDto
     * @return
     */
    ResponseEntity<String> get(ContractDto contractDto);

    ResponseEntity<String> queryContractTask(AuditUser auditUser);

    ResponseEntity<String> queryContractHistoryTask(AuditUser auditUser);

    ResponseEntity<String> queryContractChangeTask(AuditUser auditUser);

    ResponseEntity<String> queryContractChangeHistoryTask(AuditUser auditUser);

}
