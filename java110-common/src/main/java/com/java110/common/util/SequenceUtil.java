package com.java110.common.util;

import com.java110.common.cache.MappingCache;
import com.java110.common.constant.MappingConstant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    private static final String first = "10";

    /**
     *
     * 只有在不调用服务生成ID时有用
     */
    private static Map prefixMap = null;
    static {
        prefixMap = new HashMap();
        //10+yyyymmdd+八位序列
        prefixMap.put("oId","10");
        //（20+yyyymmdd+八位序列）
        prefixMap.put("bId","20");
        //（11+yyyymmdd+八位序列）
        prefixMap.put("attrId","11");
        prefixMap.put("transactionId","1000001");
    }

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

        return prefixMap.get("transactionId") + DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H) + nextId();
    }

    public static String getOId(){
        if(!MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_NEED_INVOKE_GENERATE_ID))){
            return prefixMap.get("oId") + DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H) + nextId("%08d");
        }
        //调用服务
        return null;
    }

    public static String getBId(){
        if(!MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_NEED_INVOKE_GENERATE_ID))){
            return prefixMap.get("bId") + DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H) + nextId("%08d");
        }
        //调用服务
        return null;
    }

    public static String getAttrId(){
        if(!MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_NEED_INVOKE_GENERATE_ID))){
            return prefixMap.get("attrId") + DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H) + nextId("%08d");
        }
        //调用服务
        return null;
    }


}
