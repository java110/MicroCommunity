package com.java110.vo.api.feeConfig;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiFeeConfigVo extends MorePageVo implements Serializable {
    List<ApiFeeConfigDataVo> feeConfigs;


    public List<ApiFeeConfigDataVo> getFeeConfigs() {
        return feeConfigs;
    }

    public void setFeeConfigs(List<ApiFeeConfigDataVo> feeConfigs) {
        this.feeConfigs = feeConfigs;
    }
}
