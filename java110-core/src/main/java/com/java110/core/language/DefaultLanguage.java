package com.java110.core.language;

import com.java110.dto.menu.MenuDto;
import com.java110.dto.menuCatalog.MenuCatalogDto;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DefaultLanguage implements Language {





    public List<MenuCatalogDto> getMenuCatalog(List<MenuCatalogDto> menuCatalogDtos) {

        return menuCatalogDtos;
    }


    @Override
    public List<Map> getMenuDto(List<Map> menuDtos) {

        return menuDtos;
    }

    @Override
    public ResultVo getResultVo(ResultVo resultVo) {


        return resultVo;
    }

    public String getLangMsg(String msg){


        return msg;
    }

}
