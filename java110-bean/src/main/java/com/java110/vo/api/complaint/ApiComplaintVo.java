package com.java110.vo.api.complaint;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiComplaintVo extends MorePageVo implements Serializable {
    List<ApiComplaintDataVo> complaints;


    public List<ApiComplaintDataVo> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<ApiComplaintDataVo> complaints) {
        this.complaints = complaints;
    }
}
