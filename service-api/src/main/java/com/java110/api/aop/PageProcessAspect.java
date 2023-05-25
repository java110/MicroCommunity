package com.java110.api.aop;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.log.LoggerFactory;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.exception.FilterException;
import com.java110.utils.util.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据初始化
 * Created by wuxw on 2018/5/2.
 */
@Aspect
@Component
public class PageProcessAspect {

    private static Logger logger = LoggerFactory.getLogger(PageProcessAspect.class);

    @Pointcut("execution(public * com.java110..*.*Controller.*(..)) ")
    public void dataProcess() {
    }

    /**
     * 初始化数据
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Before("dataProcess()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        IPageData pd = null;
        String reqData = "";
        String userId = "";
        String userName = "";
        String appId = "";
        String sessionId = request.getSession().getId();
        appId = request.getHeader("APP_ID");
        if (StringUtil.isEmpty(appId)) {
            appId = request.getHeader("APP-ID");
        }
        logger.debug("请求头信息：" + request.getHeaderNames());
        if ("POST,PUT".contains(request.getMethod())) {
            InputStream in = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            //reader.
            StringBuffer sb = new StringBuffer();
            String str = "";
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            reqData = sb.toString();

        }
        //对 get情况下的参数进行封装
        else {
            Map<String, String[]> params = request.getParameterMap();
            if (params != null && !params.isEmpty()) {
                JSONObject paramObj = new JSONObject();
                for (String key : params.keySet()) {
                    if (params.get(key).length > 0) {
                        String value = "";
                        for (int paramIndex = 0; paramIndex < params.get(key).length; paramIndex++) {
                            value += (params.get(key)[paramIndex] + ",");
                        }
                        value = value.endsWith(",") ? value.substring(0, value.length() - 1) : value;
                        paramObj.put(key, value);
                    }
                    continue;
                }
                reqData = paramObj.toJSONString();
            }
        }
        // 获取 userId
        if (request.getAttribute("claims") != null && request.getAttribute("claims") instanceof Map) {
            Map<String, String> userInfo = (Map<String, String>) request.getAttribute("claims");
            if (userInfo.containsKey(CommonConstant.LOGIN_USER_ID)) {
                userId = userInfo.get(CommonConstant.LOGIN_USER_ID);
                userName = userInfo.get(CommonConstant.LOGIN_USER_NAME);
            }
        }

        // 获取组件名称 和方法名称
        String url = request.getRequestURL() != null ? request.getRequestURL().toString() : "";
        String componentCode = "";
        String componentMethod = "";
        if (url.contains("callComponent")) { //组件处理
            String[] urls = url.split("/");
            if (urls.length == 6) {
                componentCode = urls[4];
                componentMethod = urls[5];
            } else {
                componentCode = "api";
                componentMethod = "callApi";
            }
        } else if (url.contains("flow")) { //流程处理
            String[] urls = url.split("/");

            if (urls.length == 5) {
                componentCode = urls[4];
            }
        }
        Map<String, Object> headers = new HashMap<>();
        Enumeration reqHeaderEnum = request.getHeaderNames();
        while (reqHeaderEnum.hasMoreElements()) {
            String headerName = (String) reqHeaderEnum.nextElement();
            headers.put(headerName.toLowerCase(), request.getHeader(headerName));
        }
        //pd = PageData.newInstance().builder(userId, userName, this.getToken(request), reqData, componentCode, componentMethod, url, sessionId, appId, headers);
        headers.put("_java110_token",this.getToken(request));
        pd = PageData.newInstance().builder(userId, userName, "", reqData, componentCode, componentMethod, url, sessionId, appId, headers);
        pd.setMethod(request.getMethod().equals("GET") ? HttpMethod.GET : HttpMethod.POST);

        logger.debug("切面 获取到的pd=" + JSONObject.toJSONString(pd));
        request.setAttribute(CommonConstant.CONTEXT_PAGE_DATA, pd);
        //调用链
        //Java110TraceFactory.createTrace(componentCode + "/" + componentMethod, headers);
    }


    @AfterReturning(returning = "ret", pointcut = "dataProcess()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
    }

    //后置异常通知
    @AfterThrowing("dataProcess()")
    public void throwException(JoinPoint jp) {
    }

    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
    @After("dataProcess()")
    public void after(JoinPoint jp) throws IOException {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //记录调用链
        //Java110TraceFactory.putAnnotations(TraceAnnotationsDto.VALUE_CLIENT_RECEIVE);

        PageData pd = request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA) != null ? (PageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA) : null;
        //保存日志处理
        if (pd == null) {
            return;
        }
        //写cookies信息
        writeCookieInfo(pd, attributes);

    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("dataProcess()")
    public Object around(ProceedingJoinPoint pjp) {
        try {
            Object o = pjp.proceed();
            return o;
        } catch (Throwable e) {
            logger.error("执行方法异常", e);
            return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * 获取TOKEN
     *
     * @param request
     * @return
     */
    private String getToken(HttpServletRequest request) throws FilterException {
        String token = "";
        if (request.getCookies() != null && request.getCookies().length > 0) {
            for (Cookie cookie : request.getCookies()) {
                if (CommonConstant.COOKIE_AUTH_TOKEN.equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }
        String authorization = request.getHeader("Authorization");

        if(StringUtil.isEmpty(token) && !StringUtil.isEmpty(authorization)){
            token = authorization.substring("Bearer ".length());
        }
        return token;
    }


    /**
     * 写cookie 信息
     *
     * @param pd         页面封装信息
     * @param attributes
     * @throws IOException
     */
    private void writeCookieInfo(IPageData pd, ServletRequestAttributes attributes) throws IOException {
        // 这里目前只写到组件级别，如果需要 写成方法级别 && "login".equals(pd.getComponentCode())
        //todo 未包含token 不做处理
        if (StringUtil.isNullOrNone(pd.getToken())) {
            return;
        }
        HttpServletResponse response = attributes.getResponse();

        //讲token写入到cookies 中
        Cookie cookie = new Cookie(CommonConstant.COOKIE_AUTH_TOKEN, pd.getToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
        //response.addHeader("Set-Cookie","SameSite=None");

        response.flushBuffer();


    }
}
