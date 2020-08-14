package com.java110.common.bmo.attrValue.impl;

import com.java110.common.bmo.attrValue.IDeleteAttrValueBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.IAttrValueInnerServiceSMO;
import com.java110.po.attrValue.AttrValuePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteAttrValueBMOImpl")
public class DeleteAttrValueBMOImpl implements IDeleteAttrValueBMO {

    @Autowired
    private IAttrValueInnerServiceSMO attrValueInnerServiceSMOImpl;

    /**
     * @param attrValuePo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(AttrValuePo attrValuePo) {

        int flag = attrValueInnerServiceSMOImpl.deleteAttrValue(attrValuePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
