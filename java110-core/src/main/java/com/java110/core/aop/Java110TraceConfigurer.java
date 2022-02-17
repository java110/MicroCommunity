package com.java110.core.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class Java110TraceConfigurer extends WebMvcConfigurerAdapter {
    @Autowired
    private Java110TraceHandlerInterceptor java110TraceHandlerInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(java110TraceHandlerInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
