package com.java110.goods.bmo.product.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.product.IUpdateProductBMO;
import com.java110.intf.goods.IProductInnerServiceSMO;
import com.java110.po.product.ProductPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateProductBMOImpl")
public class UpdateProductBMOImpl implements IUpdateProductBMO {

    @Autowired
    private IProductInnerServiceSMO productInnerServiceSMOImpl;

    /**
     * @param productPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ProductPo productPo) {

        int flag = productInnerServiceSMOImpl.updateProduct(productPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
