package com.java110.job.controller;

import com.java110.core.base.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
     * @return 页面名称
     */
    @RequestMapping(path = "/FtpToFileSystemConfigList")
    public ModelAndView flow() {
        logger.debug("请求流程 {},{}", new StringBuffer("prvncFtpToFileSystem"), new Date());
        return new ModelAndView("prvncFtpToFileSystemConfigList");
    }


}
