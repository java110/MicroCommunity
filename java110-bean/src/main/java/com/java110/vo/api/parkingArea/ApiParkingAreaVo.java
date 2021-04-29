package com.java110.vo.api.parkingArea;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiParkingAreaVo extends MorePageVo implements Serializable {
    List<ApiParkingAreaDataVo> parkingAreas;


    public List<ApiParkingAreaDataVo> getParkingAreas() {
        return parkingAreas;
    }

    public void setParkingAreas(List<ApiParkingAreaDataVo> parkingAreas) {
        this.parkingAreas = parkingAreas;
    }
}
