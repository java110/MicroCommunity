package com.java110.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by wuxw on 2017/7/25.
 */
@ConfigurationProperties(prefix = "java110.common",locations="classpath:config/common.properties")
public class CommonProperties {
}
