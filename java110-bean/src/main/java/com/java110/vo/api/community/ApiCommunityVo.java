package com.java110.vo.api.community;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiCommunityVo extends MorePageVo implements Serializable {
    List<ApiCommunityDataVo> communitys;


    public List<ApiCommunityDataVo> getCommunitys() {
        return communitys;
    }

    public void setCommunitys(List<ApiCommunityDataVo> communitys) {
        this.communitys = communitys;
    }
}
