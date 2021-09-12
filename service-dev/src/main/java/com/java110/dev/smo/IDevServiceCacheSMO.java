package com.java110.dev.smo;

import com.java110.service.context.DataQuery;

import java.util.Map;

/**
 * Created by wuxw on 2018/4/18.
 */
public interface IDevServiceCacheSMO {

    /**
     * 刷新 缓存
     */
    public void flush(DataQuery dataQuery);

    /**
     * 根据缓存类别刷新缓存
     * @param headers 缓存类别
     */
    public void flush(Map<String,String> headers);

    /**
     * 系统启动刷新
     */
    public void startFlush();
}
