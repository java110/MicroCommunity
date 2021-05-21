package com.java110.utils.util;

import org.apache.commons.beanutils.converters.AbstractConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 自定义 String 处理
 */
public class Java110StringConvert extends AbstractConverter {
    private static Logger logger = LoggerFactory.getLogger(Java110StringConvert.class);


    /**
     * Construct a <b>java.lang.String</b> <i>Converter</i> that throws
     * a <code>ConversionException</code> if an error occurs.
     */
    public Java110StringConvert() {
        super();
    }

    /**
     * Construct a <b>java.lang.String</b> <i>Converter</i> that returns
     * a default value if an error occurs.
     *
     * @param defaultValue The default value to be returned
     *                     if the value to be converted is missing or an error
     *                     occurs converting the value.
     */
    public Java110StringConvert(Object defaultValue) {
        super(defaultValue);
    }

    @Override
    protected String convertToString(Object value) throws Throwable {
        logger.debug("convertToString当前对象类型start" + value.getClass());

        if (value instanceof Date) {
            return DateUtil.getFormatTimeString((Date) value, DateUtil.DATE_FORMATE_STRING_A);
        }
        logger.debug("convertToString当前对象类型" + value.getClass());
        return super.convertToString(value);
    }

    @Override
    protected <T> T convertToType(Class<T> type, Object value) throws Throwable {
        //System.out.printf("12313");
        logger.debug("convertToType当前对象类型start" + value.getClass());

        if(type.getClass().equals(String.class)) {
	        if (value instanceof Date) {
	            return type.cast(DateUtil.getFormatTimeString(Date.class.cast(value), DateUtil.DATE_FORMATE_STRING_A));
	        }
	        logger.debug("convertToType当前对象类型" + value.getClass());
	        return type.cast(value.toString());
        }else {
        	return null;
        }
    }

    @Override
    protected Class<?> getDefaultType() {
        return String.class;
    }
}
