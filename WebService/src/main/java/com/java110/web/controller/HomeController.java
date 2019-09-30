package com.java110.web.controller;

import com.java110.utils.constant.CommonConstant;
import com.java110.core.context.IPageData;
import com.java110.web.smo.IFlowServiceSMO;
import com.java110.core.base.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class HomeController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(HomeController.class);


    @Autowired
    private IFlowServiceSMO flowServiceSMOImpl;

    @RequestMapping(path = "/")
    public String index(Model model, HttpServletRequest request) {
        String template = "index";

        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);

        if(!flowServiceSMOImpl.hasStoreInfos(pd)){
            //初始化 商户信息
            template = "init_company";
        }

        return template;
    }


    public IFlowServiceSMO getFlowServiceSMOImpl() {
        return flowServiceSMOImpl;
    }

    public void setFlowServiceSMOImpl(IFlowServiceSMO flowServiceSMOImpl) {
        this.flowServiceSMOImpl = flowServiceSMOImpl;
    }
}
