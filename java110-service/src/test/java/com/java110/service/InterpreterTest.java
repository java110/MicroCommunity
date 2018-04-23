package com.java110.service;

import bsh.Interpreter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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
}
