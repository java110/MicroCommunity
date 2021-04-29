package com.java110;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

    public void testOgnl() throws Exception{

        //创建一个Ognl上下文对象
        OgnlContext context = new OgnlContext();

        Map user = new HashMap();
        user.put("id", "123213");
        user.put("name", "张三");

        context.putAll(user);

        Object node = Ognl.parseExpression("id != null and name != null");

        Object value = Ognl.getValue(node,context);

        System.out.printf("value : " + value);




    }
}
