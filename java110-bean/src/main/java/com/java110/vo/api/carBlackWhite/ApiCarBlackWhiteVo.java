package com.java110.vo.api.carBlackWhite;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiCarBlackWhiteVo extends MorePageVo implements Serializable {
    List<ApiCarBlackWhiteDataVo> carBlackWhites;


    public List<ApiCarBlackWhiteDataVo> getCarBlackWhites() {
        return carBlackWhites;
    }

    public void setCarBlackWhites(List<ApiCarBlackWhiteDataVo> carBlackWhites) {
        this.carBlackWhites = carBlackWhites;
    }
}
