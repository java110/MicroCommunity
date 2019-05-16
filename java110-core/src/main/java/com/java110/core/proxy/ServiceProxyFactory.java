package com.java110.core.proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;


/**
 * @ClassName ServiceProxyFactory
 * @Description TODO
 * @Author wuxw
 * @Date 2019/5/15 15:56
 * @Version 1.0
 * add by wuxw 2019/5/15
 **/
@Component
public class ServiceProxyFactory implements FactoryBean<ITestService> {

    @Override
    public ITestService getObject() throws Exception {
        Class<?> interfaceType = ITestService.class;
        InvocationHandler handler = new ServiceInvocationHandler(interfaceType);
        return (ITestService) Proxy.newProxyInstance(interfaceType.getClassLoader(),
                new Class[]{interfaceType}, handler);
    }

    @Override
    public Class<?> getObjectType() {
        return ITestService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
