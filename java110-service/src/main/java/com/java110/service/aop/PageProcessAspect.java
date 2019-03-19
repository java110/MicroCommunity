package com.java110.service.aop;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.CommonConstant;
import com.java110.core.factory.PageDataFactory;
import com.java110.common.util.Assert;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.common.util.StringUtil;
import com.java110.entity.service.PageData;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Map;

/**
 * 数据初始化
 * Created by wuxw on 2018/5/2.
 */
@Aspect
@Component
public class PageProcessAspect {

    private final static Logger logger = LoggerFactory.getLogger(PageProcessAspect.class);

    //@Pointcut("execution(public * com.java110..*.*Controller.*(..)) || execution(public * com.java110..*.*Rest.*(..))")

    @Pointcut(" execution(public * com.java110..*.*Rest.*(..))")
    public void dataProcess(){}

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

        PageData pd = null;
        if("POST".equals(request.getMethod())){
            InputStream in = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            //reader.
            StringBuffer sb = new StringBuffer();
            String str = "";
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            str = sb.toString();
            if(Assert.isPageJsonObject(str)){
                pd = PageDataFactory.newInstance().builder(str).setTransactionId(GenerateCodeFactory.getPageTransactionId());
            }
        }
        //对 get情况下的参数进行封装
        if(pd == null){
            pd = PageDataFactory.newInstance().setTransactionId(GenerateCodeFactory.getPageTransactionId());
            Map<String,String[]> params = request.getParameterMap();
            if(params != null  && !params.isEmpty()) {
                JSONObject paramObj = new JSONObject();
                for(String key : params.keySet()) {
                    if(params.get(key).length>0){
                        String value = "";
                        for(int paramIndex = 0 ; paramIndex < params.get(key).length;paramIndex++) {
                            value = params.get(key)[paramIndex] + ",";
                        }
                        value = value.endsWith(",")?value.substring(0,value.length()-1):value;
                        paramObj.put(key,value);
                    }
                    continue;
                }
                pd.setParam(paramObj);
            }
        }

        if(request.getAttribute("claims") != null && request.getAttribute("claims") instanceof Map){
            Map<String,String> userInfo = (Map<String,String>)request.getAttribute("claims");
            if(userInfo.containsKey(CommonConstant.LOGIN_USER_ID)){
                pd.setUserId(userInfo.get(CommonConstant.LOGIN_USER_ID));
            }
            pd.setUserInfo(userInfo);

        }
        request.setAttribute(CommonConstant.CONTEXT_PAGE_DATA,pd);

    }

    @AfterReturning(returning = "ret", pointcut = "dataProcess()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
    }

    //后置异常通知
    @AfterThrowing("dataProcess()")
    public void throwException(JoinPoint jp){
    }

    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
    @After("dataProcess()")
    public void after(JoinPoint jp) throws IOException {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attributes.getRequest();
        PageData pd =request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA) != null ?(PageData)request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA):null ;
        //保存日志处理
        if(pd == null){
            return ;
        }

        if(!StringUtil.isNullOrNone(pd.getToken())) {
            HttpServletResponse response = attributes.getResponse();
            Cookie cookie = new Cookie(CommonConstant.COOKIE_AUTH_TOKEN, pd.getToken());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            response.flushBuffer();
        }

    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("dataProcess()")
    public Object around(ProceedingJoinPoint pjp) {
        try {
            Object o =  pjp.proceed();
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
