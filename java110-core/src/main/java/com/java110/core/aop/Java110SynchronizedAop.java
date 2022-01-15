package com.java110.core.aop;

import com.java110.core.annotation.Java110Synchronized;
import com.java110.utils.lock.DistributedLock;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName Java110TransactionalAop
 * @Description TODO
 * @Author wuxw
 * @Date 2020/7/3 22:13
 * @Version 1.0
 * add by wuxw 2020/7/3
 **/
@Component
@Aspect
public class Java110SynchronizedAop {

    private static Logger logger = LoggerFactory.getLogger(Java110SynchronizedAop.class);

    @Pointcut("@annotation(com.java110.core.annotation.Java110Synchronized)")
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

    }

    private String getKey(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();//此处joinPoint的实现类是MethodInvocationProceedingJoinPoint
        MethodSignature methodSignature = (MethodSignature) signature;//获取参数名
        Java110Synchronized java110Synchronized = methodSignature.getMethod().getAnnotation(Java110Synchronized.class);
        String value = java110Synchronized.value();
        Object[] args = joinPoint.getArgs();
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(methodSignature.getMethod());
        for (Object param : args) {
            for (String paramName : paramNames) {
                if (paramName.equals(value)) {
                    value = String.valueOf(param);
                }
            }
        }
        return value;
    }

    @AfterReturning(returning = "ret", pointcut = "dataProcess()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.debug("方法调用前执行doAfterReturning（）");
    }

    //后置异常通知
    @AfterThrowing("dataProcess()")
    public void throwException(JoinPoint jp) {
        logger.debug("方法调用异常执行throwException（）");


    }

    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
    @After("dataProcess()")
    public void after(JoinPoint jp) throws IOException {
        // 接收到请求，记录请求内容
        logger.debug("方法调用后执行after（）");
    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("dataProcess()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object o = null;
        String value = getKey(pjp);
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + value;
        try {
            //开启事务
            DistributedLock.waitGetDistributedLock(key, requestId);
            o = pjp.proceed();
            return o;
        } catch (Throwable e) {
            logger.error("执行方法异常", e);
            throw e;
        } finally {
            //清理事务信息
            DistributedLock.releaseDistributedLock(requestId, key);
        }
    }
}
