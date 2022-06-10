package com.java110.common.thread;

import com.alibaba.fastjson.JSONObject;
import com.java110.intf.common.IApplicationKeyInnerServiceSMO;
import com.java110.dto.machine.ApplicationKeyDto;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 从订单中同步业主信息至设备中间表
 * add by wuxw 2019-11-14
 */
public class UpdateApplicationKeyPwd implements Runnable {
    Logger logger = LoggerFactory.getLogger(UpdateApplicationKeyPwd.class);
    public static final long DEFAULT_WAIT_SECOND = 1000 * 60 * 24; // 默认30秒执行一次
    public static boolean TRANSLATE_STATE = false;

    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;


    private RestTemplate restTemplate;

    public UpdateApplicationKeyPwd(boolean state) {
        TRANSLATE_STATE = state;
        applicationKeyInnerServiceSMOImpl = ApplicationContextFactory.getBean("applicationKeyInnerServiceSMOImpl", IApplicationKeyInnerServiceSMO.class);
        restTemplate = ApplicationContextFactory.getBean("restTemplate", RestTemplate.class);

    }

    @Override
    public void run() {
        long waitTime = DEFAULT_WAIT_SECOND;
        while (TRANSLATE_STATE) {
            try {
                executeTask();
                /*waitTime = StringUtil.isNumber(MappingCache.getValue("DEFAULT_WAIT_SECOND")) ?
                        Long.parseLong(MappingCache.getValue("DEFAULT_WAIT_SECOND")) : DEFAULT_WAIT_SECOND;*/
                Thread.sleep(waitTime);
            } catch (Throwable e) {
                logger.error("执行订单中同步业主信息至设备中失败", e);
            }
        }
    }

    /**
     * 执行任务
     */
    private void executeTask() {

        //判断是不是每个月第一天
        if(!isFirstDayOfMonth(new Date())){
            return ;
        }

        ApplicationKeyDto applicationKeyDto = new ApplicationKeyDto();
        applicationKeyDto.setTypeFlag("1100102");
        applicationKeyDto.setEndTime(DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
        //查询订单信息
        List<ApplicationKeyDto> applicationKeyDtos = applicationKeyInnerServiceSMOImpl.queryApplicationKeys(applicationKeyDto);
        String url = "applicationKey.updateApplicationKey";
        for (ApplicationKeyDto tmpApplicationKeyDto : applicationKeyDtos) {
            try {
                logger.debug("开始处理订单" + JSONObject.toJSONString(tmpApplicationKeyDto));
                tmpApplicationKeyDto.setPwd(getRandom());//这里修改密码
                callService(JSONObject.toJSONString(tmpApplicationKeyDto), url, HttpMethod.POST);

            } catch (Exception e) {
                logger.error("执行订单任务失败", e);
            }
        }
    }


    private ResponseEntity<String> callService(String param, String url, HttpMethod httpMethod) {
        ResponseEntity<String> responseEntity = null;
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_APP_ID.toLowerCase(), CommonConstant.HC_HARDWARE_APP_ID);
        header.add(CommonConstant.HTTP_USER_ID.toLowerCase(), CommonConstant.ORDER_DEFAULT_USER_ID);
        header.add(CommonConstant.HTTP_TRANSACTION_ID.toLowerCase(), UUID.randomUUID().toString());
        header.add(CommonConstant.HTTP_REQ_TIME.toLowerCase(), DateUtil.getDefaultFormateTimeString(new Date()));
        header.add(CommonConstant.HTTP_SIGN.toLowerCase(), "");
        HttpEntity<String> httpEntity = new HttpEntity<String>(param, header);
        //logger.debug("请求中心服务信息，{}", httpEntity);
        try {
            responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>( e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("请求地址为,{} 请求中心服务信息，{},中心服务返回信息，{}", url, httpEntity, responseEntity);
        }
        
        return responseEntity;
    }

    /**
     * 获取随机数
     *
     * @return
     */
    private String getRandom() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += (random.nextInt(9) + 1);;
        }
        return result;
    }

    public boolean isFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        System.out.println(calendar.get(Calendar.MONTH));
        return calendar.get(Calendar.DAY_OF_MONTH) == 1;
    }

    public static void main(String[]args){
        String a="?";
        String value = "'${nextUserId}'";
        if(value.contains("${")){
            value = value.replace("${","\\${");
        }
        System.out.println(a.replaceFirst("\\?",value));
    }
}
