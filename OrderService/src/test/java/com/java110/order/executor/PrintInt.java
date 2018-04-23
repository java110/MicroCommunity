package com.java110.order.executor;

import java.util.concurrent.Callable;

/**
 * Created by wuxw on 2017/4/25.
 */
public class PrintInt implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {

        System.out.println("1234567");

        //Thread.sleep(10000);
        throwException();

        return 1111;
    }

    private void throwException(){
        throw new IllegalArgumentException("异常了，报出来。。。");
    }
}
