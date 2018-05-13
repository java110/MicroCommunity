package com.java110.center.smo;

import com.java110.entity.service.DataQuery;

/**
 * Created by wuxw on 2018/4/18.
 */
public interface ICenterServiceCacheSMO {

    /**
     * 刷新 缓存
     */
    public void flush(DataQuery dataQuery);

    /**
     * 系统启动刷新
     */
    public void startFlush();
}
