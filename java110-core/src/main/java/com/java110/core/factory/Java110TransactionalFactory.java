package com.java110.core.factory;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.Environment;
import com.java110.dto.order.OrderDto;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Java110TransactionalFactory
 * @Description TODO
 * @Author wuxw
 * @Date 2020/7/3 22:30
 * @Version 1.0
 * add by wuxw 2020/7/3
 **/
public class Java110TransactionalFactory {

    private static Logger logger = LoggerFactory.getLogger(Java110TransactionalFactory.class);

    //订单服务
    private static final String URL_API = ServiceConstant.SERVICE_ORDER_URL + "/order/oIdApi";

    private static final String BOOT_URL_API = "http://127.0.0.1:8008/order/oIdApi";

    /**
     * 创建事务ID
     */
    private static final String CREATE_O_ID = URL_API + "/createOId";


    /**
     * 创建事务ID
     */
    private static final String BOOT_CREATE_O_ID = BOOT_URL_API + "/createOId";


    /**
     * 回退事务ID
     */
    private static final String FALLBACK_O_ID = URL_API + "/fallBackOId";

    /**
     * 回退事务ID
     */
    private static final String BOOT_FALLBACK_O_ID = BOOT_URL_API + "/fallBackOId";

    /**
     * 回退事务ID
     */
    private static final String FINISH_O_ID = URL_API + "/finishOId";

    /**
     * 回退事务ID
     */
    private static final String BOOT_FINISH_O_ID = BOOT_URL_API + "/finishOId";


    //全局事务ID
    public static final String O_ID = "O-ID";

    //当前服务ID
    public static final String SERVICE_ID = "SERVICE-ID";

    //服务角色 创建事务者 还是 观察者
    public static final String SERVICE_ROLE = "SERVICE-ROLE";

    //创建者
    public static final String ROLE_CREATE = "creator";

    public static final String ROLE_OBSERVER = "observer";

    private static ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<Map<String, String>>() {
        @Override
        protected Map<String, String> initialValue() {
            return new HashMap<String, String>();
        }
    };

    public static String put(String key, String value) {
        return threadLocal.get().put(key, value);
    }

    public static String get(String key) {
        return threadLocal.get().get(key);
    }

    public static String remove(String key) {
        return threadLocal.get().remove(key);
    }

    public static Map<String, String> entries() {
        return threadLocal.get();
    }

    public static String getOrCreateOId(OrderDto orderDto) {

        //全局事务开启者
        if (StringUtils.isEmpty(orderDto.getoId())) {
            createOId(orderDto);
            put(SERVICE_ROLE, ROLE_CREATE);
        } else {
            put(SERVICE_ROLE, ROLE_OBSERVER);//观察者
        }
        //将事务ID 存放起来
        put(O_ID, orderDto.getoId());
        return orderDto.getoId();
    }

    /**
     * 清理事务
     */
    public static void clearOId() {
        //清理事务
        if(!StringUtil.isEmpty(getOId())) {
            remove(O_ID);
        }
        //清理角色
        if(!StringUtil.isEmpty(getServiceRole())) {
            remove(SERVICE_ROLE);
        }

    }

    /**
     * 获取事务ID
     *
     * @return
     */
    public static String getOId() {
        String oId = get(O_ID);
        return oId;
    }


    /**
     * 获取事务ID
     *
     * @return
     */
    public static String getServiceRole() {
        String oId = get(SERVICE_ROLE);
        return oId;
    }

    /**
     * 创建事务
     *
     * @param orderDto
     */
    private static void createOId(OrderDto orderDto) {
        if(Environment.isStartBootWay()){
            createOIdByBoot(orderDto);
        }else{
            createOIdByCloud(orderDto);
        }
    }

    /**
     * 创建事务
     *
     * @param orderDto
     */
    private static void createOIdByBoot(OrderDto orderDto) {
        RestTemplate restTemplate = ApplicationContextFactory.getBean("outRestTemplate", RestTemplate.class);
        HttpHeaders header = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(JSONObject.toJSONString(orderDto), header);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(BOOT_CREATE_O_ID, HttpMethod.POST, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("请求地址为,{} 请求订单服务信息，{},订单服务返回信息，{}", BOOT_CREATE_O_ID, httpEntity, responseEntity);
        }
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("创建事务失败" + responseEntity.getBody());
        }
        JSONObject order = JSONObject.parseObject(responseEntity.getBody());

