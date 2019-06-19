package com.java110.common.util;

import org.apache.commons.beanutils.converters.AbstractConverter;

import java.util.Date;

/**
 * 自定义 String 处理
 */
public class Java110StringConvert extends AbstractConverter {


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
     * if the value to be converted is missing or an error
     * occurs converting the value.
     */
    public Java110StringConvert(Object defaultValue) {
        super(defaultValue);
    }

    @Override
    protected String convertToString(Object value) throws Throwable {
        if (value instanceof Date) {
            return DateUtil.getFormatTimeString((Date) value, DateUtil.DATE_FORMATE_STRING_A);
        }
        return super.convertToString(value);
    }

    @Override
    protected Object convertToType(Class type, Object value) throws Throwable {
        //System.out.printf("12313");
        if (value instanceof Date) {
            return DateUtil.getFormatTimeString((Date) value, DateUtil.DATE_FORMATE_STRING_A);
        }
        return value.toString();
    }

    @Override
    protected Class getDefaultType() {
        return String.class;
    }
}
