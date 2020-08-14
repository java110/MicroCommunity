package com.java110.common.bmo.attrValue;

import com.java110.po.attrValue.AttrValuePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteAttrValueBMO {


    /**
     * 修改属性值
     * add by wuxw
     *
     * @param attrValuePo
     * @return
     */
    ResponseEntity<String> delete(AttrValuePo attrValuePo);


}
