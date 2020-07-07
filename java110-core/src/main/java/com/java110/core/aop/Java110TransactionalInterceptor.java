package com.java110.core.aop;

import com.java110.core.factory.Java110TransactionalFactory;
import com.java110.dto.order.OrderDto;
import com.java110.utils.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 写上层事务ID
 */
@Component
public class Java110TransactionalInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //说明已经开启事务
        if (!StringUtils.isEmpty(Java110TransactionalFactory.getOId())) {
            return true;
        }
        String oId = request.getHeader(OrderDto.O_ID);

        if (!StringUtil.isEmpty(oId)) {
            Java110TransactionalFactory.put(Java110TransactionalFactory.O_ID, oId);
        }

        return true;
    }
}
