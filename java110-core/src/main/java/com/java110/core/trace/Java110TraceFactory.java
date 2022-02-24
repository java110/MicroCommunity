package com.java110.core.trace;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.trace.*;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.EnvironmentConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.kafka.KafkaFactory;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Java110TransactionalFactory
 * @Description TODO
 * @Author wuxw
 * @Date 2020/7/3 22:30
 * @Version 1.0
 * add by wuxw 2020/7/3
 **/
public class Java110TraceFactory {

    private static Logger logger = LoggerFactory.getLogger(Java110TraceFactory.class);


    //SPAN-ID
    public static final String SPAN_ID = "SPAN-ID";

    //SPAN-ID
    public static final String PARENT_SPAN_ID = "parent-span-id";

    public static final String LOG_TRACE_TOPIC = "logTrace";

    private static ThreadLocal<Map<String, String>> spanId = new ThreadLocal<Map<String, String>>() {
        @Override
        protected Map<String, String> initialValue() {
            return new HashMap<String, String>();
        }
    };

    public static String putSpanId(String key, String value) {
        return spanId.get().put(key, value);
    }

    public static String getSpanId(String key) {
        return spanId.get().get(key);
    }

    public static String removeSpanId(String key) {
        return spanId.get().remove(key);
    }

    public static Map<String, String> entriesSpanId() {
        return spanId.get();
    }

    /**
     * 清理事务
     */
    public static void clearSpanId() {
        remove(SPAN_ID);
    }


    private static ThreadLocal<Map<String, TraceDto>> threadLocal = new ThreadLocal<Map<String, TraceDto>>() {
        @Override
        protected Map<String, TraceDto> initialValue() {
            return new HashMap<String, TraceDto>();
        }
    };

    public static TraceDto put(String key, TraceDto value) {
        return threadLocal.get().put(key, value);
    }

    public static TraceDto get(String key) {
        return threadLocal.get().get(key);
    }

    public static TraceDto remove(String key) {
        return threadLocal.get().remove(key);
    }

    public static Map<String, TraceDto> entries() {
        return threadLocal.get();
    }

    public static String createTrace(String name, Map<String, Object> headers) {
        String traceId = "";
        String parentId = "";
        if (headers.containsKey(CommonConstant.TRACE_ID)) { //先取trace Id
            traceId = headers.get(CommonConstant.TRACE_ID).toString();
        } else if (headers.containsKey(CommonConstant.TRANSACTION_ID)) {
            traceId = headers.get(CommonConstant.TRANSACTION_ID).toString();
        } else {
            traceId = GenerateCodeFactory.getUUID();
        }
        if (headers.containsKey(CommonConstant.PARENT_SPAN_ID)) {
            parentId = headers.get(CommonConstant.PARENT_SPAN_ID).toString();
        } else {
            parentId = "0";
        }
        return createTrace(name, traceId, parentId, TraceAnnotationsDto.VALUE_CLIENT_SEND);
    }


