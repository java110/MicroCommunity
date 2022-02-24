package com.java110.order.dao.impl;

import com.java110.core.base.dao.BaseServiceDao;
import com.java110.order.dao.ICorderServiceDao;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.corder.ApiCorderDataVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * demo服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("corderServiceDaoImpl")
//@Transactional
public class CorderServiceDaoImpl extends BaseServiceDao implements ICorderServiceDao {

    private static Logger logger = LoggerFactory.getLogger(CorderServiceDaoImpl.class);



    @Override
    public List<ApiCorderDataVo> getCorderInfo(Map info) throws DAOException {
        logger.debug("查询demo信息 入参 info : {}",info);

        List<ApiCorderDataVo> businessDemoInfos = sqlSessionTemplate.selectList("corderServiceDaoImpl.getCorderInfo",info);

        return businessDemoInfos;
    }

    @Override
    public int queryCordersCount(Map info) {
        logger.debug("查询order数据 入参 info : {}",info);

        List<Map> businessDemoInfos = sqlSessionTemplate.selectList("corderServiceDaoImpl.queryCordersCount", info);
        if (businessDemoInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessDemoInfos.get(0).get("count").toString());
    }
}
