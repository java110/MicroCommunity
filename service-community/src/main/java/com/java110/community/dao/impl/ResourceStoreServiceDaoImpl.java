package com.java110.community.dao.impl;

import com.java110.community.dao.IResourceStoreServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 物品信息
 *
 * @author fqz
 * @date 2021-03-17 11:48
 */
@Service("resourceStoreServiceDaoImpl")
public class ResourceStoreServiceDaoImpl extends BaseServiceDao implements IResourceStoreServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ResourceStoreServiceDaoImpl.class);

    @Override
    public List<Map> getResourceStoresInfo(Map info) {
        logger.debug("查询资源物品信息 入参 info : {}", info);
        List<Map> businessRoomRenovationInfos = sqlSessionTemplate.selectList("resourceStoreServiceDaoImpl.getResourceStoresInfo", info);
        return businessRoomRenovationInfos;
    }

    @Override
    public int getResourceStoresCount(Map info) {
        logger.debug("查询资源物品数量 入参 info : {}", info);
        List<Map> businessRoomRenovationInfos = sqlSessionTemplate.selectList("resourceStoreServiceDaoImpl.getResourceStoresCount", info);
        if (businessRoomRenovationInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(businessRoomRenovationInfos.get(0).get("count").toString());
    }
}
