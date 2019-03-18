package com.java110.service.init;

import com.java110.common.factory.ApplicationContextFactory;
import org.springframework.context.ApplicationContext;

/**
 * Created by wuxw on 2018/5/7.
 */
public class ServiceStartInit {

    public static void initSystemConfig(ApplicationContext context){
        //加载配置文件，注册订单处理侦听
        try {
            ApplicationContextFactory.setApplicationContext(context);
        }
        catch (Exception ex) {
            throw new IllegalStateException("系统初始化失败", ex);
        }
    }



}
