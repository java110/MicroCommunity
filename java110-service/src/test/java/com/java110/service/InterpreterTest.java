package com.java110.service;

import bsh.Interpreter;
import com.alibaba.fastjson.JSONObject;
import com.java110.service.smo.impl.QueryServiceSMOImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.ibatis.ognl.OgnlException;
import org.dom4j.DocumentException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuxw on 2018/4/23.
 */
public class InterpreterTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public InterpreterTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( InterpreterTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testInterpreter() throws Exception
    {
        String javaCode = "public int add(int a, int b){" +
                "return a+b;" +
                "}" +
                "" +
                "public int execute(int a,int b){" +
                "return add(a,b);" +
                "}";
        Interpreter interpreter = new Interpreter();
        interpreter.eval(javaCode);
        String param = "9,4";
        System.out.println(interpreter.eval("execute("+param+")").toString());
    }


    public void testDealSqlIf() throws OgnlException, DocumentException {

        String oldSql = "select * from s_a a\n" +
                "                  where <if test=\"name != null and name != ''\">\n" +
                "                          a.name = #name#\n" +
                "                      </if>" +
                "                       and a.sex = #name#" +
                "                       <if test=\"id != null and id!= ''\"> and a.id = #id# </if>";

        QueryServiceSMOImpl queryServiceSMO = new QueryServiceSMOImpl();
        JSONObject params = new JSONObject();
        params.put("id","123213");
        params.put("name","123213");

        System.out.println((queryServiceSMO.dealSqlIf(oldSql, params)));
    }
}
