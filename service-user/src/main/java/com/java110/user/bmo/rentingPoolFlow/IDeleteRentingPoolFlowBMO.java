package com.java110.user.bmo.rentingPoolFlow;
import com.java110.po.rentingPoolFlow.RentingPoolFlowPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteRentingPoolFlowBMO {


    /**
     * 修改出租流程
     * add by wuxw
     * @param rentingPoolFlowPo
     * @return
     */
    ResponseEntity<String> delete(RentingPoolFlowPo rentingPoolFlowPo);


}
