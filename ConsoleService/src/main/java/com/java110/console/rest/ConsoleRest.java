package com.java110.console.rest;

import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.SMOException;
import com.java110.common.factory.DataTransactionFactory;
import com.java110.console.smo.IConsoleServiceSMO;
import com.java110.core.base.controller.BaseController;
import com.java110.entity.service.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wuxw on 2018/5/8.
 */

@RestController
public class ConsoleRest extends BaseController {

    @Autowired
    private IConsoleServiceSMO consoleServiceSMOImpl;

    @RequestMapping(path = "/console/queryTemplateCol",method = RequestMethod.POST)
    public String queryTemplateCol(HttpServletRequest request){
        PageData pd = null;
        try {
            pd = this.getPageData(request);
            // 判断用户是否登录
            checkLogin(pd);
            consoleServiceSMOImpl.getTemplateCol(pd);
        }catch (IllegalArgumentException e){
            return DataTransactionFactory.pageResponseJson(pd, ResponseConstant.RESULT_PARAM_ERROR,e.getMessage(),null);
        }catch (SMOException e){
            return DataTransactionFactory.pageResponseJson(pd,e.getResult().getCode(),e.getMessage(),null);
        }catch (Exception e){
            logger.error("异常信息：",e);
            return DataTransactionFactory.pageResponseJson(pd,ResponseConstant.RESULT_CODE_ERROR,"请求参数出错 ",null);
        }

        return pd.getResJson().toJSONString();
    }


    /**
     * 获取模板数据
     * @param request
     * @return
     */
    @RequestMapping(path = "/console/queryTemplateData",method = RequestMethod.GET)
    public  String  queryTemplateData(HttpServletRequest request){
        PageData pd = null;
        try {
            pd = this.getPageData(request);
            // 判断用户是否登录
            checkLogin(pd);
            consoleServiceSMOImpl.getTemplateData(pd);
        }catch (IllegalArgumentException e){
            return DataTransactionFactory.pageResponseJson(pd, ResponseConstant.RESULT_PARAM_ERROR,e.getMessage(),null);
        }catch (SMOException e){
            return DataTransactionFactory.pageResponseJson(pd,e.getResult().getCode(),e.getMessage(),null);
        }catch (Exception e){
            logger.error("异常信息：",e);
            return DataTransactionFactory.pageResponseJson(pd,ResponseConstant.RESULT_CODE_ERROR,"请求参数出错 ",null);
        }

        return pd.getResJson().toJSONString();
    }


    @RequestMapping(path = "/console/flushCache",method = RequestMethod.POST)
    public String flushCache(HttpServletRequest request){
        PageData pd = null;
        try {
            pd = this.getPageData(request);
            // 判断用户是否登录
            checkLogin(pd);
            consoleServiceSMOImpl.flushCache(pd);
        }catch (IllegalArgumentException e){
            return DataTransactionFactory.pageResponseJson(pd, ResponseConstant.RESULT_PARAM_ERROR,e.getMessage(),null);
        }catch (SMOException e){
            return DataTransactionFactory.pageResponseJson(pd,e.getResult().getCode(),e.getMessage(),null);
        }catch (Exception e){
            logger.error("异常信息：",e);
            return DataTransactionFactory.pageResponseJson(pd,ResponseConstant.RESULT_CODE_ERROR,"请求参数出错 ",null);
        }

        return pd.getResJson().toJSONString();
    }

    /**
     * 编辑模板数据
     * @param request
     * @return
     */
    @RequestMapping(path = "/console/editTemplateData",method = RequestMethod.POST)
    public  String  editTemplateData(HttpServletRequest request){
        PageData pd = null;
        try {
            pd = this.getPageData(request);
            // 判断用户是否登录
            checkLogin(pd);
            consoleServiceSMOImpl.editTemplateData(pd);
        }catch (IllegalArgumentException e){
            return DataTransactionFactory.pageResponseJson(pd, ResponseConstant.RESULT_PARAM_ERROR,e.getMessage(),null);
        }catch (SMOException e){
            return DataTransactionFactory.pageResponseJson(pd,e.getResult().getCode(),e.getMessage(),null);
        }catch (Exception e){
            logger.error("异常信息：",e);
            return DataTransactionFactory.pageResponseJson(pd,ResponseConstant.RESULT_CODE_ERROR,"请求参数出错 ",null);
        }

        return pd.getResJson().toJSONString();
    }

}
