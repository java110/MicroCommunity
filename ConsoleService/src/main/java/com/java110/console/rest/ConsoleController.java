package com.java110.console.rest;

import com.java110.common.exception.NoAuthorityException;
import com.java110.common.exception.SMOException;
import com.java110.console.smo.IConsoleServiceSMO;
import com.java110.core.base.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制中心处理类
 * Created by wuxw on 2018/4/25.
 */
@Controller
public class ConsoleController extends BaseController {

    @Autowired
    private IConsoleServiceSMO consoleServiceSMOImpl;

    @RequestMapping(path = "/")
    public String index(Model model) {
        String template = "index";
        try {
            //1.0 判断用户是否登录
            String managerId = checkLogin();
            //2.0 查询菜单信息
            List<Map> menuItems = consoleServiceSMOImpl.getMenuItemsByManageId(managerId);
            for(Map menuItem : menuItems){
                if(!"-1".equals(menuItem.get("parentId")) && !"1".equals(menuItem.get("level"))){
                    Map parentMenuItem = this.getMenuItemFromList(menuItems,menuItem.get("parentId").toString());
                    if(parentMenuItem.containsKey("subMenus")){
                        List<Map> subMenus = (List<Map>) parentMenuItem.get("subMenus");
                        subMenus.add(menuItem);
                    }else{
                        List<Map> subMenus = new ArrayList<Map>();
                        subMenus.add(menuItem);
                        parentMenuItem.put("subMenus",subMenus);
                    }

                    menuItems.remove(menuItem);
                }
            }
            model.addAttribute("menus",menuItems);
            //3.0 查询各个系统调用量
        }catch (NoAuthorityException e){
            //跳转到登录页面
            template = "login";
        }catch (IllegalArgumentException e){
            template = "error";
        }catch (SMOException e){
            template = "error";
        }finally {
            return template;
        }
    }


    private Map getMenuItemFromList(List<Map> menuItems,String parentId){
        for(Map menuItem : menuItems){
            if(menuItem.get("mId").equals(parentId)){
                return menuItem;
            }
        }
        return null;
    }

    public IConsoleServiceSMO getConsoleServiceSMOImpl() {
        return consoleServiceSMOImpl;
    }

    public void setConsoleServiceSMOImpl(IConsoleServiceSMO consoleServiceSMOImpl) {
        this.consoleServiceSMOImpl = consoleServiceSMOImpl;
    }
}
