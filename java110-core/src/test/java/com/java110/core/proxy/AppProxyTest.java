package com.java110.core.proxy;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.lang.reflect.Proxy;

/**
 * Unit test for simple App.
 */
public class AppProxyTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppProxyTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppProxyTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {

        CglibProxy proxy = new CglibProxy();
        //通过生成子类的方式创建代理类
        ITestService proxyImp = (ITestService)proxy.getProxy(ITestService.class);
        proxyImp.get("123213");
        proxyImp.set("123123");
    }
}
