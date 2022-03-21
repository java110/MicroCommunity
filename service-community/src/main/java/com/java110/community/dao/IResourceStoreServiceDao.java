package com.java110.community.dao;

import java.util.List;
import java.util.Map;

public interface IResourceStoreServiceDao {

    /**
     * 获取资源物品信息
     *
     * @param info
     * @return
     */
    List<Map> getResourceStoresInfo(Map info);

    /**
     * 获取资源物品数量
     *
     * @param info
     * @return
     */
    int getResourceStoresCount(Map info);

    /**
     * 查询资源总数
     *
     * @param info 资源信息
     * @return 资源数量
     */
    String queryResourceStoresTotalPrice(Map info);


}
