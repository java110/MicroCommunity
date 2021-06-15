package com.java110.vo.api.fastuser;

import com.java110.vo.MorePageVo;
import com.java110.vo.api.activities.ApiActivitiesDataVo;

import java.io.Serializable;
import java.util.List;

public class ApiFastuserVo extends MorePageVo implements Serializable {

    List<ApiFastuserDataVo> fastuserDataVos;

    public List<ApiFastuserDataVo> getFastuserDataVos() {
        return fastuserDataVos;
    }

    public void setFastuserDataVos(List<ApiFastuserDataVo> fastuserDataVos) {
        this.fastuserDataVos = fastuserDataVos;
    }

}
