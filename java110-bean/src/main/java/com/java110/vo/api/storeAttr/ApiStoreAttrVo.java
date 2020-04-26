package com.java110.vo.api.storeAttr;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiStoreAttrVo extends MorePageVo implements Serializable {
    List<ApiStoreAttrDataVo> storeAttrs;


    public List<ApiStoreAttrDataVo> getStoreAttrs() {
        return storeAttrs;
    }

    public void setStoreAttrs(List<ApiStoreAttrDataVo> storeAttrs) {
        this.storeAttrs = storeAttrs;
    }
}
