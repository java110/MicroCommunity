package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IRentingPoolAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 出租房屋属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("rentingPoolAttrServiceDaoImpl")
//@Transactional
public class RentingPoolAttrServiceDaoImpl extends BaseServiceDao implements IRentingPoolAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RentingPoolAttrServiceDaoImpl.class);





    /**
     * 保存出租房屋属性信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveRentingPoolAttrInfo(Map info) throws DAOException {
        logger.debug("保存出租房屋属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("rentingPoolAttrServiceDaoImpl.saveRentingPoolAttrInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存出租房屋属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询出租房屋属性信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getRentingPoolAttrInfo(Map info) throws DAOException {
        logger.debug("查询出租房屋属性信息 入参 info : {}",info);

        List<Map> businessRentingPoolAttrInfos = sqlSessionTemplate.selectList("rentingPoolAttrServiceDaoImpl.getRentingPoolAttrInfo",info);

        return businessRentingPoolAttrInfos;
    }


    /**
     * 修改出租房屋属性信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateRentingPoolAttrInfo(Map info) throws DAOException {
        logger.debug("修改出租房屋属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("rentingPoolAttrServiceDaoImpl.updateRentingPoolAttrInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改出租房屋属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询出租房屋属性数量
     * @param info 出租房屋属性信息
     * @return 出租房屋属性数量
     */
    @Override
    public int queryRentingPoolAttrsCount(Map info) {
        logger.debug("查询出租房屋属性数据 入参 info : {}",info);

        List<Map> businessRentingPoolAttrInfos = sqlSessionTemplate.selectList("rentingPoolAttrServiceDaoImpl.queryRentingPoolAttrsCount", info);
        if (businessRentingPoolAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRentingPoolAttrInfos.get(0).get("count").toString());
    }


}
