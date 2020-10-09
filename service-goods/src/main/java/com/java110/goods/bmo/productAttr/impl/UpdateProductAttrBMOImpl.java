package com.java110.goods.bmo.productAttr.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.productAttr.IUpdateProductAttrBMO;
import com.java110.intf.IProductAttrInnerServiceSMO;
import com.java110.po.productAttr.ProductAttrPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateProductAttrBMOImpl")
public class UpdateProductAttrBMOImpl implements IUpdateProductAttrBMO {

    @Autowired
    private IProductAttrInnerServiceSMO productAttrInnerServiceSMOImpl;

    /**
     * @param productAttrPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ProductAttrPo productAttrPo) {

        int flag = productAttrInnerServiceSMOImpl.updateProductAttr(productAttrPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
