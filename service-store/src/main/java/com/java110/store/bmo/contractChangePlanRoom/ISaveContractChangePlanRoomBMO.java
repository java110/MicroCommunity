package com.java110.api.bmo.contractChangePlanRoom;

import org.springframework.http.ResponseEntity;
public interface ISaveContractChangePlanRoomBMO {


    /**
     * 添加合同房屋变更
     * add by wuxw
     * @param contractChangePlanRoomPo
     * @return
     */
    ResponseEntity<String> save(ContractChangePlanRoomPo contractChangePlanRoomPo);


}
