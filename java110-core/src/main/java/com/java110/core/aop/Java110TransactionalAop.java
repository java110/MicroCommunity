package com.java110.core.aop;

import com.java110.core.factory.Java110TransactionalFactory;
import com.java110.dto.order.OrderDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

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
public class Java110TransactionalAop {

    private static Logger logger = LoggerFactory.getLogger(Java110TransactionalAop.class);

    @Pointcut("@annotation(com.java110.core.annotation.Java110Transactional)")
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
        Enumeration<String> headerNames = request.getHeaderNames();
        OrderDto orderDto = new OrderDto();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            if (OrderDto.APP_ID.equals(key.toUpperCase())) {
                orderDto.setAppId(value);
            }
            if (OrderDto.TRANSACTION_ID.equals(key.toUpperCase())) {
                orderDto.setExtTransactionId(value);
            }
            if (OrderDto.REQUEST_TIME.equals(key.toUpperCase())) {
                orderDto.setRequestTime(value);
            }
            if (OrderDto.O_ID.equals(key.toUpperCase())) {
                orderDto.setoId(value);
            }
        }
        orderDto.setOrderTypeCd(OrderDto.ORDER_TYPE_DEAL);
        //全局事务ID申请
        Java110TransactionalFactory.getOrCreateOId(orderDto);
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

        //回退事务
        Java110TransactionalFactory.fallbackOId();
    }

    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
    @After("dataProcess()")
    public void after(JoinPoint jp) throws IOException {
        // 接收到请求，记录请求内容
        logger.debug("方法调用后执行after（）");


    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("dataProcess()")
    public Object around(ProceedingJoinPoint pjp) {
        try {
            Object o = pjp.proceed();
            return o;
        } catch (Throwable e) {
            logger.error("执行方法异常", e);
            return new ResponseEntity("内部异常" + e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
