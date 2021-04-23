package com.java110.vo.api;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * API 查询小区楼返回对象
 */
public class ApiFeeDetailVo extends MorePageVo implements Serializable {


    private List<ApiFeeDetailDataVo> feeDetails;


    public List<ApiFeeDetailDataVo> getFeeDetails() {
        return feeDetails;
    }

    public void setFeeDetails(List<ApiFeeDetailDataVo> feeDetails) {
        this.feeDetails = feeDetails;
    }
}
