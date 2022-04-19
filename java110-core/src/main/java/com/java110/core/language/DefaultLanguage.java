package com.java110.core.language;

import com.java110.dto.menu.MenuDto;
import com.java110.dto.menuCatalog.MenuCatalogDto;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DefaultLanguage implements Language{

    protected static Map<String,String> menuCatalogs = new HashMap<>();
    protected static Map<String,String> menus = new HashMap<>();
    protected static Map<String,String> msgs = new HashMap<>();


    public List<MenuCatalogDto> getMenuCatalog(List<MenuCatalogDto> menuCatalogDtos){
        String menuCatalogsName = "";
        for(MenuCatalogDto menuCatalogDto :menuCatalogDtos){
            menuCatalogsName = menuCatalogs.get(menuCatalogDto.getName());
            if(!StringUtil.isEmpty(menuCatalogsName)){
                menuCatalogDto.setName(menuCatalogsName);
            }
        }
        return menuCatalogDtos;
    }


    @Override
    public List<MenuDto> getMenuDto(List<MenuDto> menus) {
        return null;
    }

    @Override
    public ResultVo getResultVo(ResultVo resultVo) {
        return null;
    }

}
