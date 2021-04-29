package com.java110.common.bmo.attrSpec;

import com.java110.dto.attrSpec.AttrSpecDto;
import org.springframework.http.ResponseEntity;

public interface IGetAttrSpecBMO {


    /**
     * 查询属性规格表
     * add by wuxw
     *
     * @param attrSpecDto
     * @return
     */
    ResponseEntity<String> get(AttrSpecDto attrSpecDto);


}
