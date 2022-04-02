package com.java110.store.bmo.contractChangePlan;

import com.alibaba.fastjson.JSONObject;
import com.java110.po.contractChangePlan.ContractChangePlanPo;
import com.java110.po.contractChangePlanDetail.ContractChangePlanDetailPo;
import com.java110.po.contractChangePlanRoom.ContractChangePlanRoomPo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ISaveContractChangePlanBMO {


    /**
     * 添加合同变更计划
     * add by wuxw
     * @param contractChangePlanPo
     * @return
     */
    ResponseEntity<String> save(ContractChangePlanPo contractChangePlanPo,
                                ContractChangePlanDetailPo contractChangePlanDetailPo,
                                List<ContractChangePlanRoomPo> contractChangePlanRoomPos,
                                JSONObject reqJson);


}
