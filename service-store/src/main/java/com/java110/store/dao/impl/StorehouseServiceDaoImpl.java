package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IStorehouseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 仓库服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("storehouseServiceDaoImpl")
//@Transactional
public class StorehouseServiceDaoImpl extends BaseServiceDao implements IStorehouseServiceDao {

    private static Logger logger = LoggerFactory.getLogger(StorehouseServiceDaoImpl.class);



    /**
     * 查询仓库信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getStorehouseInfo(Map info) throws DAOException {
        logger.debug("查询仓库信息 入参 info : {}",info);

        List<Map> businessStorehouseInfos = sqlSessionTemplate.selectList("storehouseServiceDaoImpl.getStorehouseInfo",info);

        return businessStorehouseInfos;
    }



     /**
     * 查询仓库数量
     * @param info 仓库信息
     * @return 仓库数量
     */
    @Override
    public int queryStorehousesCount(Map info) {
        logger.debug("查询仓库数据 入参 info : {}",info);

        List<Map> businessStorehouseInfos = sqlSessionTemplate.selectList("storehouseServiceDaoImpl.queryStorehousesCount", info);
        if (businessStorehouseInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessStorehouseInfos.get(0).get("count").toString());
    }


}
