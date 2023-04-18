package com.java110.core.language;

import com.alibaba.fastjson.JSONArray;
import com.java110.dto.menu.MenuCatalogDto;
import com.java110.vo.ResultVo;

import java.util.List;
import java.util.Map;

public interface Language {

    List<MenuCatalogDto> getMenuCatalog(List<MenuCatalogDto> menuCatalogDtos);

    /**
     * 获取菜单
     * @param menuDtos
     * @return
     */
    List<Map> getMenuDto(List<Map> menuDtos);

    /**
     * 获取菜单
     * @param tmpPrivilegeArrays
     * @return
     */
    JSONArray getPrivilegeMenuDto(JSONArray tmpPrivilegeArrays);


    /**
     * 返回
     * @param resultVo
     * @return
     */
    ResultVo getResultVo(ResultVo resultVo);


    /**
     * 返回
     * @param msg
     * @return
     */
    String getLangMsg(String msg);
}
