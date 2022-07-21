package com.java110.store;

import com.java110.entity.product.Product;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by wuxw on 2017/5/20.
 */
public class BeanTest extends TestCase {



    public BeanTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( BeanTest.class );
    }


    public void testBean(){

        System.out.println(Math.ceil(0.1));
    }
}
