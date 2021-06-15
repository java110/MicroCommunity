package com.java110.common.bmo.attrSpec;

import com.java110.po.attrSpec.AttrSpecPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateAttrSpecBMO {


    /**
     * 修改属性规格表
     * add by wuxw
     *
     * @param attrSpecPo
     * @return
     */
    ResponseEntity<String> update(AttrSpecPo attrSpecPo);


}
