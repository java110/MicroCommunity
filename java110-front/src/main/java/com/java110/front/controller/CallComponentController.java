package com.java110.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;


/**
 * 组件调用处理类
 */
@RestController
public class CallComponentController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(CallComponentController.class);

    /**
     * 调用组件方法
     *
     * @return
     */

    @RequestMapping(path = "/callComponent/{componentCode}/{componentMethod}")
    public ResponseEntity<String> callComponent(
            @PathVariable String componentCode,
            @PathVariable String componentMethod,
            //@RequestBody String info,
            HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        try {
            Assert.hasLength(componentCode, "参数错误，未传入组件编码");
            Assert.hasLength(componentMethod, "参数错误，未传入调用组件方法");

            Object componentInstance = ApplicationContextFactory.getBean(componentCode);

            Assert.notNull(componentInstance, "未找到组件对应的处理类，请确认 " + componentCode);

            Method cMethod = componentInstance.getClass().getDeclaredMethod(componentMethod, IPageData.class);

            Assert.notNull(cMethod, "未找到组件对应处理类的方法，请确认 " + componentCode + "方法：" + componentMethod);

            IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);

            logger.debug("组件编码{}，组件方法{}，pd 为{}", componentCode, componentMethod, pd.toString());

            responseEntity = (ResponseEntity<String>) cMethod.invoke(componentInstance, pd);

        } catch (SMOException e) {
            /*MultiValueMap<String, String> headers = new HttpHeaders();
            headers.add("code", e.getResult().getCode());*/
            logger.error("调用组件异常", e);
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("调用组件异常", e);
            String msg = "";
            if (e instanceof InvocationTargetException) {
                Throwable targetEx = ((InvocationTargetException) e).getTargetException();
                if (targetEx != null) {
                    msg = targetEx.getMessage();
                }
            } else {
                msg = e.getMessage();
            }
            responseEntity = new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("组件调用返回信息为{}", responseEntity);
            return responseEntity;
        }
    }

    //组件上传文件处理/callComponent/upload/

    /**
     * 调用组件 文件上传
     * /callComponent/upload/assetImport/importData
     *
     * @return
     */

    @RequestMapping(path = "/callComponent/upload/{componentCode}/{componentMethod}")
    public ResponseEntity<String> callComponentUploadFile(
            @PathVariable String componentCode,
            @PathVariable String componentMethod,
            @RequestParam("uploadFile") MultipartFile uploadFile,
            //@RequestBody String info,
            HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        Map formParam = null;
        IPageData pd = null;
        try {
            Assert.hasLength(componentCode, "参数错误，未传入组件编码");
            Assert.hasLength(componentMethod, "参数错误，未传入调用组件方法");

            Object componentInstance = ApplicationContextFactory.getBean(componentCode);

            Assert.notNull(componentInstance, "未找到组件对应的处理类，请确认 " + componentCode);

            Method cMethod = componentInstance.getClass().getDeclaredMethod(componentMethod, IPageData.class, MultipartFile.class);

            Assert.notNull(cMethod, "未找到组件对应处理类的方法，请确认 " + componentCode + "方法：" + componentMethod);
            pd = freshPageDate(request);

            logger.debug("组件编码{}，组件方法{}，pd 为{}", componentCode, componentMethod, pd.toString());

            responseEntity = (ResponseEntity<String>) cMethod.invoke(componentInstance, pd, uploadFile);

        } catch (SMOException e) {
            logger.error("组件运行异常",e);
            /*MultiValueMap<String, String> headers = new HttpHeaders();
            headers.add("code", e.getResult().getCode());*/
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("组件运行异常",e);
            String msg = "";
            if (e instanceof InvocationTargetException) {
                Throwable targetEx = ((InvocationTargetException) e).getTargetException();
                if (targetEx != null) {
                    msg = targetEx.getMessage();
                }
            } else {
                msg = e.getMessage();
            }
            responseEntity = new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("组件调用返回信息为{}", responseEntity);
            return responseEntity;
        }
    }

    /**
     * 调用组件 文件上传
     * /callComponent/upload/assetImport/importData
     *
     * @return
     */

    @RequestMapping(path = "/callComponent/download/{componentCode}/{componentMethod}")
    public ResponseEntity<Object> callComponentDownloadFile(
            @PathVariable String componentCode,
            @PathVariable String componentMethod,
            HttpServletRequest request,
            HttpServletResponse response) {
        ResponseEntity<Object> responseEntity = null;
        Map formParam = null;
        IPageData pd = null;
        try {
            Assert.hasLength(componentCode, "参数错误，未传入组件编码");
            Assert.hasLength(componentMethod, "参数错误，未传入调用组件方法");

            Object componentInstance = ApplicationContextFactory.getBean(componentCode);

            Assert.notNull(componentInstance, "未找到组件对应的处理类，请确认 " + componentCode);

            Method cMethod = componentInstance.getClass().getDeclaredMethod(componentMethod, IPageData.class);

            Assert.notNull(cMethod, "未找到组件对应处理类的方法，请确认 " + componentCode + "方法：" + componentMethod);
            pd = freshPageDate(request);
            responseEntity = (ResponseEntity<Object>) cMethod.invoke(componentInstance, pd);

        } catch (SMOException e) {
            logger.error("组件运行异常",e);
            responseEntity = new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("组件运行异常",e);
            String msg = "";
            if (e instanceof InvocationTargetException) {
                Throwable targetEx = ((InvocationTargetException) e).getTargetException();
                if (targetEx != null) {
                    msg = targetEx.getMessage();
                }
            } else {
                msg = e.getMessage();
            }
            responseEntity = new ResponseEntity<Object>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("组件调用返回信息为{}", responseEntity);
            return responseEntity;
        }
    }

    /**
     * 刷新 pd 对象
     *
     * @param request HttpServletRequest 对象
     * @return pd 对象
     */
    private IPageData freshPageDate(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
        String reqData = "";
        if (params != null && !params.isEmpty()) {
            JSONObject paramObj = new JSONObject();
            for (String key : params.keySet()) {
                if (params.get(key).length > 0) {
                    String value = "";
                    for (int paramIndex = 0; paramIndex < params.get(key).length; paramIndex++) {
                        value = params.get(key)[paramIndex] + ",";
                    }
                    value = value.endsWith(",") ? value.substring(0, value.length() - 1) : value;
                    paramObj.put(key, value);
                }
                continue;
            }
            reqData = paramObj.toJSONString();
        }

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getToken(),
                reqData, pd.getComponentCode(), pd.getComponentMethod(), "", pd.getSessionId(),"");
        return newPd;
    }


}
