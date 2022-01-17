package com.java110.core.log;

import com.java110.dto.system.SystemLogDto;
import org.slf4j.Logger;
import org.slf4j.Marker;

public class LoggerFactory implements Logger {

    private final Logger logger;

    private final String logPrefix = "";

    public LoggerFactory(Logger logger) {
        this.logger = logger;
    }

    public static Logger getLogger(Class<?> clazz) {
        Logger logger = org.slf4j.LoggerFactory.getLogger(clazz);
        return new LoggerFactory(logger);
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return SystemLogDto.getLogSwatch();
    }


    @Override
    public boolean isDebugEnabled() {
        return SystemLogDto.getLogSwatch();
    }

    @Override
    public boolean isInfoEnabled() {
        return SystemLogDto.getLogSwatch();
    }

    @Override
    public boolean isErrorEnabled() {
        return SystemLogDto.getLogSwatch();
    }


    @Override
    public boolean isErrorEnabled(Marker marker) {
        return this.isDebugEnabled(marker);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return this.logger.isWarnEnabled(marker);
    }


    @Override
    public boolean isDebugEnabled(Marker marker) {
        return this.logger.isDebugEnabled(marker);
    }
    public void trace(String message) {
        if (this.isTraceEnabled()) {
            this.logger.trace(this.logPrefix + message);
        }

    }

    public void trace(String message, Object arg) {
        if (this.isTraceEnabled()) {
            this.logger.trace(this.logPrefix + message, arg);
        }

    }

    public void trace(String message, Object arg1, Object arg2) {
        if (this.isTraceEnabled()) {
            this.logger.trace(this.logPrefix + message, arg1, arg2);
        }

    }

    public void trace(String message, Object... args) {
        if (this.isTraceEnabled()) {
            this.logger.trace(this.logPrefix + message, args);
        }

    }

    public void trace(String msg, Throwable t) {
        if (this.isTraceEnabled()) {
            this.logger.trace(this.logPrefix + msg, t);
        }

    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return this.isTraceEnabled(marker);
    }

    public void trace(Marker marker, String msg) {
        if (this.isTraceEnabled()) {
            this.logger.trace(marker, this.logPrefix + msg);
        }

    }

