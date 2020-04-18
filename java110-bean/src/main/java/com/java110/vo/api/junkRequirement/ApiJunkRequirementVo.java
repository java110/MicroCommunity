package com.java110.vo.api.junkRequirement;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiJunkRequirementVo extends MorePageVo implements Serializable {
    List<ApiJunkRequirementDataVo> junkRequirements;


    public List<ApiJunkRequirementDataVo> getJunkRequirements() {
        return junkRequirements;
    }

    public void setJunkRequirements(List<ApiJunkRequirementDataVo> junkRequirements) {
        this.junkRequirements = junkRequirements;
    }
}
