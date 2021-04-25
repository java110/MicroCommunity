package com.java110.store.bmo.contractChangePlanRoom.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.store.IContractChangePlanRoomInnerServiceSMO;
import com.java110.po.contractChangePlanRoom.ContractChangePlanRoomPo;
import com.java110.store.bmo.contractChangePlanRoom.IUpdateContractChangePlanRoomBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateContractChangePlanRoomBMOImpl")
public class UpdateContractChangePlanRoomBMOImpl implements IUpdateContractChangePlanRoomBMO {

    @Autowired
    private IContractChangePlanRoomInnerServiceSMO contractChangePlanRoomInnerServiceSMOImpl;

    /**
     * @param contractChangePlanRoomPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ContractChangePlanRoomPo contractChangePlanRoomPo) {

        int flag = contractChangePlanRoomInnerServiceSMOImpl.updateContractChangePlanRoom(contractChangePlanRoomPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
