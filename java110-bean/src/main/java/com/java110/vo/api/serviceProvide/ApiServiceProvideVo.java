package com.java110.vo.api.serviceProvide;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiServiceProvideVo extends MorePageVo implements Serializable {
    List<ApiServiceProvideDataVo> serviceProvides;


    public List<ApiServiceProvideDataVo> getServiceProvides() {
        return serviceProvides;
    }

    public void setServiceProvides(List<ApiServiceProvideDataVo> serviceProvides) {
        this.serviceProvides = serviceProvides;
    }
}
