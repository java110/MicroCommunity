package com.java110.core.language;

import com.java110.dto.menu.MenuDto;
import com.java110.dto.menuCatalog.MenuCatalogDto;
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
