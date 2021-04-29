package com.java110.goods.bmo.productSpec.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.goods.bmo.productSpec.IUpdateProductSpecBMO;
import com.java110.intf.goods.IProductSpecDetailInnerServiceSMO;
import com.java110.intf.goods.IProductSpecInnerServiceSMO;
import com.java110.po.product.ProductSpecDetailPo;
import com.java110.po.product.ProductSpecPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("updateProductSpecBMOImpl")
public class UpdateProductSpecBMOImpl implements IUpdateProductSpecBMO {

    @Autowired
    private IProductSpecInnerServiceSMO productSpecInnerServiceSMOImpl;

    @Autowired
    private IProductSpecDetailInnerServiceSMO productSpecDetailInnerServiceSMOImpl;

    /**
     * @param productSpecPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ProductSpecPo productSpecPo, List<ProductSpecDetailPo> productSpecDetailPos) {

        int flag = productSpecInnerServiceSMOImpl.updateProductSpec(productSpecPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        ProductSpecDetailPo tmpProductSpecDetailPo = new ProductSpecDetailPo();
        tmpProductSpecDetailPo.setSpecId(productSpecPo.getSpecId());
        tmpProductSpecDetailPo.setStoreId(productSpecPo.getStoreId());
        flag = productSpecDetailInnerServiceSMOImpl.deleteProductSpecDetail(tmpProductSpecDetailPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }


        for (ProductSpecDetailPo productSpecDetailPo : productSpecDetailPos) {
            productSpecDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
            productSpecDetailPo.setStoreId(productSpecPo.getStoreId());
            productSpecDetailPo.setSpecId(productSpecPo.getSpecId());
            flag = productSpecDetailInnerServiceSMOImpl.saveProductSpecDetail(productSpecDetailPo);
            if (flag < 1) {
                return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存规格失败");
            }
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
