package com.java110.config.feign;

import com.java110.dto.system.SystemLogDto;
import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class FeignConfiguration {
    /**
     * @return
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new UserErrorDecoder();
    }


    @Bean
    Logger.Level feignLoggerLevel() {
        //这里记录所有，根据实际情况选择合适的日志level
        if (SystemLogDto.getLogSwatch()) {
            return Logger.Level.FULL;
        } else {
            return Logger.Level.NONE;
        }
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