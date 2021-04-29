package com.java110.fee.bmo.feePrintSpec.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.feePrintSpec.IDeleteFeePrintSpecBMO;
import com.java110.intf.fee.IFeePrintSpecInnerServiceSMO;
import com.java110.po.feePrintSpec.FeePrintSpecPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteFeePrintSpecBMOImpl")
public class DeleteFeePrintSpecBMOImpl implements IDeleteFeePrintSpecBMO {

    @Autowired
    private IFeePrintSpecInnerServiceSMO feePrintSpecInnerServiceSMOImpl;

    /**
     * @param feePrintSpecPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(FeePrintSpecPo feePrintSpecPo) {

        int flag = feePrintSpecInnerServiceSMOImpl.deleteFeePrintSpec(feePrintSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
