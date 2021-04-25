package com.java110.store.bmo.contractChangePlanRoom;

import com.java110.po.contractChangePlanRoom.ContractChangePlanRoomPo;
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
