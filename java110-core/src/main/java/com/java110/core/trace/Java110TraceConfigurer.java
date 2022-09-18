package com.java110.core.trace;

import okhttp3.ConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.TimeUnit;

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

//    @Bean
//    @ConditionalOnBean(Java110FeignClientInterceptor.class)
//    public okhttp3.OkHttpClient okHttpClient(@Autowired
//                                                     Java110FeignClientInterceptor okHttpLoggingInterceptor){
//        okhttp3.OkHttpClient.Builder ClientBuilder = new okhttp3.OkHttpClient.Builder()
//                .readTimeout(30, TimeUnit.SECONDS) //读取超时
//                .connectTimeout(10, TimeUnit.SECONDS) //连接超时
//                .writeTimeout(60, TimeUnit.SECONDS) //写入超时
//                .connectionPool(new ConnectionPool(10 /*maxIdleConnections*/, 3, TimeUnit.MINUTES))
//                .addInterceptor(okHttpLoggingInterceptor);
//        return ClientBuilder.build();
//    }
}
