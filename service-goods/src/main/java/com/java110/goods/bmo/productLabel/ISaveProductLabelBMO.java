package com.java110.goods.bmo.productLabel;

import com.java110.po.productLabel.ProductLabelPo;
import org.springframework.http.ResponseEntity;

public interface ISaveProductLabelBMO {


    /**
     * 添加产品标签
     * add by wuxw
     *
     * @param productLabelPo
     * @return
     */
    ResponseEntity<String> save(ProductLabelPo productLabelPo);


}
