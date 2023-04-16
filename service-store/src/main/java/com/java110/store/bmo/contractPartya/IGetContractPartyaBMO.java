package com.java110.store.bmo.contractPartya;
import com.java110.dto.contract.ContractPartyaDto;
import org.springframework.http.ResponseEntity;
public interface IGetContractPartyaBMO {


    /**
     * 查询合同房屋
     * add by wuxw
     * @param  contractPartyaDto
     * @return
     */
    ResponseEntity<String> get(ContractPartyaDto contractPartyaDto);


}
