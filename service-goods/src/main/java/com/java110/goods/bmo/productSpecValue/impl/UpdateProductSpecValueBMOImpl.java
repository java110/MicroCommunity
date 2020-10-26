package com.java110.goods.bmo.productSpecValue.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.productSpecValue.IUpdateProductSpecValueBMO;
import com.java110.intf.goods.IProductSpecValueInnerServiceSMO;
import com.java110.po.productSpecValue.ProductSpecValuePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateProductSpecValueBMOImpl")
public class UpdateProductSpecValueBMOImpl implements IUpdateProductSpecValueBMO {

    @Autowired
    private IProductSpecValueInnerServiceSMO productSpecValueInnerServiceSMOImpl;

    /**
     * @param productSpecValuePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ProductSpecValuePo productSpecValuePo) {

        int flag = productSpecValueInnerServiceSMOImpl.updateProductSpecValue(productSpecValuePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
