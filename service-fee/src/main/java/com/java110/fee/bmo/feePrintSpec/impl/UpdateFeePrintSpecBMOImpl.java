package com.java110.fee.bmo.feePrintSpec.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.feePrintSpec.IUpdateFeePrintSpecBMO;
import com.java110.intf.fee.IFeePrintSpecInnerServiceSMO;
import com.java110.po.feePrintSpec.FeePrintSpecPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeePrintSpecBMOImpl")
public class UpdateFeePrintSpecBMOImpl implements IUpdateFeePrintSpecBMO {

    @Autowired
    private IFeePrintSpecInnerServiceSMO feePrintSpecInnerServiceSMOImpl;

    /**
     * @param feePrintSpecPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(FeePrintSpecPo feePrintSpecPo) {

        int flag = feePrintSpecInnerServiceSMOImpl.updateFeePrintSpec(feePrintSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
