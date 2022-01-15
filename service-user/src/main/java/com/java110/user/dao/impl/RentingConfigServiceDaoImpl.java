package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IRentingConfigServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 房屋出租配置服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("rentingConfigServiceDaoImpl")
//@Transactional
public class RentingConfigServiceDaoImpl extends BaseServiceDao implements IRentingConfigServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RentingConfigServiceDaoImpl.class);





    /**
     * 保存房屋出租配置信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveRentingConfigInfo(Map info) throws DAOException {
        logger.debug("保存房屋出租配置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("rentingConfigServiceDaoImpl.saveRentingConfigInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存房屋出租配置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询房屋出租配置信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getRentingConfigInfo(Map info) throws DAOException {
        logger.debug("查询房屋出租配置信息 入参 info : {}",info);

        List<Map> businessRentingConfigInfos = sqlSessionTemplate.selectList("rentingConfigServiceDaoImpl.getRentingConfigInfo",info);

        return businessRentingConfigInfos;
    }


    /**
     * 修改房屋出租配置信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateRentingConfigInfo(Map info) throws DAOException {
        logger.debug("修改房屋出租配置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("rentingConfigServiceDaoImpl.updateRentingConfigInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改房屋出租配置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询房屋出租配置数量
     * @param info 房屋出租配置信息
     * @return 房屋出租配置数量
     */
    @Override
    public int queryRentingConfigsCount(Map info) {
        logger.debug("查询房屋出租配置数据 入参 info : {}",info);

        List<Map> businessRentingConfigInfos = sqlSessionTemplate.selectList("rentingConfigServiceDaoImpl.queryRentingConfigsCount", info);
        if (businessRentingConfigInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRentingConfigInfos.get(0).get("count").toString());
    }


}
