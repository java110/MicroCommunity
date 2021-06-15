package com.java110.store.bmo.contractRoom.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.po.contractRoom.ContractRoomPo;
import com.java110.store.bmo.contractRoom.ISaveContractRoomBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveContractRoomBMOImpl")
public class SaveContractRoomBMOImpl implements ISaveContractRoomBMO {

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractRoomPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ContractRoomPo contractRoomPo) {

        contractRoomPo.setCrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_crId));
        int flag = contractRoomInnerServiceSMOImpl.saveContractRoom(contractRoomPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
