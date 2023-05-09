package com.java110.utils.cache;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface Jedis {



    String set(String key, String value);
    String set(byte[] key, byte[] value);

    String set(final String key, final String value, final String nxxx, final String expx,
                      final int time);

    String get(String key);

    byte[] get(byte[] key);

    Boolean exists(String key);
    Long expire(String key, int seconds);
    Long ttl(String key);
    Long incr(String key);
    Long hset(String key, String field, String value);
    String hget(String key, String field);
    Long hdel(String key, String... field);

     Long del(String key);

    Long del(byte[] key);


    void close() ;

    Set<String> keys(final String pattern);

     Object eval(String script, int keyCount, String... params);

    Object eval(String script, List<String> keys, List<String> args);

}
