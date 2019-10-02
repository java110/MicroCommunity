package com.java110.vo.api.ownerRepair;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiOwnerRepairVo extends MorePageVo implements Serializable {
    List<ApiOwnerRepairDataVo> ownerRepairs;


    public List<ApiOwnerRepairDataVo> getOwnerRepairs() {
        return ownerRepairs;
    }

    public void setOwnerRepairs(List<ApiOwnerRepairDataVo> ownerRepairs) {
        this.ownerRepairs = ownerRepairs;
    }
}
