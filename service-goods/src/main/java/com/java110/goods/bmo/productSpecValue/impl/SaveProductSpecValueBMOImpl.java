package com.java110.goods.bmo.productSpecValue.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.goods.bmo.productSpecValue.ISaveProductSpecValueBMO;
import com.java110.intf.IProductSpecValueInnerServiceSMO;
import com.java110.po.productSpecValue.ProductSpecValuePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveProductSpecValueBMOImpl")
public class SaveProductSpecValueBMOImpl implements ISaveProductSpecValueBMO {

    @Autowired
    private IProductSpecValueInnerServiceSMO productSpecValueInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param productSpecValuePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ProductSpecValuePo productSpecValuePo) {

        productSpecValuePo.setValueId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_valueId));
        int flag = productSpecValueInnerServiceSMOImpl.saveProductSpecValue(productSpecValuePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
