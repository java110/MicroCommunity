package com.java110.console.controller;

import com.java110.common.exception.NoAuthorityException;
import com.java110.common.exception.SMOException;
import com.java110.common.util.Assert;
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
            getMenus(model,pd,consoleServiceSMOImpl.getMenuItemsByManageId(pd.getUserId()));
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

    /**
     * 查询数据
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(path = "/console/list")
    public String listData(Model model, HttpServletRequest request){
        String template = "list_template";
        try {
            String templateCode = request.getParameter("templateCode");

            Assert.hasLength(templateCode,"请求参数templateCode 不能为空！");

            model.addAttribute("templateCode",templateCode);
            //1.0 获取对象
            PageData pd = this.getPageData(request);
            // 判断用户是否登录
            checkLogin(pd);
            //2.0 查询菜单信息
            getMenus(model,pd,consoleServiceSMOImpl.getMenuItemsByManageId(pd.getUserId()));
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



    public IConsoleServiceSMO getConsoleServiceSMOImpl() {
        return consoleServiceSMOImpl;
    }

    public void setConsoleServiceSMOImpl(IConsoleServiceSMO consoleServiceSMOImpl) {
        this.consoleServiceSMOImpl = consoleServiceSMOImpl;
    }
}
