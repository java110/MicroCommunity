package com.java110.core.language;

import com.java110.dto.menu.MenuDto;
import com.java110.vo.ResultVo;

import java.util.List;

public interface Language {

    /**
     * 获取菜单
     * @param menus
     * @return
     */
    List<MenuDto> getMenuDto(List<MenuDto> menus);


    /**
     * 返回
     * @param resultVo
     * @return
     */
    ResultVo getResultVo(ResultVo resultVo);
}
