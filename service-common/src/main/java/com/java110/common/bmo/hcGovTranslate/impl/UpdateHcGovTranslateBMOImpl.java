package com.java110.common.bmo.hcGovTranslate.impl;

import com.java110.common.bmo.hcGovTranslate.IUpdateHcGovTranslateBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.IHcGovTranslateInnerServiceSMO;
import com.java110.po.hcGovTranslate.HcGovTranslatePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("updateHcGovTranslateBMOImpl")
public class UpdateHcGovTranslateBMOImpl implements IUpdateHcGovTranslateBMO {

    @Autowired
    private IHcGovTranslateInnerServiceSMO hcGovTranslateInnerServiceSMOImpl;

    /**
     *
     *
     * @param hcGovTranslatePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(HcGovTranslatePo hcGovTranslatePo) {

        int flag = hcGovTranslateInnerServiceSMOImpl.updateHcGovTranslate(hcGovTranslatePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
