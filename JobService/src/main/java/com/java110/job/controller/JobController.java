package com.java110.job.controller;

import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 首页登陆控制类
 * 20190813
 * 师延俊
 */
@Controller
public class JobController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);


    /**
     * 首页处理方法
     *
     * @param JobCode 编码
     * @param request  请求对象
     * @return 页面名称
     */
    @RequestMapping(path = "/Job/{JobCode}")
    public String flow(@PathVariable String JobCode,
                       HttpServletRequest request) {
        logger.debug("请求流程 {},{}", JobCode, new Date());
        /*try {
            IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
            validateFlowData(flowCode, pd);

        } catch (Throwable e) {
            flowCode = "error";
        }*/
        System.out.println(JobCode);

        return "prvncFtpToFileSystemConfigList";
    }


}
