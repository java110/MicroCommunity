package com.java110.vo.api.owner;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * API 查询小区楼返回对象
 */
public class ApiOwnerCarVo extends MorePageVo implements Serializable {


    private List<ApiOwnerCarDataVo> ownerCars;

    public List<ApiOwnerCarDataVo> getOwnerCars() {
        return ownerCars;
    }

    public void setOwnerCars(List<ApiOwnerCarDataVo> ownerCars) {
        this.ownerCars = ownerCars;
    }
}
