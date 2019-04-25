package com.java110.core.feign;

import feign.Feign;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 *
 */
@Configuration
public class FeignConfiguration {
    /**
     *
     * @return
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new UserErrorDecoder();
    }

    /**
     *
     * @return
     */
   /* @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }*/
}