package com.java110.config.properties.code;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by wuxw on 2017/7/25.
 */
@Component
@ConfigurationProperties(prefix = "java110")
@PropertySource("classpath:java110.properties")
public class Java110Properties {

    private String mappingPath;

    public String getMappingPath() {
        return mappingPath;
    }

    public void setMappingPath(String mappingPath) {
        this.mappingPath = mappingPath;
    }
}
