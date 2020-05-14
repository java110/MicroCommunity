package com.java110.vo.api.returnPayFee;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiReturnPayFeeVo extends MorePageVo implements Serializable {
    List<ApiReturnPayFeeDataVo> returnPayFees;


    public List<ApiReturnPayFeeDataVo> getReturnPayFees() {
        return returnPayFees;
    }

    public void setReturnPayFees(List<ApiReturnPayFeeDataVo> returnPayFees) {
        this.returnPayFees = returnPayFees;
    }
}
