package com.java110.goods.bmo.product;

import com.alibaba.fastjson.JSONArray;
import com.java110.po.product.ProductPo;
import com.java110.po.productDetail.ProductDetailPo;
import com.java110.po.productSpecValue.ProductSpecValuePo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ISaveProductBMO {


    /**
     * 添加产品
     * add by wuxw
     * @param productPo
     * @return
     */
    ResponseEntity<String> save(ProductPo productPo, String coverPhoto, JSONArray carouselFigurePhoto,
                                List<ProductSpecValuePo> productSpecValuePos, ProductDetailPo productDetailPo);


}
