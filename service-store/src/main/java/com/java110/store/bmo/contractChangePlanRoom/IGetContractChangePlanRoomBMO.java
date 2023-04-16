package com.java110.store.bmo.contractChangePlanRoom;
import com.java110.dto.contract.ContractChangePlanRoomDto;
import org.springframework.http.ResponseEntity;
public interface IGetContractChangePlanRoomBMO {


    /**
     * 查询合同房屋变更
     * add by wuxw
     * @param  contractChangePlanRoomDto
     * @return
     */
    ResponseEntity<String> get(ContractChangePlanRoomDto contractChangePlanRoomDto);


}
