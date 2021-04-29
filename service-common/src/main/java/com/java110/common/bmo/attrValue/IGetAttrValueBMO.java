package com.java110.common.bmo.attrValue;
import com.java110.dto.attrSpec.AttrValueDto;
import org.springframework.http.ResponseEntity;
public interface IGetAttrValueBMO {


    /**
     * 查询属性值
     * add by wuxw
     * @param  attrValueDto
     * @return
     */
    ResponseEntity<String> get(AttrValueDto attrValueDto);


}
