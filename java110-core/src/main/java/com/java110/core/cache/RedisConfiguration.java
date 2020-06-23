package com.java110.core.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by wuxw on 2017/7/23.
 */
@Configuration
public class RedisConfiguration extends CachingConfigurerSupport  {

    @Bean(name= "jedisPool")
    @Autowired
    public JedisPool jedisPool(@Qualifier("jedis.pool.config") JedisPoolConfig config,
                               @Value("${jedis.pool.host}")String host,
                               @Value("${jedis.pool.port}")int port,
                               @Value("${jedis.pool.timeout}")int timeout,
                               @Value("${jedis.pool.password}") String password) {
        //没有配置改为默认值
        if(timeout == 0){
            timeout = 2000;
        }

        if(StringUtils.isEmpty(password)) {
            return new JedisPool(config, host, port,timeout);
        }else{
            return new JedisPool(config,host,port,timeout,password);
        }
    }

    @Bean(name= "jedis.pool.config")
    public JedisPoolConfig jedisPoolConfig (@Value("${jedis.pool.config.maxTotal}")int maxTotal,
                                            @Value("${jedis.pool.config.maxIdle}")int maxIdle,
                                            @Value("${jedis.pool.config.maxWaitMillis}")int maxWaitMillis) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);

        return config;
    }




}
