package com.java110.goods.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wuxw on 2018/4/15.
 */
@Configuration
public class GoodsServiceBean {
    @Bean
    public GoodsServiceKafka listener() {
        return new GoodsServiceKafka();
    }

}
