package com.java110.console.controller;

import com.java110.common.exception.NoAuthorityException;
import com.java110.common.exception.SMOException;
import com.java110.console.smo.IConsoleServiceSMO;
import com.java110.core.base.controller.BaseController;
import com.java110.entity.service.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
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
    public String index(Model model, HttpServletRequest request) {
        String template = "index";
        try {
            //1.0 获取对象
            PageData pd = this.getPageData(request);
            // 判断用户是否登录
            checkLogin(pd);
            //2.0 查询菜单信息
            List<Map> menuItems = consoleServiceSMOImpl.getMenuItemsByManageId(pd.getUserId());
            List<Map> removeMenuItems = new ArrayList<Map>();
            for(Map menuItem : menuItems){
                if(!"-1".equals(menuItem.get("parentId")) && !"1".equals(menuItem.get("level"))){
                    Map parentMenuItem = this.getMenuItemFromList(menuItems,menuItem.get("parentId").toString());
                    if(parentMenuItem == null){
                        continue;
                    }
                    if(parentMenuItem.containsKey("subMenus")){
                        List<Map> subMenus = (List<Map>) parentMenuItem.get("subMenus");
                        subMenus.add(menuItem);
                    }else{
                        List<Map> subMenus = new ArrayList<Map>();
                        subMenus.add(menuItem);
                        parentMenuItem.put("subMenus",subMenus);
                    }

                    removeMenuItems.add(menuItem);
                }
            }

            removeMap(menuItems,removeMenuItems);
            model.addAttribute("menus",menuItems);
            //3.0 查询各个系统调用量
        }catch (NoAuthorityException e){
            //跳转到登录页面
            template = "redirect:/login";
        }catch (IllegalArgumentException e){
            template = "redirect:/system/error";
        }catch (SMOException e){
            template = "redirect:/system/error";
        }catch (Exception e){
            logger.error("系统异常：",e);
            template = "redirect:/system/error";
        }finally {
            return template;
        }
    }


    private Map getMenuItemFromList(List<Map> menuItems,String parentId){
        for(Map menuItem : menuItems){
            if(menuItem.get("mId").toString().equals(parentId)){
                return menuItem;
            }
        }
        return null;
    }

    /**
     * 删除map
     * @param menuItems
     * @param removeMenuItems
     */
    private void removeMap(List<Map> menuItems,List<Map> removeMenuItems){
        if(removeMenuItems == null  || removeMenuItems.size() == 0){
            return;
        }

        for(Map removeMenuItem : removeMenuItems){
            menuItems.remove(removeMenuItem);
        }
    }

    public IConsoleServiceSMO getConsoleServiceSMOImpl() {
        return consoleServiceSMOImpl;
    }

    public void setConsoleServiceSMOImpl(IConsoleServiceSMO consoleServiceSMOImpl) {
        this.consoleServiceSMOImpl = consoleServiceSMOImpl;
    }
}
