/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.api.controller.component;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.language.Java110Lang;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
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
public class CallComponentController extends DefaultAbstractComponentSMO {

    private final static Logger logger = LoggerFactory.getLogger(CallComponentController.class);
    private static final String VERSION = "version";
    private static final String VERSION_2 = "2.0";
    @Autowired
    private RestTemplate restTemplate;


    /**
     * 前台调用api方法
     * add by wuxw 2020-03-16
     * /callComponent/activities.listActivitiess
     *
     * @return
     */

    @RequestMapping(path = "/callComponent/{api}")
    @Java110Lang
    public ResponseEntity<String> callApi(
            @PathVariable String api,
            //@RequestBody String info,
            HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        String componentCode = "api";
        String componentMethod = "callApi";
        long startTime = DateUtil.getCurrentDate().getTime();
        try {
            Assert.hasLength(api, "参数错误，未传入api编码");

            IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
            pd.setApiUrl(api);
            //权限校验
            hasPrivilege(restTemplate, pd, "/callComponent/" + api);

            Object componentInstance = ApplicationContextFactory.getBean(componentCode);

            Assert.notNull(componentInstance, "未找到组件对应的处理类，请确认 " + componentCode);

            Method cMethod = componentInstance.getClass().getDeclaredMethod(componentMethod, IPageData.class);

            Assert.notNull(cMethod, "未找到组件对应处理类的方法，请确认 " + componentCode + "方法：" + componentMethod);


            logger.debug("组件编码{}，组件方法{}，pd 为{}", componentCode, componentMethod, pd.toString());

            responseEntity = (ResponseEntity<String>) cMethod.invoke(componentInstance, pd);

        } catch (SMOException e) {
            /*MultiValueMap<String, String> headers = new HttpHeaders();
            headers.add("code", e.getResult().getCode());*/
            logger.error("调用api异常", e);
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("调用api异常", e);
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
            logger.debug("api调用返回信息为{}", responseEntity);
            logger.debug(componentCode + "::" + componentCode + "back耗时：" + (DateUtil.getCurrentDate().getTime() - startTime));

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity;
            }
            String version = request.getParameter(VERSION);
            //当 接口版本号为2.0时 返回错误处理
            if (!StringUtil.isEmpty(version) && VERSION_2.equals(version)) {
                return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, responseEntity.getBody());
            }
        }
        return responseEntity;
    }

    /**
     * 调用组件方法
     * add by wuxw 2020-03-16
     *
     * @return
     */

    @RequestMapping(path = "/callComponent/{componentCode}/{componentMethod}")
    @Java110Lang
    public ResponseEntity<String> callComponent(
            @PathVariable String componentCode,
            @PathVariable String componentMethod,
            //@RequestBody String info,
            HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        long startTime = DateUtil.getCurrentDate().getTime();
        logger.debug(componentCode + "::" + componentMethod + "调用back开始：" + (startTime));

        try {
            Assert.hasLength(componentCode, "参数错误，未传入组件编码");
            Assert.hasLength(componentMethod, "参数错误，未传入调用组件方法");

            IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
            //权限校验
            hasPrivilege(restTemplate, pd, "/callComponent/" + componentCode + "/" + componentMethod);

            Object componentInstance = ApplicationContextFactory.getBean(componentCode);

            Assert.notNull(componentInstance, "未找到组件对应的处理类，请确认 " + componentCode);

            Method cMethod = componentInstance.getClass().getDeclaredMethod(componentMethod, IPageData.class);

            Assert.notNull(cMethod, "未找到组件对应处理类的方法，请确认 " + componentCode + "方法：" + componentMethod);


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
            logger.debug(componentCode + "::" + componentMethod + "调用back耗时：" + (DateUtil.getCurrentDate().getTime() - startTime));
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity;
            }
            String version = request.getParameter(VERSION);
            //当 接口版本号为2.0时 返回错误处理
            if (!StringUtil.isEmpty(version) && VERSION_2.equals(version)) {
                return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, responseEntity.getBody());
            }
        }
        return responseEntity;
    }

    //组件上传文件处理/callComponent/upload/

    /**
     * 调用组件 文件上传
     * /callComponent/upload/assetImport/importData
     *
     * @return
     */

    @RequestMapping(path = "/callComponent/upload/{componentCode}/{componentMethod}")
    @Java110Lang
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

            pd = freshPageDate(request);
            //权限校验
            hasPrivilege(restTemplate, pd, "/callComponent/" + componentCode + "/" + componentMethod);

            Object componentInstance = ApplicationContextFactory.getBean(componentCode);

            Assert.notNull(componentInstance, "未找到组件对应的处理类，请确认 " + componentCode);

            Method cMethod = componentInstance.getClass().getDeclaredMethod(componentMethod, IPageData.class, MultipartFile.class);

            Assert.notNull(cMethod, "未找到组件对应处理类的方法，请确认 " + componentCode + "方法：" + componentMethod);


            logger.debug("组件编码{}，组件方法{}，pd 为{}", componentCode, componentMethod, pd.toString());

            responseEntity = (ResponseEntity<String>) cMethod.invoke(componentInstance, pd, uploadFile);

        } catch (SMOException e) {
            logger.error("组件运行异常", e);
            /*MultiValueMap<String, String> headers = new HttpHeaders();
            headers.add("code", e.getResult().getCode());*/
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("组件运行异常", e);
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
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity;
            }
            String version = request.getParameter(VERSION);
            //当 接口版本号为2.0时 返回错误处理
            if (!StringUtil.isEmpty(version) && VERSION_2.equals(version)) {
                return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, responseEntity.getBody());
            }
        }
        return responseEntity;
    }

    /**
     * 调用组件 文件上传
     * /callComponent/upload/assetImport/importData
     *
     * @return
     */

    @RequestMapping(path = "/callComponent/download/{componentCode}/{componentMethod}")
    @Java110Lang
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
            pd = freshPageDate(request);

            hasPrivilege(restTemplate, pd, "/callComponent/download/" + componentCode + "/" + componentMethod);


            Object componentInstance = ApplicationContextFactory.getBean(componentCode);

            Assert.notNull(componentInstance, "未找到组件对应的处理类，请确认 " + componentCode);

            Method cMethod = componentInstance.getClass().getDeclaredMethod(componentMethod, IPageData.class);

            Assert.notNull(cMethod, "未找到组件对应处理类的方法，请确认 " + componentCode + "方法：" + componentMethod);
            responseEntity = (ResponseEntity<Object>) cMethod.invoke(componentInstance, pd);

        } catch (SMOException e) {
            logger.error("组件运行异常", e);
            responseEntity = new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("组件运行异常", e);
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
        }
        return responseEntity;
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

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(),
                reqData, pd.getComponentCode(), pd.getComponentMethod(), "", pd.getSessionId(), pd.getAppId());
        return newPd;
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
