package com.java110.vo.api.serviceImpl;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiServiceImplVo extends MorePageVo implements Serializable {
    List<ApiServiceImplDataVo> serviceImpls;


    public List<ApiServiceImplDataVo> getServiceImpls() {
        return serviceImpls;
    }

    public void setServiceImpls(List<ApiServiceImplDataVo> serviceImpls) {
        this.serviceImpls = serviceImpls;
    }
}