    public void trace(Marker marker, String format, Object arg) {
        if (this.isTraceEnabled()) {
            this.logger.trace(marker, this.logPrefix + format, arg);
        }

    }

    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        if (this.isTraceEnabled()) {
            this.logger.trace(marker, this.logPrefix + format, arg1, arg2);
        }

    }

    public void trace(Marker marker, String format, Object... argArray) {
        if (this.isTraceEnabled()) {
            this.logger.trace(marker, this.logPrefix + format, argArray);
        }

    }

    public void trace(Marker marker, String msg, Throwable t) {
        if (this.isTraceEnabled()) {
            this.logger.trace(marker, this.logPrefix + msg, t);
        }

    }


    public void debug(String message) {
        if (this.isDebugEnabled()) {
            this.logger.debug(this.logPrefix + message);
        }

    }

    public void debug(String message, Object arg) {
        if (this.isDebugEnabled()) {
            this.logger.debug(this.logPrefix + message, arg);
        }

    }

    public void debug(String message, Object arg1, Object arg2) {
        if (this.isDebugEnabled()) {
            this.logger.debug(this.logPrefix + message, arg1, arg2);
        }

    }

    public void debug(String message, Object... args) {
        if (this.isDebugEnabled()) {
            this.logger.debug(this.logPrefix + message, args);
        }

    }

    public void debug(String msg, Throwable t) {
        if (this.isDebugEnabled()) {
            this.logger.debug(this.logPrefix + msg, t);
        }

    }


    public void debug(Marker marker, String msg) {
        if (this.isDebugEnabled()) {
            this.logger.debug(marker, this.logPrefix + msg);
        }

    }

    public void debug(Marker marker, String format, Object arg) {
        if (this.isDebugEnabled()) {
            this.logger.debug(marker, this.logPrefix + format, arg);
        }

    }

    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        if (this.isDebugEnabled()) {
            this.logger.debug(marker, this.logPrefix + format, arg1, arg2);
        }

    }

    public void debug(Marker marker, String format, Object... arguments) {
        if (this.isDebugEnabled()) {
            this.logger.debug(marker, this.logPrefix + format, arguments);
        }

    }

    public void debug(Marker marker, String msg, Throwable t) {
        if (this.isDebugEnabled()) {
            this.logger.debug(marker, this.logPrefix + msg, t);
        }

    }



    public void warn(String message) {
        if(this.isWarnEnabled()) {
            this.logger.warn(this.logPrefix + message);
        }
    }

    public void warn(String message, Object arg) {
        this.logger.warn(this.logPrefix + message, arg);
    }

    public void warn(String message, Object arg1, Object arg2) {
        this.logger.warn(this.logPrefix + message, arg1, arg2);
    }

    public void warn(String message, Object... args) {
        if(this.isWarnEnabled()) {
            this.logger.warn(this.logPrefix + message, args);
        }
    }

    public void warn(String msg, Throwable t) {
        this.logger.warn(this.logPrefix + msg, t);
    }



    public void warn(Marker marker, String msg) {
        this.logger.warn(marker, this.logPrefix + msg);
    }

    public void warn(Marker marker, String format, Object arg) {
        this.logger.warn(marker, this.logPrefix + format, arg);
    }

    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        this.logger.warn(marker, this.logPrefix + format, arg1, arg2);
    }

    public void warn(Marker marker, String format, Object... arguments) {
        this.logger.warn(marker, this.logPrefix + format, arguments);
    }

    public void warn(Marker marker, String msg, Throwable t) {
        this.logger.warn(marker, this.logPrefix + msg, t);
    }



    public void error(String message) {
        if (this.logger.isErrorEnabled()) {
            this.logger.error(this.logPrefix + message);
        }
    }

    public void error(String message, Object arg) {
        this.logger.error(this.logPrefix + message, arg);
    }

    public void error(String message, Object arg1, Object arg2) {
        this.logger.error(this.logPrefix + message, arg1, arg2);
    }

    public void error(String message, Object... args) {
        if (this.isErrorEnabled()) {
            this.logger.error(this.logPrefix + message, args);
        }
    }

    public void error(String msg, Throwable t) {
        this.logger.error(this.logPrefix + msg, t);
    }



    public void error(Marker marker, String msg) {
        this.logger.error(marker, this.logPrefix + msg);
    }

    public void error(Marker marker, String format, Object arg) {
        this.logger.error(marker, this.logPrefix + format, arg);
    }

    public void error(Marker marker, String format, Object arg1, Object arg2) {
        this.logger.error(marker, this.logPrefix + format, arg1, arg2);
    }

    public void error(Marker marker, String format, Object... arguments) {
        this.logger.error(marker, this.logPrefix + format, arguments);
    }

    public void error(Marker marker, String msg, Throwable t) {
        this.logger.error(marker, this.logPrefix + msg, t);
    }

    public void info(String message) {
        if (this.isInfoEnabled()) {
            this.logger.info(this.logPrefix + message);
        }
    }

    public void info(String message, Object arg) {
        this.logger.info(this.logPrefix + message, arg);
    }

    public void info(String message, Object arg1, Object arg2) {
        this.logger.info(this.logPrefix + message, arg1, arg2);
    }

    public void info(String message, Object... args) {
        if (this.isInfoEnabled()) {
            this.logger.info(this.logPrefix + message, args);
        }
    }

    public void info(String msg, Throwable t) {
        this.logger.info(this.logPrefix + msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return false;
    }

    public void info(Marker marker, String msg) {
        this.logger.info(marker, this.logPrefix + msg);
    }

    public void info(Marker marker, String format, Object arg) {
        this.logger.info(marker, this.logPrefix + format, arg);
    }

    public void info(Marker marker, String format, Object arg1, Object arg2) {
        this.logger.info(marker, this.logPrefix + format, arg1, arg2);
    }

    public void info(Marker marker, String format, Object... arguments) {
        this.logger.info(marker, this.logPrefix + format, arguments);
    }

    public void info(Marker marker, String msg, Throwable t) {
        this.logger.info(marker, this.logPrefix + msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.isWarnEnabled();
    }
}
