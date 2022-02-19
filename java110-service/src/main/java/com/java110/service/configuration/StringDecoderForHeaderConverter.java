package com.java110.service.configuration;


import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;

public class StringDecoderForHeaderConverter implements GenericConverter {
    private Logger logger = LoggerFactory.getLogger(StringDecoderForHeaderConverter.class);

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final String NO_NAME = "NO_NAME";

    private Charset charset;

    public StringDecoderForHeaderConverter(@Nullable Charset charset) {
        this.charset = charset;
        if (this.charset == null) {
            this.charset = DEFAULT_CHARSET;
        }
    }

    /**
     * +返回编码值
     * @return charset
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * +设置编码值
     * @param charset 编码值
     */
    public void setCharset(Charset charset) {
        this.charset = charset;

        if (this.charset == null) {
            this.charset = DEFAULT_CHARSET;
        }
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, String.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (ObjectUtils.isEmpty(source)) {
            return source;
        }

        String name = needDecoder(source, targetType);
        if (name != null) {
            return convert(source.toString(), name);
        }

        return source;
    }

    /**
     * +是否需要解码
     * @param source 待处理的值
     * @param targetType 类型
     * @return 非null:需要解码；null:无需解码
     */
    private String needDecoder(Object source, TypeDescriptor targetType) {
        RequestHeader requestHeader = targetType.getAnnotation(RequestHeader.class);
        Class<?> type = targetType.getType();
        if (requestHeader != null && type == String.class) {
            if (source.toString().indexOf("%") >= 0) {
                String name = requestHeader.name();
                if (name == null || name.equals("")) {
                    name = requestHeader.value();
                }
                if (name == null || name.equals("")) {
                    name = NO_NAME;
                }

                return name;
            }
        }

        return null;
    }

    /**
     * +结果解码
     * @param source 待解码的结果
     * @param name 参数名称
     * @return 解码后的结果
     */
    private String convert(final String source, final String name) {
        if (logger.isDebugEnabled()) {
            logger.debug("Begin convert[" + source + "] for RequestHeader[" + name + "].");
        }
        String _result = null;
        try {
            _result = URLDecoder.decode(source, this.charset.name());
            if (logger.isDebugEnabled()) {
                logger.debug("Success convert[" + source + ", " + _result + "] for RequestHeader[" + name + "].");
            }

            return _result;
        } catch(Exception e) {
            logger.warn("Fail convert[" + source + "] for RequestHeader[" + name + "]!", e);
        }

        return source;
    }
}