package com.java110.utils.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志管理引擎，所有需要打印日志的服务，需要继承日志引擎
 * Created by wuxw on 2016/12/27.
 */
public class LoggerEngine {

    protected final static Logger logger = LoggerFactory.getLogger(LoggerEngine.class);

    /**
     * debug 模式引擎
     * @param loggerMsg
     */
    public static void debug(String loggerMsg){
        if(logger.isDebugEnabled()){
            logger.debug(loggerMsg);
        }
    }

    public static void debug(String loggerMsg,Object loggerInfo){
        if(logger.isDebugEnabled()){
            logger.debug(loggerMsg,loggerInfo);
        }
    }

    /**
     * info 模式引擎
     * @param loggerMsg
     */
    public static void info(String loggerMsg){
        if(logger.isInfoEnabled()){
            logger.info(loggerMsg);
        }
    }


    /**
     * error 模式引擎
     * @param loggerMsg
     */
    public static void error(String loggerMsg){
        if(logger.isErrorEnabled()){
            logger.error(loggerMsg);
        }
    }

    /**
     * error 模式引擎
     * @param loggerMsg
     */
    public static void error(String loggerMsg,Exception e){
        if(logger.isErrorEnabled()){
            logger.error(loggerMsg,e);
        }
    }
}
