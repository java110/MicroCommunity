package com.java110.core.trace;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.trace.TraceParamDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * trace log  api aop
 */
@Component
@Aspect
public class Java110TraceLogAop {

    @Pointcut("@annotation(com.java110.core.trace.Java110TraceLog) || execution(public * com.java110..*.*InnerServiceSMOImpl.*(..))")
    public void dataProcess() {
    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("dataProcess()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object out = null;
        TraceParamDto traceParamDto = new TraceParamDto();
        JSONObject paramIn = new JSONObject();
        JSONObject paramOut = new JSONObject();

        Object[] args = pjp.getArgs();
        for (int paramIndex = 0; paramIndex < args.length; paramIndex++) {
            if (args[paramIndex] instanceof HttpServletRequest) {
//                HttpServletRequest request = (HttpServletRequest) args[paramIndex];
//                paramIn.put("param" + paramIndex, request.getParameterMap());
                continue;
            }
            if (args[paramIndex] instanceof HttpServletResponse) {
                continue;
            }
            paramIn.put("param" + paramIndex, args[paramIndex]);
        }
        traceParamDto.setReqParam(paramIn.toJSONString());
        out = pjp.proceed();
        if (paramOut != null) {
            paramOut.put("param", out);
        } else {
            paramOut.put("param", new JSONObject());
        }
        traceParamDto.setResParam(paramOut.toJSONString());
        Java110TraceFactory.putParams(traceParamDto);
        return out;
    }
}
