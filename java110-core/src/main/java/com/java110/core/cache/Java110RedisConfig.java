package com.java110.core.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching //开启缓存，默认是rendis缓存，继承CachingConfigurerSupport ，直接重写里面的方法
@ConditionalOnBean(Java110RedisCacheWriter.class)
public class Java110RedisConfig extends CachingConfigurerSupport {

    public final static String REDIS_EXPIRE_TIME_KEY = "#key_expire_time";

    public final static String DEFAULT_EXPIRE_TIME_KEY = "#key_expire_time=600";
    public final static String GET_STORE_ENTER_COMMUNITYS_EXPIRE_TIME_KEY = "#key_expire_time=7200";
    public final static String GET_STORE_INFO_EXPIRE_TIME_KEY = "#key_expire_time=5400";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Bean
    public CacheManager cacheManager() {

        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        System.out.printf("123123");
        //上面实现的缓存读写
        Java110RedisCacheWriter java110RedisCacheWriter
                = new Java110RedisCacheWriter(connectionFactory);

        CacheManager cm
                = new RedisCacheManager(java110RedisCacheWriter, redisCacheConfiguration());

        return cm;
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();

        configuration = configuration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())).entryTtl(Duration.ofSeconds(30));

        return configuration;
    }
}
