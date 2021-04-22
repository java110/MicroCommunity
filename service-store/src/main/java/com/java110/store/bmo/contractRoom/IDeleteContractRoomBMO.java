package com.java110.store.bmo.contractRoom;
import com.java110.po.contractRoom.ContractRoomPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteContractRoomBMO {


    /**
     * 修改合同房屋
     * add by wuxw
     * @param contractRoomPo
     * @return
     */
    ResponseEntity<String> delete(ContractRoomPo contractRoomPo);


}