    public static String createTrace(String name, String traceId, String parentId, String event) {
        //初始事件
        Environment environment = (Environment) ApplicationContextFactory.getBean(Environment.class);
        //判断调用链是否打开
        if (!EnvironmentConstant.TRACE_SWITCH_ON.equals(environment.getProperty(EnvironmentConstant.TRACE_SWITCH))) {
            return "";
        }
        //全局事务开启者
        TraceDto traceDto = new TraceDto();
        traceDto.setId(GenerateCodeFactory.getUUID());
        traceDto.setName(name);
        traceDto.setParentSpanId(parentId);
        traceDto.setTimestamp(DateUtil.getCurrentDate().getTime());

        TraceAnnotationsDto traceAnnotationsDto = new TraceAnnotationsDto();
        TraceEndpointDto traceEndpointDto = new TraceEndpointDto();
        traceEndpointDto.setServiceName(environment.getProperty("spring.application.name"));
        traceEndpointDto.setPort(environment.getProperty("server.port"));
        InetAddress addr = null;
        String ip = "";
        try {
            addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch (UnknownHostException e) {
            ip = "127.0.0.1";
        }
        traceEndpointDto.setIpv4(ip);
        traceAnnotationsDto.setEndpoint(traceEndpointDto);
        traceAnnotationsDto.setTimestamp(DateUtil.getCurrentDate().getTime());
        traceAnnotationsDto.setValue(event);

        List<TraceAnnotationsDto> traceAnnotationsDtos = new ArrayList<>();
        traceAnnotationsDtos.add(traceAnnotationsDto);
        traceDto.setAnnotations(traceAnnotationsDtos);
        traceDto.setTraceId(traceId);

        traceDto.setDbs(new ArrayList<>());

        put(traceDto.getId(), traceDto);
        putSpanId(SPAN_ID, traceDto.getId());
        return traceDto.getId();
    }

    /**
     * 添加参数
     *
     * @param traceParamDto
     */
    public static void putParams(TraceParamDto traceParamDto) {
        TraceDto traceDto = getTraceDto();

        if (traceDto == null) {
            return;
        }

        //如果存在 则跳过
        if (traceDto.getParam() != null) {
            return;
        }

        traceDto.setParam(traceParamDto);
        put(getSpanId(SPAN_ID), traceDto);

    }

    /**
     * 添加动作时间
     *
     * @param event
     */
    public static void putAnnotations(String event) {
        TraceDto traceDto = getTraceDto();

        if (traceDto == null) {
            return;
        }

        //初始事件
        Environment environment = (Environment) ApplicationContextFactory.getBean(Environment.class);
        TraceAnnotationsDto traceAnnotationsDto = new TraceAnnotationsDto();
        TraceEndpointDto traceEndpointDto = new TraceEndpointDto();
        traceEndpointDto.setServiceName(environment.getProperty("spring.application.name"));
        traceEndpointDto.setPort(environment.getProperty("server.port"));
        InetAddress addr = null;
        String ip = "";
        try {
            addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch (UnknownHostException e) {
            ip = "127.0.0.1";
        }
        traceEndpointDto.setIpv4(ip);
        traceAnnotationsDto.setEndpoint(traceEndpointDto);
        traceAnnotationsDto.setTimestamp(DateUtil.getCurrentDate().getTime());
        traceAnnotationsDto.setValue(event);

        List<TraceAnnotationsDto> traceAnnotationsDtos = traceDto.getAnnotations();
        traceAnnotationsDtos.add(traceAnnotationsDto);
        traceDto.setAnnotations(traceAnnotationsDtos);
        put(getSpanId(SPAN_ID), traceDto);
        //判断是否为cr
        if (!TraceAnnotationsDto.VALUE_CLIENT_RECEIVE.equals(traceAnnotationsDto.getValue())) {
            return;
        }

        try {
            KafkaFactory.sendKafkaMessage(LOG_TRACE_TOPIC, JSONObject.toJSONString(traceDto));
        } catch (Exception e) {
            logger.error("发送log trace  异常", e);
        }
        clearTrace(getSpanId(SPAN_ID));
        clearSpanId();
    }

    /**
     * 添加db
     *
     * @param sql
     */
    public static void putDbs(String sql, String param, long duration) {
        TraceDto traceDto = getTraceDto();

        if (traceDto == null) {
            return;
        }
        List<TraceDbDto> dbs = traceDto.getDbs();
        TraceDbDto traceDbDto = new TraceDbDto();
        traceDbDto.setDbSql(sql);
        traceDbDto.setParam(param);
        traceDbDto.setDuration(duration+"");
        dbs.add(traceDbDto);
        traceDto.setDbs(dbs);
    }

    /**
     * 清理事务
     */
    public static void clearTrace(String spanId) {
        remove(spanId);
    }

    /**
     * 获取事务ID
     *
     * @return
     */
    public static TraceDto getTraceDto() {
        TraceDto traceDto = get(getSpanId(SPAN_ID));
        return traceDto;
    }

}
