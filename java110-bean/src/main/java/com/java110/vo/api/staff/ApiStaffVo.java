package com.java110.vo.api.staff;

import com.java110.vo.MorePageVo;
import com.java110.vo.api.visit.ApiVisitDataVo;

import java.io.Serializable;
import java.util.List;

public class ApiStaffVo extends MorePageVo implements Serializable {
    List<ApiStaffDataVo> staffs;


    public List<ApiStaffDataVo> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<ApiStaffDataVo> staffs) {
        this.staffs = staffs;
    }
}
