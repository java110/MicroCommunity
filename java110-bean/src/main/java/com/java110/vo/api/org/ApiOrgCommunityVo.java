package com.java110.vo.api.org;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiOrgCommunityVo extends MorePageVo implements Serializable {
    List<ApiOrgCommunityDataVo> orgCommunitys;


    public List<ApiOrgCommunityDataVo> getOrgCommunitys() {
        return orgCommunitys;
    }

    public void setOrgCommunitys(List<ApiOrgCommunityDataVo> orgCommunitys) {
        this.orgCommunitys = orgCommunitys;
    }
}
