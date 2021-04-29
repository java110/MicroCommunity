package com.java110.common.bmo.attrSpec.impl;

import com.java110.common.bmo.attrSpec.IUpdateAttrSpecBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.IAttrSpecInnerServiceSMO;
import com.java110.po.attrSpec.AttrSpecPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateAttrSpecBMOImpl")
public class UpdateAttrSpecBMOImpl implements IUpdateAttrSpecBMO {

    @Autowired
    private IAttrSpecInnerServiceSMO attrSpecInnerServiceSMOImpl;

    /**
     * @param attrSpecPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(AttrSpecPo attrSpecPo) {

        int flag = attrSpecInnerServiceSMOImpl.updateAttrSpec(attrSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
