package com.java110.core.aop;

import com.java110.core.factory.Java110TransactionalFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.order.OrderDto;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
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
        // 接收到请求，记录请求内容
        String curOId = Java110TransactionalFactory.getOId();
        if (StringUtil.isEmpty(curOId)) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            OrderDto orderDto = new OrderDto();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                logger.debug("请求头信息 key= " + key + ",value = " + value);

                key = key.toLowerCase();
                if (CommonConstant.APP_ID.equals(key) || CommonConstant.HTTP_APP_ID.equals(key)) {
                    orderDto.setAppId(value);
                }
                if (CommonConstant.TRANSACTION_ID.equals(key) || CommonConstant.HTTP_TRANSACTION_ID.equals(key)) {
                    orderDto.setExtTransactionId(value);
                }
                if (CommonConstant.REQUEST_TIME.equals(key) || CommonConstant.HTTP_REQ_TIME.equals(key)) {
                    orderDto.setRequestTime(value);
                }
                if (OrderDto.O_ID.equals(key)) {
                    orderDto.setoId(value);
                }
                if (CommonConstant.USER_ID.equals(key) || CommonConstant.HTTP_USER_ID.equals(key)) {
                    orderDto.setUserId(value);
                }
            }
            orderDto.setOrderTypeCd(OrderDto.ORDER_TYPE_DEAL);
            //全局事务ID申请
            Java110TransactionalFactory.getOrCreateOId(orderDto);
        }

        try {
            o = pjp.proceed();
            //观察者不做处理
            if (Java110TransactionalFactory.ROLE_OBSERVER.equals(Java110TransactionalFactory.getServiceRole())) {
                return o;
            }
            //完成事务
            if (StringUtil.isEmpty(curOId)) {
                Java110TransactionalFactory.finishOId();
            }
            return o;
        } catch (Throwable e) {
            logger.error("执行方法异常", e);
            //回退事务
            if (StringUtil.isEmpty(curOId)) {
                Java110TransactionalFactory.fallbackOId();
            }
            //return new BusinessDto(BusinessDto.CODE_ERROR, "内部异常" + e.getLocalizedMessage());
            throw e;
        } finally {
            //完成事务
            if (StringUtil.isEmpty(curOId)) {
                //清理事务信息
                Java110TransactionalFactory.clearOId();
            }
        }
    }
}
