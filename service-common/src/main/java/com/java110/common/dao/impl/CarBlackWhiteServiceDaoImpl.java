package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.ICarBlackWhiteServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 黑白名单服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("carBlackWhiteServiceDaoImpl")
//@Transactional
public class CarBlackWhiteServiceDaoImpl extends BaseServiceDao implements ICarBlackWhiteServiceDao {

    private static Logger logger = LoggerFactory.getLogger(CarBlackWhiteServiceDaoImpl.class);

    /**
     * 黑白名单信息封装
     *
     * @param businessCarBlackWhiteInfo 黑白名单信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessCarBlackWhiteInfo(Map businessCarBlackWhiteInfo) throws DAOException {
        businessCarBlackWhiteInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存黑白名单信息 入参 businessCarBlackWhiteInfo : {}", businessCarBlackWhiteInfo);
        int saveFlag = sqlSessionTemplate.insert("carBlackWhiteServiceDaoImpl.saveBusinessCarBlackWhiteInfo", businessCarBlackWhiteInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存黑白名单数据失败：" + JSONObject.toJSONString(businessCarBlackWhiteInfo));
        }
    }


    /**
     * 查询黑白名单信息
     *
     * @param info bId 信息
     * @return 黑白名单信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessCarBlackWhiteInfo(Map info) throws DAOException {

        logger.debug("查询黑白名单信息 入参 info : {}", info);

        List<Map> businessCarBlackWhiteInfos = sqlSessionTemplate.selectList("carBlackWhiteServiceDaoImpl.getBusinessCarBlackWhiteInfo", info);

        return businessCarBlackWhiteInfos;
    }


    /**
     * 保存黑白名单信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveCarBlackWhiteInfoInstance(Map info) throws DAOException {
        logger.debug("保存黑白名单信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("carBlackWhiteServiceDaoImpl.saveCarBlackWhiteInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存黑白名单信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询黑白名单信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getCarBlackWhiteInfo(Map info) throws DAOException {
        logger.debug("查询黑白名单信息 入参 info : {}", info);

        List<Map> businessCarBlackWhiteInfos = sqlSessionTemplate.selectList("carBlackWhiteServiceDaoImpl.getCarBlackWhiteInfo", info);

        return businessCarBlackWhiteInfos;
    }


    /**
     * 修改黑白名单信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateCarBlackWhiteInfoInstance(Map info) throws DAOException {
        logger.debug("修改黑白名单信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("carBlackWhiteServiceDaoImpl.updateCarBlackWhiteInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改黑白名单信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询黑白名单数量
     *
     * @param info 黑白名单信息
     * @return 黑白名单数量
     */
    @Override
    public int queryCarBlackWhitesCount(Map info) {
        logger.debug("查询黑白名单数据 入参 info : {}", info);

        List<Map> businessCarBlackWhiteInfos = sqlSessionTemplate.selectList("carBlackWhiteServiceDaoImpl.queryCarBlackWhitesCount", info);
        if (businessCarBlackWhiteInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessCarBlackWhiteInfos.get(0).get("count").toString());
    }


}
