package com.java110.vo.api.applicationKey;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiApplicationKeyVo extends MorePageVo implements Serializable {
    List<ApiApplicationKeyDataVo> applicationKeys;


    public List<ApiApplicationKeyDataVo> getApplicationKeys() {
        return applicationKeys;
    }

    public void setApplicationKeys(List<ApiApplicationKeyDataVo> applicationKeys) {
        this.applicationKeys = applicationKeys;
    }
}
