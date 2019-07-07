package com.java110.vo.api.service;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiServiceVo extends MorePageVo implements Serializable {
    List<ApiServiceDataVo> services;


    public List<ApiServiceDataVo> getServices() {
        return services;
    }

    public void setServices(List<ApiServiceDataVo> services) {
        this.services = services;
    }
}
