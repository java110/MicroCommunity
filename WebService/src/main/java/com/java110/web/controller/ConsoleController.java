package com.java110.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.exception.NoAuthorityException;
import com.java110.common.exception.SMOException;
import com.java110.common.util.Assert;
import com.java110.common.util.StringUtil;
import com.java110.web.smo.IConsoleServiceSMO;
import com.java110.core.base.controller.BaseController;
import com.java110.entity.service.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

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

            return template;
    }

    /**
     * 查询数据
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(path = "/console/list")
    public String listData(Model model, HttpServletRequest request){
        String template = "";
        try {

            //1.0 获取对象
            PageData pd = this.getPageData(request);

            Assert.hasLength(pd.getParam().getString("templateCode"),"请求参数templateCode 不能为空！");
            // 判断用户是否登录
            checkLogin(pd);
            //2.0 查询模板信息
            checkTemplate(pd,model);
            //3.0 查询菜单信息
            getMenus(model,pd,consoleServiceSMOImpl.getMenuItemsByManageId(pd.getUserId()));
            //3.0 查询各个系统调用量

            template = pd.getData().getJSONObject("template").containsKey("htmlName")
                    && !StringUtil.isNullOrNone( pd.getData().getJSONObject("template").getString("htmlName")) ?
                    pd.getData().getJSONObject("template").getString("htmlName"):
                    "list_template";

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
     * 模板校验并将模板写入到 model 对象中 前台页面展示用
     * @param pd
     * @param model
     */
    private void checkTemplate(PageData pd,Model model) throws IllegalArgumentException{
        try {
            consoleServiceSMOImpl.getTemplate(pd);
            JSONObject template = pd.getData().getJSONObject("template");
            model.addAttribute("templateCode",template.getString("templateCode"));
            model.addAttribute("templateName",template.getString("templateName"));
        }catch (Exception e){
            logger.error("查询异常",e);
            throw  new IllegalArgumentException("配置错误，没有当前模板【"+pd.getParam().getString("templateCode")+"】");
        }

    }



    public IConsoleServiceSMO getConsoleServiceSMOImpl() {
        return consoleServiceSMOImpl;
    }

    public void setConsoleServiceSMOImpl(IConsoleServiceSMO consoleServiceSMOImpl) {
        this.consoleServiceSMOImpl = consoleServiceSMOImpl;
    }
}
