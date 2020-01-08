package com.java110.vo.api.store;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiStoreVo extends MorePageVo implements Serializable {
    List<ApiStoreDataVo> stores;


    public List<ApiStoreDataVo> getStores() {
        return stores;
    }

    public void setStores(List<ApiStoreDataVo> stores) {
        this.stores = stores;
    }
}
