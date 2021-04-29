package com.java110.vo.api.user;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiUserVo extends MorePageVo implements Serializable {
    List<ApiUserDataVo> users;

    public List<ApiUserDataVo> getUsers() {
        return users;
    }

    public void setUsers(List<ApiUserDataVo> users) {
        this.users = users;
    }
}
