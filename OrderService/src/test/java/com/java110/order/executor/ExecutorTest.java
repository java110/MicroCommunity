package com.java110.order.executor;

import com.java110.order.AppTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by wuxw on 2017/4/25.
 */
public class ExecutorTest  extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ExecutorTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ExecutorTest.class );
    }

    public void testThrowException(){
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        int i = 0;
        List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();
        while(i < 5) {
            Future<Integer> sum = executorService.submit(new PrintInt());
            i++;
            futureList.add(sum);
        }

        for(Future<Integer> sum : futureList) {
            try{

                    System.out.println(sum.get());

            }catch (Exception e){
                System.out.println("异常了");
                e.printStackTrace();
            }
        }
    }

    public void testThrowException1(){
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        int i = 0;
        while(i < 5) {
            Future<Integer> sum = executorService.submit(new PrintInt());
            i++;

            try{

                System.out.println(sum.get());

            }catch (Exception e){
                System.out.println("异常了");
                e.printStackTrace();
            }

        }

    }

}
