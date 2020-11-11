package com.java110.user.bmo.rentingPoolFlow.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.user.IRentingPoolFlowInnerServiceSMO;
import com.java110.po.rentingPoolFlow.RentingPoolFlowPo;
import com.java110.user.bmo.rentingPoolFlow.ISaveRentingPoolFlowBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveRentingPoolFlowBMOImpl")
public class SaveRentingPoolFlowBMOImpl implements ISaveRentingPoolFlowBMO {

    @Autowired
    private IRentingPoolFlowInnerServiceSMO rentingPoolFlowInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param rentingPoolFlowPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(RentingPoolFlowPo rentingPoolFlowPo) {

        rentingPoolFlowPo.setFlowId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_flowId));
        int flag = rentingPoolFlowInnerServiceSMOImpl.saveRentingPoolFlow(rentingPoolFlowPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
