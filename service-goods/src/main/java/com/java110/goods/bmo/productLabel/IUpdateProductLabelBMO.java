package com.java110.goods.bmo.productLabel;
import com.java110.po.productLabel.ProductLabelPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateProductLabelBMO {


    /**
     * 修改产品标签
     * add by wuxw
     * @param productLabelPo
     * @return
     */
    ResponseEntity<String> update(ProductLabelPo productLabelPo);


}
