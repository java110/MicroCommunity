package com.java110.core;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * 系统启动时加载信息
 * Created by wuxw on 2017/4/14.
 */
public class SystemStartUpInit implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * 默认 事件配置路径
     */
    private final String DEFAULT_EVENT_PATH = "classpath:/config/";


    /**
     * 默认 文件名称
     */
    private final String DEFAULT_FILE_NAME = "event.properties";


    /**
     * 加载配置文件，将侦听初始化
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {


    }


}
