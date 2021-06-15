package com.java110.vo.api.resourceStore;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiResourceStoreVo extends MorePageVo implements Serializable {
    List<ApiResourceStoreDataVo> resourceStores;


    public List<ApiResourceStoreDataVo> getResourceStores() {
        return resourceStores;
    }

    public void setResourceStores(List<ApiResourceStoreDataVo> resourceStores) {
        this.resourceStores = resourceStores;
    }
}
