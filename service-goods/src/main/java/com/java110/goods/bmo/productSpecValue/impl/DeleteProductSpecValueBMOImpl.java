package com.java110.goods.bmo.productSpecValue.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.productSpecValue.IDeleteProductSpecValueBMO;
import com.java110.intf.goods.IProductSpecValueInnerServiceSMO;
import com.java110.po.productSpecValue.ProductSpecValuePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteProductSpecValueBMOImpl")
public class DeleteProductSpecValueBMOImpl implements IDeleteProductSpecValueBMO {

    @Autowired
    private IProductSpecValueInnerServiceSMO productSpecValueInnerServiceSMOImpl;

    /**
     * @param productSpecValuePo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ProductSpecValuePo productSpecValuePo) {

        int flag = productSpecValueInnerServiceSMOImpl.deleteProductSpecValue(productSpecValuePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