        if (!order.containsKey("oId") || StringUtils.isEmpty(order.getString("oId"))) {
            throw new IllegalArgumentException("创建事务失败" + responseEntity.getBody());
        }
        orderDto.setoId(order.getString("oId"));
        //将事务ID 存放起来
        put(O_ID, orderDto.getoId());
    }

    /**
     * 创建事务
     *
     * @param orderDto
     */
    private static void createOIdByCloud(OrderDto orderDto) {
        RestTemplate restTemplate = ApplicationContextFactory.getBean("restTemplate", RestTemplate.class);
        HttpHeaders header = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(JSONObject.toJSONString(orderDto), header);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(CREATE_O_ID, HttpMethod.POST, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("请求地址为,{} 请求订单服务信息，{},订单服务返回信息，{}", CREATE_O_ID, httpEntity, responseEntity);
        }
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("创建事务失败" + responseEntity.getBody());
        }
        JSONObject order = JSONObject.parseObject(responseEntity.getBody());

        if (!order.containsKey("oId") || StringUtils.isEmpty(order.getString("oId"))) {
            throw new IllegalArgumentException("创建事务失败" + responseEntity.getBody());
        }
        orderDto.setoId(order.getString("oId"));
        //将事务ID 存放起来
        put(O_ID, orderDto.getoId());
    }

    /**
     * 处理失败，回退事务
     */
    public static void fallbackOId() {

        if(Environment.isStartBootWay()){
            fallbackOIdByBoot();
        }else{
            fallbackOIdByCloud();
        }

    }

    /**
     * 处理失败，回退事务
     */
    public static void fallbackOIdByBoot() {

        String oId = getOId();
        if (StringUtil.isEmpty(oId) || ROLE_OBSERVER.equals(getServiceRole())) {
            //当前没有开启事务无需回退
            logger.debug("当前没有开启事务无需回退");
            return;
        }
        OrderDto orderDto = new OrderDto();
        orderDto.setoId(oId);
        RestTemplate restTemplate = ApplicationContextFactory.getBean("outRestTemplate", RestTemplate.class);
        HttpHeaders header = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(JSONObject.toJSONString(orderDto), header);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(BOOT_FALLBACK_O_ID, HttpMethod.POST, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("回退事务 请求地址为,{} 请求订单服务信息，{},订单服务返回信息，{}", FALLBACK_O_ID, httpEntity, responseEntity);
        }

        if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            //当前没有开启事务无需回退
            logger.debug("当前没有开启事务无需回退");
            return;
        }

        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new IllegalArgumentException("回退事务失败" + responseEntity.getBody());
        }

    }

    /**
     * 处理失败，回退事务
     */
    public static void fallbackOIdByCloud() {

        String oId = getOId();
        if (StringUtil.isEmpty(oId) || ROLE_OBSERVER.equals(getServiceRole())) {
            //当前没有开启事务无需回退
            logger.debug("当前没有开启事务无需回退");
            return;
        }
        OrderDto orderDto = new OrderDto();
        orderDto.setoId(oId);
        RestTemplate restTemplate = ApplicationContextFactory.getBean("restTemplate", RestTemplate.class);
        HttpHeaders header = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(JSONObject.toJSONString(orderDto), header);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(FALLBACK_O_ID, HttpMethod.POST, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("回退事务 请求地址为,{} 请求订单服务信息，{},订单服务返回信息，{}", FALLBACK_O_ID, httpEntity, responseEntity);
        }

        if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            //当前没有开启事务无需回退
            logger.debug("当前没有开启事务无需回退");
            return;
        }

        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new IllegalArgumentException("回退事务失败" + responseEntity.getBody());
        }

    }

    /**
     * 处理失败，回退事务
     */
    public static void finishOId() {
        if(Environment.isStartBootWay()){
            finishOIdByBoot();
        }else{
            finishOIdByCloud();
        }

    }

    /**
     * 处理失败，回退事务
     */
    public static void finishOIdByBoot() {
        String oId = getOId();
        if (StringUtil.isEmpty(oId) || ROLE_OBSERVER.equals(getServiceRole())) {
            //当前没有开启事务无需回退
            logger.debug("当前没有开启事务无需回退");
            return;
        }
        OrderDto orderDto = new OrderDto();
        orderDto.setoId(oId);
        RestTemplate restTemplate = ApplicationContextFactory.getBean("outRestTemplate", RestTemplate.class);
        HttpHeaders header = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(JSONObject.toJSONString(orderDto), header);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(BOOT_FINISH_O_ID, HttpMethod.POST, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("完成事务 请求地址为,{} 请求订单服务信息，{},订单服务返回信息，{}", BOOT_FINISH_O_ID, httpEntity, responseEntity);
        }

        if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            //当前没有开启事务无需回退
            logger.debug("当前没有开启事务无需处理");
            return;
        }

        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new IllegalArgumentException("完成事务失败" + responseEntity.getBody());
        }

    }

    /**
     * 处理失败，回退事务
     */
    public static void finishOIdByCloud() {
        String oId = getOId();
        if (StringUtil.isEmpty(oId) || ROLE_OBSERVER.equals(getServiceRole())) {
            //当前没有开启事务无需回退
            logger.debug("当前没有开启事务无需回退");
            return;
        }
        OrderDto orderDto = new OrderDto();
        orderDto.setoId(oId);
        RestTemplate restTemplate = ApplicationContextFactory.getBean("restTemplate", RestTemplate.class);
        HttpHeaders header = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(JSONObject.toJSONString(orderDto), header);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(FINISH_O_ID, HttpMethod.POST, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("完成事务 请求地址为,{} 请求订单服务信息，{},订单服务返回信息，{}", FINISH_O_ID, httpEntity, responseEntity);
        }

        if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            //当前没有开启事务无需回退
            logger.debug("当前没有开启事务无需处理");
            return;
        }

        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new IllegalArgumentException("完成事务失败" + responseEntity.getBody());
        }

    }
}
