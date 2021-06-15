package com.java110.vo.api.menu;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiMenuVo extends MorePageVo implements Serializable {
    List<ApiMenuDataVo> menus;


    public List<ApiMenuDataVo> getMenus() {
        return menus;
    }

    public void setMenus(List<ApiMenuDataVo> menus) {
        this.menus = menus;
    }
}
