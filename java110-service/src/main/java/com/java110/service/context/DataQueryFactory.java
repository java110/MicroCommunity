package com.java110.service.context;

import com.java110.utils.cache.ServiceSqlCache;
import com.java110.entity.service.ServiceSql;

/**
 * 数据查询工厂类
 * Created by wuxw on 2018/4/19.
 */
public class DataQueryFactory {

    public static DataQuery newInstance(){
        return new DataQuery();
    }

    public static ServiceSql getServiceSql(DataQuery dataQuery){
        return ServiceSqlCache.getServiceSql(dataQuery.getServiceCode());
    }
}
