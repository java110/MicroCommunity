package com.java110.goods.bmo.productSpecValue;
import com.java110.po.productSpecValue.ProductSpecValuePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteProductSpecValueBMO {


    /**
     * 修改产品规格值
     * add by wuxw
     * @param productSpecValuePo
     * @return
     */
    ResponseEntity<String> delete(ProductSpecValuePo productSpecValuePo);


}
