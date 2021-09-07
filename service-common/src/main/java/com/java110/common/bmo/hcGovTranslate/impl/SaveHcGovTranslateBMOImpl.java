package com.java110.common.bmo.hcGovTranslate.impl;

import com.java110.common.bmo.hcGovTranslate.ISaveHcGovTranslateBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IHcGovTranslateInnerServiceSMO;
import com.java110.po.hcGovTranslate.HcGovTranslatePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("saveHcGovTranslateBMOImpl")
public class SaveHcGovTranslateBMOImpl implements ISaveHcGovTranslateBMO {

    @Autowired
    private IHcGovTranslateInnerServiceSMO hcGovTranslateInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param hcGovTranslatePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(HcGovTranslatePo hcGovTranslatePo) {

        hcGovTranslatePo.setTranId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_tranId));
        int flag = hcGovTranslateInnerServiceSMOImpl.saveHcGovTranslate(hcGovTranslatePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
