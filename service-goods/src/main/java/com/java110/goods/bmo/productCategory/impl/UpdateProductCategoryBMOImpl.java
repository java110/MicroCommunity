package com.java110.goods.bmo.productCategory.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.productCategory.IUpdateProductCategoryBMO;
import com.java110.intf.goods.IProductCategoryInnerServiceSMO;
import com.java110.po.product.ProductCategoryPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateProductCategoryBMOImpl")
public class UpdateProductCategoryBMOImpl implements IUpdateProductCategoryBMO {

    @Autowired
    private IProductCategoryInnerServiceSMO productCategoryInnerServiceSMOImpl;

    /**
     * @param productCategoryPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ProductCategoryPo productCategoryPo) {

        int flag = productCategoryInnerServiceSMOImpl.updateProductCategory(productCategoryPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
