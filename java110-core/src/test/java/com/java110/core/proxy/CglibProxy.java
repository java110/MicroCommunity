package com.java110.core.proxy;

import org.assertj.core.internal.cglib.proxy.Enhancer;
import org.assertj.core.internal.cglib.proxy.MethodInterceptor;
import org.assertj.core.internal.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @ClassName CglibProxy
 * @Description TODO
 * @Author wuxw
 * @Date 2019/5/15 16:50
 * @Version 1.0
 * add by wuxw 2019/5/15
 **/
public class CglibProxy implements MethodInterceptor {
    private Enhancer enhancer = new Enhancer();
    public Object getProxy(Class clazz){
        //设置需要创建子类的类
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        //通过字节码技术动态创建子类实例
        return enhancer.create();
    }
    //实现MethodInterceptor接口方法
    public Object intercept(Object obj, Method method, Object[] args,
                            MethodProxy proxy) throws Throwable {
        System.out.println("代理" + method.getName());
        //通过代理类调用父类中的方法
        return "123213";
    }
}