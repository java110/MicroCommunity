package com.java110.vo.api;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * API 查询小区楼返回对象
 */
public class ApiFloorVo  extends MorePageVo implements Serializable {


    private List<ApiFloorDataVo> apiFloorDataVoList;


    public List<ApiFloorDataVo> getApiFloorDataVoList() {
        return apiFloorDataVoList;
    }

    public void setApiFloorDataVoList(List<ApiFloorDataVo> apiFloorDataVoList) {
        this.apiFloorDataVoList = apiFloorDataVoList;
    }
}
