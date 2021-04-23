package com.java110.user;

import com.java110.entity.user.BoCust;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by wuxw on 2017/4/11.
 */
public class BoCustCompareTest  extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public BoCustCompareTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    public void testBoCustCompare(){
        List<BoCust> boCusts = new ArrayList<BoCust>();
        BoCust boCust = null;
        for (int i = 0 ;i< 10;i++){
            boCust = new BoCust();
            boCust.setCustId(i+"");
            if(i < 4) {
                boCust.setState("ADD");
            }else if(i< 6 ){
                boCust.setState("DEL");
            }else{
                boCust.setState("ADD");
            }
            boCusts.add(boCust);
        }

        for(BoCust boCust1 : boCusts){
            System.out.println("bocust1" + boCust1.getCustId()+" || "+boCust1.getState());
        }

        Collections.sort(boCusts);

        for(BoCust boCust1 : boCusts){
            System.out.println("bocust1" + boCust1.getCustId()+" || "+boCust1.getState());
        }


    }
}