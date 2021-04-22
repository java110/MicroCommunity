package com.java110.store.bmo.contractRoom;

import com.java110.po.contractRoom.ContractRoomPo;
import org.springframework.http.ResponseEntity;
public interface ISaveContractRoomBMO {


    /**
     * 添加合同房屋
     * add by wuxw
     * @param contractRoomPo
     * @return
     */
    ResponseEntity<String> save(ContractRoomPo contractRoomPo);


}
