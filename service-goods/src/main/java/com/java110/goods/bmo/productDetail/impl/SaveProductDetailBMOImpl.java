package com.java110.goods.bmo.productDetail.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.goods.bmo.productDetail.ISaveProductDetailBMO;
import com.java110.intf.goods.IProductDetailInnerServiceSMO;
import com.java110.po.productDetail.ProductDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveProductDetailBMOImpl")
public class SaveProductDetailBMOImpl implements ISaveProductDetailBMO {

    @Autowired
    private IProductDetailInnerServiceSMO productDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param productDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ProductDetailPo productDetailPo) {

        productDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = productDetailInnerServiceSMOImpl.saveProductDetail(productDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
