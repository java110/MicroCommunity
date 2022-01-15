package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IOwnerCarAttrServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 业主车辆属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("ownerCarAttrServiceDaoImpl")
//@Transactional
public class OwnerCarAttrServiceDaoImpl extends BaseServiceDao implements IOwnerCarAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OwnerCarAttrServiceDaoImpl.class);

    /**
     * 业主车辆属性信息封装
     *
     * @param businessOwnerCarAttrInfo 业主车辆属性信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessOwnerCarAttrInfo(Map businessOwnerCarAttrInfo) throws DAOException {
        businessOwnerCarAttrInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存业主车辆属性信息 入参 businessOwnerCarAttrInfo : {}", businessOwnerCarAttrInfo);
        int saveFlag = sqlSessionTemplate.insert("ownerCarAttrServiceDaoImpl.saveBusinessOwnerCarAttrInfo", businessOwnerCarAttrInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存业主车辆属性数据失败：" + JSONObject.toJSONString(businessOwnerCarAttrInfo));
        }
    }


    /**
     * 查询业主车辆属性信息
     *
     * @param info bId 信息
     * @return 业主车辆属性信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessOwnerCarAttrInfo(Map info) throws DAOException {

        logger.debug("查询业主车辆属性信息 入参 info : {}", info);

        List<Map> businessOwnerCarAttrInfos = sqlSessionTemplate.selectList("ownerCarAttrServiceDaoImpl.getBusinessOwnerCarAttrInfo", info);

        return businessOwnerCarAttrInfos;
    }


    /**
     * 保存业主车辆属性信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOwnerCarAttrInfoInstance(Map info) throws DAOException {
        logger.debug("保存业主车辆属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("ownerCarAttrServiceDaoImpl.saveOwnerCarAttrInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存业主车辆属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询业主车辆属性信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOwnerCarAttrInfo(Map info) throws DAOException {
        logger.debug("查询业主车辆属性信息 入参 info : {}", info);

        List<Map> businessOwnerCarAttrInfos = sqlSessionTemplate.selectList("ownerCarAttrServiceDaoImpl.getOwnerCarAttrInfo", info);

        return businessOwnerCarAttrInfos;
    }


    /**
     * 修改业主车辆属性信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateOwnerCarAttrInfoInstance(Map info) throws DAOException {
        logger.debug("修改业主车辆属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("ownerCarAttrServiceDaoImpl.updateOwnerCarAttrInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改业主车辆属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询业主车辆属性数量
     *
     * @param info 业主车辆属性信息
     * @return 业主车辆属性数量
     */
    @Override
    public int queryOwnerCarAttrsCount(Map info) {
        logger.debug("查询业主车辆属性数据 入参 info : {}", info);

        List<Map> businessOwnerCarAttrInfos = sqlSessionTemplate.selectList("ownerCarAttrServiceDaoImpl.queryOwnerCarAttrsCount", info);
        if (businessOwnerCarAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerCarAttrInfos.get(0).get("count").toString());
    }

    @Override
    public int saveOwnerCarAttr(Map beanCovertMap) {
        logger.debug("保存业主车辆属性信息Instance 入参 info : {}", beanCovertMap);

        int saveFlag = sqlSessionTemplate.insert("ownerCarAttrServiceDaoImpl.saveOwnerCarAttr", beanCovertMap);

        return saveFlag;
    }


}
