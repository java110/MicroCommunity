package com.java110.store.bmo.contractChangePlanRoom;
import com.java110.po.contractChangePlanRoom.ContractChangePlanRoomPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteContractChangePlanRoomBMO {


    /**
     * 修改合同房屋变更
     * add by wuxw
     * @param contractChangePlanRoomPo
     * @return
     */
    ResponseEntity<String> delete(ContractChangePlanRoomPo contractChangePlanRoomPo);


}
