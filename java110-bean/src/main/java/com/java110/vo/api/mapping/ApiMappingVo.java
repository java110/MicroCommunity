package com.java110.vo.api.mapping;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiMappingVo extends MorePageVo implements Serializable {
    List<ApiMappingDataVo> mappings;


    public List<ApiMappingDataVo> getMappings() {
        return mappings;
    }

    public void setMappings(List<ApiMappingDataVo> mappings) {
        this.mappings = mappings;
    }
}
