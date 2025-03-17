package com.java110.vo.api.community;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiCommunityVo extends MorePageVo implements Serializable {
    List<ApiCommunityDataVo> communitys;

    private int code;

    private String msg;


    public List<ApiCommunityDataVo> getCommunitys() {
        return communitys;
    }

    public void setCommunitys(List<ApiCommunityDataVo> communitys) {
        this.communitys = communitys;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
