package com.java110.goods.bmo.productDetail;
import com.java110.po.productDetail.ProductDetailPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateProductDetailBMO {


    /**
     * 修改产品属性
     * add by wuxw
     * @param productDetailPo
     * @return
     */
    ResponseEntity<String> update(ProductDetailPo productDetailPo);


}
