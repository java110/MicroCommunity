package com.java110.log.type;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by wuxw on 2017/4/14.
 */
public class TypeTest

    extends TestCase
    {
        /**
         * Create the test case
         *
         * @param testName name of the test case
         */
    public TypeTest( String testName )
        {
            super( testName );
        }

        /**
         * @return the suite of tests being tested
         */
    public static Test suite()
    {
        return new TestSuite( TypeTest.class );
    }

        /**
         *
         * @since 1.7
         * @throws Exception
         */
    public void testType() throws Exception{
       Object obj = Class.forName("com.java110.log.type.CustDispatchListener").newInstance();
        Type[] types = obj.getClass().getGenericInterfaces();
       System.out.println(((ParameterizedType)types[0]).getActualTypeArguments()[0].getTypeName());
    }
}
