package com.java110.goods.bmo.productCategory.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.goods.bmo.productCategory.ISaveProductCategoryBMO;
import com.java110.intf.goods.IProductCategoryInnerServiceSMO;
import com.java110.po.productCategory.ProductCategoryPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveProductCategoryBMOImpl")
public class SaveProductCategoryBMOImpl implements ISaveProductCategoryBMO {

    @Autowired
    private IProductCategoryInnerServiceSMO productCategoryInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param productCategoryPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ProductCategoryPo productCategoryPo) {

        productCategoryPo.setCategoryId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_categoryId));
        int flag = productCategoryInnerServiceSMOImpl.saveProductCategory(productCategoryPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
