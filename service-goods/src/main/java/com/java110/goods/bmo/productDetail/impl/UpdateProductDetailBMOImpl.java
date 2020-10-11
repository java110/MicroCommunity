package com.java110.goods.bmo.productDetail.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.productDetail.IUpdateProductDetailBMO;
import com.java110.intf.IProductDetailInnerServiceSMO;
import com.java110.po.productDetail.ProductDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateProductDetailBMOImpl")
public class UpdateProductDetailBMOImpl implements IUpdateProductDetailBMO {

    @Autowired
    private IProductDetailInnerServiceSMO productDetailInnerServiceSMOImpl;

    /**
     * @param productDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ProductDetailPo productDetailPo) {

        int flag = productDetailInnerServiceSMOImpl.updateProductDetail(productDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
