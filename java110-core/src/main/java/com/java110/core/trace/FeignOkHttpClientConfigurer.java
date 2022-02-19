package com.java110.core.trace;

import feign.Feign;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignOkHttpClientConfigurer {


    private OkHttpClient okHttpClient;

    @Bean
    public Java110FeignClientInterceptor okHttpInterceptor() {
        return new Java110FeignClientInterceptor();
    }

    //注入okhttp
    @Bean
    public okhttp3.OkHttpClient okHttpClient(OkHttpClientFactory okHttpClientFactory,
                                             FeignHttpClientProperties httpClientProperties) {
        this.okHttpClient = okHttpClientFactory.createBuilder(httpClientProperties.isDisableSslValidation()).connectTimeout(httpClientProperties.getConnectionTimeout(),
                TimeUnit.SECONDS)
                .followRedirects(httpClientProperties.isFollowRedirects())
                .addInterceptor(okHttpInterceptor())
                .build();
        return this.okHttpClient;
    }
}
