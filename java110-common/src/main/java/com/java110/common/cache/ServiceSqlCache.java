package com.java110.common.cache;

import com.java110.common.util.SerializeUtil;
import com.java110.entity.service.ServiceSql;

/**
 * 缓存
 * Created by wuxw on 2018/4/19.
 */
public class ServiceSqlCache extends BaseCache {

    /**
     * 查询 服务sql
     * @param serviceCode
     * @return
     */
    public static ServiceSql getServiceSql(String serviceCode){

        Object obj = SerializeUtil.unserialize(getJedis().get(serviceCode.getBytes()));
        if(obj instanceof ServiceSql){
            return (ServiceSql) obj;
        }

        return null;
    }

    /**
     * 存储对象
     * @param serviceSql
     */
    public static void setServiceSql(ServiceSql serviceSql){
        getJedis().set(serviceSql.getServiceCode().getBytes(),SerializeUtil.serialize(serviceSql));
    }
}
