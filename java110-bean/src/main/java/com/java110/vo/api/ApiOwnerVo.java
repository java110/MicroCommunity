package com.java110.vo.api;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * API 查询小区楼返回对象
 */
public class ApiOwnerVo extends MorePageVo implements Serializable {


    private List<ApiOwnerDataVo> owners;


    public List<ApiOwnerDataVo> getOwners() {
        return owners;
    }

    public void setOwners(List<ApiOwnerDataVo> owners) {
        this.owners = owners;
    }
}
