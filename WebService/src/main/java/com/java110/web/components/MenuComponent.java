package com.java110.web.components;

import com.alibaba.fastjson.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 菜单 组件 处理类
 * Created by wuxw on 2019/3/19.
 */
@Component("menu")
public class MenuComponent {

    /**
     * 测试版本号
     * @return
     */
    public ResponseEntity<String> getMenus(String msg){

        String menuData = "[{'id':1,'icon':'fa-desktop','name':'我的菜单','label':'HOT','childs':[" +
                "{'name':'子菜单','href':'http://www.baidu.com'}]}," +
                "{'id':2,'icon':'fa-flask','name':'我的菜单','childs':[],'href':'/doc'}," +
                "{'id':3,'icon':'fa-globe','name':'我的菜单','childs':[{'name':'子菜单','href':'http://www.baidu.com'}]}" +
                "]";

        JSONArray menus = JSONArray.parseArray(menuData);

        return new ResponseEntity<String>(menus.toJSONString(), HttpStatus.OK);
    }

}
