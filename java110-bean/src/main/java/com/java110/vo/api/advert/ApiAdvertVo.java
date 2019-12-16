package com.java110.vo.api.advert;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiAdvertVo extends MorePageVo implements Serializable {
    List<ApiAdvertDataVo> adverts;


    public List<ApiAdvertDataVo> getAdverts() {
        return adverts;
    }

    public void setAdverts(List<ApiAdvertDataVo> adverts) {
        this.adverts = adverts;
    }
}
