package com.java110.core.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wuxw on 2017/7/23.
 */
@Configuration
@EnableCaching
public class Java110RedisConfig extends CachingConfigurerSupport {

    public final static String REDIS_EXPIRE_TIME_KEY = "#key_expire_time";

    public final static String DEFAULT_EXPIRE_TIME_KEY = "#key_expire_time=600";
    public final static String GET_STORE_ENTER_COMMUNITYS_EXPIRE_TIME_KEY = "#key_expire_time=7200";
    public final static String GET_STORE_INFO_EXPIRE_TIME_KEY = "#key_expire_time=5400";

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // key的序列化类型
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer()); // value的序列化类型
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    //@Bean(name = "jedisPool")
    //@Autowired
    public JedisPool jedisPool(@Qualifier("jedis.pool.config") JedisPoolConfig config,
                               @Value("${jedis.pool.host}") String host,
                               @Value("${jedis.pool.port}") int port,
                               @Value("${jedis.pool.timeout}") int timeout,
                               @Value("${jedis.pool.password}") String password) {
        //没有配置改为默认值
        if (timeout == 0) {
            timeout = 2000;
        }

        if (StringUtils.isEmpty(password)) {
            return new JedisPool(config, host, port, timeout);
        } else {
            return new JedisPool(config, host, port, timeout, password);
        }
    }


    //    @Bean(name = "jedisCluster")
//    @Autowired
    public JedisCluster jedisCluster(@Qualifier("jedis.pool.config") JedisPoolConfig config,
                                     @Value("${jedis.pool.host}") String host,
                                     @Value("${jedis.pool.port}") int port,
                                     @Value("${jedis.pool.timeout}") int timeout,
                                     @Value("${jedis.pool.password}") String password) {
        //没有配置改为默认值
        if (timeout == 0) {
            timeout = 2000;
        }
        String[] hosts = host.split(",");
        Set<HostAndPort> nodes = new HashSet<>();
        String[] tmpHosts = null;
        for (String tmpHost : hosts) {
            tmpHosts = tmpHost.split(":");
            nodes.add(new HostAndPort(tmpHosts[0], Integer.parseInt(tmpHosts[1])));
        }
        JedisCluster cluster = new JedisCluster(nodes);
        return cluster;
    }

    //@Bean(name = "jedis.pool.config")
    public JedisPoolConfig jedisPoolConfig(@Value("${jedis.pool.config.maxTotal}") int maxTotal,
                                           @Value("${jedis.pool.config.maxIdle}") int maxIdle,
                                           @Value("${jedis.pool.config.maxWaitMillis}") int maxWaitMillis) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);


        return config;
    }

    @Bean
    public RedisSerializer<String> redisKeySerializer(){
        return new StringRedisSerializer();
    }


    @Override
    @Bean
    public CacheManager cacheManager() {

        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        //上面实现的缓存读写
        Java110RedisCacheWriter java110RedisCacheWriter
                = new Java110RedisCacheWriter(connectionFactory);

        CacheManager cm
                = new RedisCacheManager(java110RedisCacheWriter, redisCacheConfiguration());

        return cm;
    }

    /**
     * @return
     * @Cacheable和通过RedisTemplate之间去相互获取数据
     * @Cacheable注解存储redis缓存数据和通过RedisTemplate去获取存储的数据，由于@Cacheable默认的序列化编码存储到数据为byte类型，而RedisTemplate的默认编码为JdkSerializationRedisSerializer，所以要通过设置同一个序列化方式去解决问题； 通过继承CachingConfigurerSupport并且开启@EnableCaching去设置cache的配置通过RedisCacheConfiguration.defaultCacheConfig()去设置序列化的方式RedisTemplate<String, Object>设置RedisTemplate的序列化方式
     * 设置序列化方式Jackson2JsonRedisSerializer
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        configuration = configuration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new JdkSerializationRedisSerializer())).entryTtl(Duration.ofSeconds(30));
        return configuration;


    }


}
