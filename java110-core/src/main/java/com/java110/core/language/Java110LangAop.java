package com.java110.core.language;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.trace.Java110TraceFactory;
import com.java110.dto.trace.TraceParamDto;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * trace log  api aop
 */
@Component
@Aspect
public class Java110LangAop {



    @Pointcut("@annotation(com.java110.core.language.Java110Lang)")
    public void dataProcess() {
    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("dataProcess()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object out = null;
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String lang = request.getHeader(CommonConstant.JAVA110_LANG);

        out = pjp.proceed();

        if (StringUtil.isEmpty(lang) || "zh-cn".equals(lang)) {
            return out;
        }


        if (out instanceof ResponseEntity) {
            String body = ((ResponseEntity) out).getBody().toString();

            if (!StringUtil.isJsonObject(body)) {
                return out;
            }
            JSONObject outJson = JSONObject.parseObject(body);
            String msg = outJson.getString("msg");

            Language language = ApplicationContextFactory.getBean(lang, Language.class);

            if (language == null) {
                return out;
            }

            msg = language.getLangMsg(msg);

            outJson.put("msg", msg);

            return new ResponseEntity(outJson.toJSONString(), ((ResponseEntity) out).getStatusCode());
        }

        return out;
    }
}
