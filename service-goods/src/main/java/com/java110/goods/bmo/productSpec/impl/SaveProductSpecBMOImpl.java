package com.java110.goods.bmo.productSpec.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.goods.bmo.productSpec.ISaveProductSpecBMO;
import com.java110.intf.goods.IProductSpecInnerServiceSMO;
import com.java110.po.product.ProductSpecPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveProductSpecBMOImpl")
public class SaveProductSpecBMOImpl implements ISaveProductSpecBMO {

    @Autowired
    private IProductSpecInnerServiceSMO productSpecInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param productSpecPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ProductSpecPo productSpecPo) {

        productSpecPo.setSpecId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_specId));
        int flag = productSpecInnerServiceSMOImpl.saveProductSpec(productSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
