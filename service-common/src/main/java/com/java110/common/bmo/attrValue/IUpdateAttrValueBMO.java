package com.java110.common.bmo.attrValue;

import com.java110.po.attrValue.AttrValuePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateAttrValueBMO {


    /**
     * 修改属性值
     * add by wuxw
     *
     * @param attrValuePo
     * @return
     */
    ResponseEntity<String> update(AttrValuePo attrValuePo);


}
