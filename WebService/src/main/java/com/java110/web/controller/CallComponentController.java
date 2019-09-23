package com.java110.web.controller;

import com.java110.common.constant.CommonConstant;
import com.java110.common.exception.SMOException;
import com.java110.common.factory.ApplicationContextFactory;
import com.java110.common.util.Assert;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.web.smo.impl.LoginServiceSMOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


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
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
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
        try {
            Assert.hasLength(componentCode, "参数错误，未传入组件编码");
            Assert.hasLength(componentMethod, "参数错误，未传入调用组件方法");

            Object componentInstance = ApplicationContextFactory.getBean(componentCode);

            Assert.notNull(componentInstance, "未找到组件对应的处理类，请确认 " + componentCode);

            Method cMethod = componentInstance.getClass().getDeclaredMethod(componentMethod, IPageData.class, MultipartFile.class);

            Assert.notNull(cMethod, "未找到组件对应处理类的方法，请确认 " + componentCode + "方法：" + componentMethod);

            IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);

            logger.debug("组件编码{}，组件方法{}，pd 为{}", componentCode, componentMethod, pd.toString());

            responseEntity = (ResponseEntity<String>) cMethod.invoke(componentInstance, pd);

        } catch (SMOException e) {
            /*MultiValueMap<String, String> headers = new HttpHeaders();
            headers.add("code", e.getResult().getCode());*/
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
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


}
