package com.java110.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @ClassName ServiceInvocationHandler
 * @Description TODO
 * @Author wuxw
 * @Date 2019/5/15 15:54
 * @Version 1.0
 * add by wuxw 2019/5/15
 **/
public class ServiceInvocationHandler implements InvocationHandler {
    private Class<?> interfaceType;

    public ServiceInvocationHandler(Class<?> intefaceType) {
        this.interfaceType = interfaceType;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            System.out.println("if调用前");
            return method.invoke(this, args);
        }
        System.out.println("调用前，参数：{}" + args);
        Object result = Arrays.asList(args);
        System.out.println("调用后，结果：{}" + result);
        return result;
    }

}
