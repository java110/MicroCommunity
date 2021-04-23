package com.java110.goods.bmo.productCategory.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.productCategory.IDeleteProductCategoryBMO;
import com.java110.intf.goods.IProductCategoryInnerServiceSMO;
import com.java110.po.product.ProductCategoryPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteProductCategoryBMOImpl")
public class DeleteProductCategoryBMOImpl implements IDeleteProductCategoryBMO {

    @Autowired
    private IProductCategoryInnerServiceSMO productCategoryInnerServiceSMOImpl;

    /**
     * @param productCategoryPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ProductCategoryPo productCategoryPo) {

        int flag = productCategoryInnerServiceSMOImpl.deleteProductCategory(productCategoryPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
