package com.java110.order.dao;

/**
 * Created by wuxw on 2018/6/3.
 */
public interface ISnowflakeldWorker {

    /**
     * 根据前缀生成ID
     * @param prefix
     * @return
     */
    public String getIdByPrefix(String prefix,long workId);
}
