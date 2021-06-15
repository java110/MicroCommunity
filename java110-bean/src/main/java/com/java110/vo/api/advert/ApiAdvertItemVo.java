package com.java110.vo.api.advert;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiAdvertItemVo extends MorePageVo implements Serializable {
    List<ApiAdvertItemDataVo> advertItems;

    public List<ApiAdvertItemDataVo> getAdvertItems() {
        return advertItems;
    }

    public void setAdvertItems(List<ApiAdvertItemDataVo> advertItems) {
        this.advertItems = advertItems;
    }
}
