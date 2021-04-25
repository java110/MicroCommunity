package com.java110.api.bmo.contractChangePlanRoom;
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
