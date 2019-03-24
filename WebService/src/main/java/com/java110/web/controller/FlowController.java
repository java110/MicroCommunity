package com.java110.web.controller;

import com.java110.common.constant.CommonConstant;
import com.java110.common.util.Assert;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 流程控制类
 */
@Controller
public class FlowController extends BaseController {

    /**
     * 流程处理方法
     * @param flowCode 流程编码
     * @param request 请求对象
     * @return
     */
    @RequestMapping(path="/flow/{flowCode}")
    public String flow(@PathVariable String flowCode,
                                       HttpServletRequest request){

        try {
            IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
            validateFlowData(flowCode,pd);

        }catch (Throwable e){
            flowCode = "error";
        }


        return flowCode;
    }

    /**
     *
     * 流程数据校验方法
     *
     * @throws RuntimeException
     */
    private void validateFlowData(String flowCode,IPageData pd) throws RuntimeException{

        Assert.hasLength(flowCode, "参数错误，未传入流程编码");
    }
}
