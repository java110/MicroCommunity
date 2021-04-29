package com.java110.goods.bmo.productSpec.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.productSpec.IDeleteProductSpecBMO;
import com.java110.intf.goods.IProductSpecDetailInnerServiceSMO;
import com.java110.intf.goods.IProductSpecInnerServiceSMO;
import com.java110.po.product.ProductSpecDetailPo;
import com.java110.po.product.ProductSpecPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteProductSpecBMOImpl")
public class DeleteProductSpecBMOImpl implements IDeleteProductSpecBMO {

    @Autowired
    private IProductSpecInnerServiceSMO productSpecInnerServiceSMOImpl;

    @Autowired
    private IProductSpecDetailInnerServiceSMO productSpecDetailInnerServiceSMOImpl;

    /**
     * @param productSpecPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ProductSpecPo productSpecPo) {

        int flag = productSpecInnerServiceSMOImpl.deleteProductSpec(productSpecPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "删除失败");

        }
        ProductSpecDetailPo tmpProductSpecDetailPo = new ProductSpecDetailPo();
        tmpProductSpecDetailPo.setSpecId(productSpecPo.getSpecId());
        tmpProductSpecDetailPo.setStoreId(productSpecPo.getStoreId());
        flag = productSpecDetailInnerServiceSMOImpl.deleteProductSpecDetail(tmpProductSpecDetailPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "删除失败");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "删除失败");

    }

}
