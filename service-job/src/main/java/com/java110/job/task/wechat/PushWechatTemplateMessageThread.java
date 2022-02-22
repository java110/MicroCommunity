package com.java110.job.task.wechat;

import com.java110.core.log.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

/**
 * 微信发送模板消息 线程
 */
public class PushWechatTemplateMessageThread implements Callable {

    private static Logger logger = LoggerFactory.getLogger(PushWechatTemplateMessageThread.class);


    private RestTemplate outRestTemplate;

    private String url;

    private String reqParam;

    private ResponseEntity<String> responseEntity;

    public PushWechatTemplateMessageThread() {
    }

    public PushWechatTemplateMessageThread(RestTemplate outRestTemplate, String url, String reqParam) {
        this.outRestTemplate = outRestTemplate;
        this.url = url;
        this.reqParam = reqParam;
    }

    @Override
    public Object call() throws Exception {
        logger.debug("开始发送消息 url:{},param:{}", this.url, this.reqParam);
        responseEntity = outRestTemplate.postForEntity(this.url, this.reqParam, String.class);
        logger.debug("发送完成 结果：{}", responseEntity);
        return null;
    }
}
