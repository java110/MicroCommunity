package com.java110.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生成序列工具类
 * Created by wuxw on 2017/2/27.
 */
public class SequenceUtil {

    private static final long ONE_STEP = 1000000;
    private static final Lock LOCK = new ReentrantLock();
    private static short lastCount = 1;
    private static int count = 0;
    private static DateFormat dateFormatDay = new SimpleDateFormat("yyyyMMdd");
    private static DateFormat dateFormatMinute = new SimpleDateFormat("yyyyMMddhhmmss");
    private static final String first = "10";

    private static String PLATFORM_CODE = "0001";

    @SuppressWarnings("finally")
    public static String nextId(String idLength) {
        LOCK.lock();
        try {
            if (lastCount == ONE_STEP) {
                lastCount = 1;
            }
            count = lastCount++;
        } finally {
            LOCK.unlock();
            return String.format(idLength, count);
        }
    }

    public static String nextId(){
        return nextId("%06d");
    }

    /**
     * 获取交易流水ID
     *
     * @return
     */
    public static String getTransactionId() {

        //从内存中获取平台随机码

        return first + PLATFORM_CODE + dateFormatDay.format(new Date()) + nextId();
    }

    /**
     * 创建能力平台交互 流水
     * SVC90005
     *
     * 90001 20170314094355 10000018
     * @return
     */
    public static String getSVC90005TransactionId(){
        return "90001"+ dateFormatMinute.format(new Date()) +"99" + nextId();
    }

    /**
     * 6004050001201703141105137879
     * dateFormatMinute
     * @return
     */
    public static String getInvokeSAOPTransactionId(){
        return "6004050001"+dateFormatMinute.format(new Date())+nextId("%04d");
    }
}
