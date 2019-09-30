package com.java110.web.controller;

import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
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
 * 流程控制类
 */
@Controller
public class FlowController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(FlowController.class);

    /**
     * 流程处理方法
     *
     * @param flowCode 流程编码
     * @param request  请求对象
     * @return 页面名称
     */
    @RequestMapping(path = "/flow/{flowCode}")
    public String flow(@PathVariable String flowCode,
                       HttpServletRequest request) {
        logger.debug("请求流程 {},{}", flowCode, new Date());
        try {
            System.out.println("开始寻找组件数据");
            IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
            System.out.println("数据获取成功");
            validateFlowData(flowCode, pd);

        } catch (Throwable e) {
            flowCode = "error";
        }
        System.out.println("传入的路径为  "+flowCode);

        return flowCode;
    }

    /**
     * 流程数据校验方法
     *
     * @throws RuntimeException
     */
    private void validateFlowData(String flowCode, IPageData pd) throws RuntimeException {

        Assert.hasLength(flowCode, "参数错误，未传入流程编码");
    }
}
