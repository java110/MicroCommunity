package com.java110.job.controller;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.controller.BaseController;
import com.java110.job.Api.HcFtpToFileSystemConfigAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 首页控制类
 * 20190813
 * 师延俊
 */
@Controller
@RequestMapping(path = "/HcFtpToFileSystemConfigList")
public class JobController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
    HcFtpToFileSystemConfigAction ftpToFileSystemConfigAction;


    /**
     * 首页处理方法
     *
     * @return 页面名称
     */
    @RequestMapping(path = "/")
    public ModelAndView flow() {
        logger.debug("请求流程 {},{}", new StringBuffer("首页我进来了"), new Date());
        return new ModelAndView("HcFtpToFileSystemConfigList");
    }

    /**
     * 页面载入时加载
     * @return
     */
    @RequestMapping(path = "/queryFtpItems")
    @ResponseBody
    public String queryFtpItems(HttpServletRequest request) {
        logger.debug("请求流程 {},{}", new StringBuffer("queryFtpItems  我看到请求了"), new Date());
        JSONObject JSON = ftpToFileSystemConfigAction.queryFtpItems(request);
        return JSON.toJSONString();
    }

    /**
     *  点击保存按钮时
     * @return
     */
    @RequestMapping(path = "/addFtpItem")
    @ResponseBody
    public String addFtpItem(HttpServletRequest request) {
        logger.debug("请求流程 {},{}", new StringBuffer("addFtpItem  我看到请求了"), new Date());
        JSONObject JSON = ftpToFileSystemConfigAction.addFtpItem(request);
        return JSON.toJSONString();
    }

    /**
     * 获取任务属性
     * @return
     */
    @RequestMapping(path = "/questTaskTample")
    @ResponseBody
    public String questTaskTample(HttpServletRequest request) {
        logger.debug("请求流程 {},{}", new StringBuffer("questTaskTample  我看到请求了"), new Date());
        JSONObject JSON = ftpToFileSystemConfigAction.questTaskTample(request);
        return JSON.toJSONString();
    }

    /**
     * 根据TaskId 获取任务属性
     * @return
     */
    @RequestMapping(path = "/queryTaskAttrs")
    @ResponseBody
    public String queryTaskAttrs(HttpServletRequest request) {
        logger.debug("请求流程 {},{}", new StringBuffer("queryTaskAttrs  我看到请求了"), new Date());
        JSONObject JSON = ftpToFileSystemConfigAction.queryTaskAttrs(request);
        return JSON.toJSONString();
    }

}
