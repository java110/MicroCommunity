package com.java110.front.listener;

import com.java110.config.properties.code.Java110Properties;
import com.java110.utils.factory.ApplicationContextFactory;

/**
 * 只用于开发时组件 自动加载，无需自动启动服务去加载组件
 * 可能会影响性能
 * add by wuxw 2019-11-21
 */
public class ReloadComponentFileListenerFactory {

    public static void startReloadComponentFileListener() {
        Java110Properties java110Properties = ApplicationContextFactory.getBean("java110Properties", Java110Properties.class);
        boolean autoReloadComponent = java110Properties.getAutoReloadComponent();
        //如果不是开发环境则不启用 自动加载组件功能
        if (!"dev".equals(ApplicationContextFactory.getActiveProfile())) {
            autoReloadComponent = false;
        }
        ReloadComponentFileListener reloadComponentFileListener = new ReloadComponentFileListener(autoReloadComponent);
        Thread componentThread = new Thread(reloadComponentFileListener, "thread-ReloadComponentFileListener");
        componentThread.start();
    }

}
