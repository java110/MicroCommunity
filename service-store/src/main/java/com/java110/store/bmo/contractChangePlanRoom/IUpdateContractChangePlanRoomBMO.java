package com.java110.api.bmo.contractChangePlanRoom;
import org.springframework.http.ResponseEntity;

public interface IUpdateContractChangePlanRoomBMO {


    /**
     * 修改合同房屋变更
     * add by wuxw
     * @param contractChangePlanRoomPo
     * @return
     */
    ResponseEntity<String> update(ContractChangePlanRoomPo contractChangePlanRoomPo);


}
