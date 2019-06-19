package com.java110.vo.api;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * API 查询小区楼返回对象
 */
public class ApiParkingSpaceVo extends MorePageVo implements Serializable {


    private List<ApiParkingSpaceDataVo> parkingSpaces;


    public List<ApiParkingSpaceDataVo> getParkingSpaces() {
        return parkingSpaces;
    }

    public void setParkingSpaces(List<ApiParkingSpaceDataVo> parkingSpaces) {
        this.parkingSpaces = parkingSpaces;
    }
}
